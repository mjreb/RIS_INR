package com.UAM.RISINR.service.implementations;

import com.UAM.RISINR.model.Sesion;
import com.UAM.RISINR.model.SesionPK;
import com.UAM.RISINR.model.dto.access.LoginRequestDTO;
import com.UAM.RISINR.model.dto.access.LoginResponseDTO;
import com.UAM.RISINR.model.dto.access.SeleccionRolRequestDTO;
import com.UAM.RISINR.model.dto.access.UsuarioResumenDTO;
import com.UAM.RISINR.model.dto.shared.AreaDTO;
import com.UAM.RISINR.model.dto.shared.RolDTO;
import com.UAM.RISINR.repository.AreaDeServicioRepository;
import com.UAM.RISINR.repository.PerfilRepository;
import com.UAM.RISINR.repository.RolRepository;
import com.UAM.RISINR.repository.SesionRepository;
import com.UAM.RISINR.repository.UsuarioRepository;
import com.UAM.RISINR.repository.projection.PerfilRolView;
import com.UAM.RISINR.repository.projection.RolView;
import com.UAM.RISINR.repository.projection.UsuarioAuthView;
import com.UAM.RISINR.service.AccessService;
import com.UAM.RISINR.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccessServiceImpl implements AccessService {

    private final UsuarioRepository usuarioRepo;
    private final PerfilRepository perfilRepo;
    private final RolRepository rolRepo;
    private final AreaDeServicioRepository areaRepo;
    private final SesionRepository sesionRepo;
    private final JwtService jwtService;

    // AplicacionID fijo en 1
    private static final int APLICACION_ID = 1;

    public AccessServiceImpl(UsuarioRepository usuarioRepo,
                             PerfilRepository perfilRepo,
                             RolRepository rolRepo,
                             AreaDeServicioRepository areaRepo,
                             SesionRepository sesionRepo,
                             JwtService jwtService) {
        this.usuarioRepo = usuarioRepo;
        this.perfilRepo = perfilRepo;
        this.rolRepo = rolRepo;
        this.areaRepo = areaRepo;
        this.sesionRepo = sesionRepo;
        this.jwtService = jwtService;
    }

    /** LOGIN:
     * - Autentica con UsuarioRepository.autenticar(...) (sin exponer Passwd).
     * - Arma usuario/área.
     * - Lee roles (Perfil) y detalla (Rol).
     * - Si hay 1 rol: crea Sesión + emite token. Si >1: requiere selección (sin token).
     */
    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request, String ipDispositivo) {
        // 1) Autenticar (0/1/>1 por BD legacy sin unique en UsuarioID)
        var matches = usuarioRepo.autenticar(request.getUsuario(), request.getContrasena()); //  Regresa List con datos de usuarios con coincidencias en ID + Contraseñas
        if (matches.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        if (matches.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "UsuarioID repetido para las credenciales dadas");
        }
        UsuarioAuthView auth = matches.get(0);

        // 2) Usuario resumen (Util para saludar)
        var usuario = new UsuarioResumenDTO(
                auth.getUsuarioId(),
                auth.getNombre(),
                auth.getApellidoPaterno(),
                auth.getApellidoMaterno()
        );

        // 3) Área
        Integer areaId = auth.getAreaId();
        if (areaId != null) {
        // Datos inconsistentes: el usuario no tiene área asignada
        throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario no tiene área asignada.");
        }
        AreaDTO area = areaRepo.findById(areaId)
                .map(a -> new AreaDTO(a.getIdArea(), a.getNombre(), a.getDescripcion()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "El área asociada al usuario no existe (id=" + areaId + ").")
                );

        // 4) Roles: ids/estado (Perfil) → detalle (Rol)
        var perfiles = perfilRepo.rolesDeUsuario(auth.getNumEmpleado(), auth.getCurp()); //List con Roles del usuario
        if (perfiles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene roles asignados");
        }
        List<Integer> rolIds = perfiles.stream()
                .map(PerfilRolView::getRolId)
                .distinct()
                .collect(Collectors.toList());

        var rolRows = rolRepo.findByIdRolIn(rolIds);
        Map<Integer, RolView> byId = new LinkedHashMap<>();
        rolRows.forEach(r -> byId.put(r.getIdRol(), r));

        List<RolDTO> roles = new ArrayList<>();
        for (Integer id : rolIds) {
            var r = byId.get(id);
            if (r != null) {
                roles.add(new RolDTO(r.getIdRol(), r.getNombre(), r.getDescripcion()));
            }
        }

        // 5) 1 rol → crear sesión + token; >1 → requiere selección
        if (roles.size() == 1) {
            RolDTO unico = roles.get(0);

            long horaInicio = System.currentTimeMillis();
            String ip15 = normalizarIp(ipDispositivo);

            var spk = new SesionPK(
                    horaInicio,
                    auth.getNumEmpleado(),
                    auth.getCurp(),
                    APLICACION_ID
            );
            var sesion = new Sesion(spk, ip15);
            sesion.setRolNombre(unico.getNombre());
            sesionRepo.save(sesion);

            String token = jwtService.emitirToken(auth.getNumEmpleado(),
                    auth.getCurp(),
                    horaInicio,
                    APLICACION_ID
            );

            // Opción 1: 'roles' solo con el elegido
            return new LoginResponseDTO(usuario, area, List.of(unico), token, false);
        } else {
            // varios roles → sin sesión ni token
            return new LoginResponseDTO(usuario, area, roles, null, true);
        }
    }

    @Override
    @Transactional
    public LoginResponseDTO seleccionarRol(SeleccionRolRequestDTO request, String ipDispositivo) {

        // 0) Validaciones de entrada básicas
        if (request == null || request.getUsuarioId() == null || request.getUsuarioId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario requerido.");
        }
        if (request.getIdRol() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rolId requerido.");
        }

        // 1) Ubicar al usuario por su UsuarioID (SIN contraseña en este paso)
        //    Esperamos 0, 1 o >1 (por legado sin unique en UsuarioID)
        List<UsuarioAuthView> candidatos = usuarioRepo.buscarBasicoPorUsuario(request.getUsuarioId());
        if (candidatos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }
        if (candidatos.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "UsuarioID ambiguo (duplicado).");
        }
        UsuarioAuthView auth = candidatos.get(0);

        // 2) Área OBLIGATORIA
        Integer areaId = auth.getAreaId();
        if (areaId == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario no tiene área asignada.");
        }
        AreaDTO area = areaRepo.findById(areaId)
                .map(a -> new AreaDTO(a.getIdArea(), a.getNombre(), a.getDescripcion()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "El área asociada al usuario no existe (id=" + areaId + ").")
                );

        // 3) Validar que el rol elegido pertenece al usuario
        boolean pertenece = perfilRepo.perteneceRol(auth.getNumEmpleado(), auth.getCurp(), request.getIdRol());
        if (!pertenece) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El rol elegido no pertenece al usuario.");
        }

        // 4) Obtener el detalle del rol elegido (nombre/descripcion) — usando la proyección que ya tienes
        List<RolView> rolRows = rolRepo.findByIdRolIn(List.of(request.getIdRol()));
        if (rolRows.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El rol elegido no existe.");
        }
        RolView r = rolRows.get(0);
        RolDTO rolElegido = new RolDTO(r.getIdRol(), r.getNombre(), r.getDescripcion());

        // 5) Crear Sesión (PK SIN UsuarioID) y emitir token
        long horaInicio = System.currentTimeMillis();
        String ip15 = normalizarIp(ipDispositivo);

        SesionPK spk = new SesionPK(
                horaInicio,
                auth.getNumEmpleado(),
                auth.getCurp(),
                APLICACION_ID
        );

        Sesion sesion = new Sesion(spk, ip15);
        sesion.setRolNombre(rolElegido.getNombre());
        sesionRepo.save(sesion);

        String token = jwtService.emitirToken(
                auth.getNumEmpleado(),
                auth.getCurp(),
                horaInicio,
                APLICACION_ID
        );

        // 6) Usuario en respuesta (mismo criterio que en login)
        UsuarioResumenDTO usuario = new UsuarioResumenDTO(
                auth.getUsuarioId(),
                auth.getNombre(),
                auth.getApellidoPaterno(),
                auth.getApellidoMaterno()
        );

        // 7) Responder con SOLO el rol elegido y requiereSeleccionRol=false
        return new LoginResponseDTO(usuario, area, List.of(rolElegido), token, false);
    }

    @Override
    @Transactional
    public void logout(String tokenJWT, String tipoCierre) {

        if (tokenJWT == null || tokenJWT.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token requerido.");
        }
        if (tipoCierre == null || tipoCierre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipoCierre requerido.");
        }

        // 1) Validar/parsear token y reconstruir la PK de la Sesión
        final var info = jwtService.parseToken(tokenJWT);
        final var pk = new SesionPK(
                info.getHoraInicio(),      // hst
                info.getNumEmpleado(),     // nme
                info.getCurp(),            // curp
                info.getAplicacionId()     // asi
        );

        // 2) Buscar la Sesión y actualizar fin de sesión + tipo de cierre
        var sesion = sesionRepo.findById(pk)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sesión no encontrada para el token provisto."));

        sesion.setHoraFin(java.math.BigInteger.valueOf(System.currentTimeMillis()));
        sesion.setTipoCierre(normalizarTipoCierre(tipoCierre));

        sesionRepo.save(sesion);
    }

    private String normalizarIp(String ip) {
        if (ip == null) return null;
        // Columna VARCHAR(15) → IPv4
        return (ip.length() > 15) ? ip.substring(0, 15) : ip;
    }
    
    private String normalizarTipoCierre(String tc) {
    if (tc == null) return null;
    // Columna VARCHAR(25)
    tc = tc.trim();
    return tc.length() > 25 ? tc.substring(0, 25) : tc;
}
}