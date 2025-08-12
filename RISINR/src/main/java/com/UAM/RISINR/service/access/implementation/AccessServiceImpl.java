package com.UAM.RISINR.service.access.implementation;

import com.UAM.RISINR.model.Sesion;
import com.UAM.RISINR.model.SesionPK;
import com.UAM.RISINR.model.dto.access.LoginRequestDTO;
import com.UAM.RISINR.model.dto.access.LoginResponseDTO;
import com.UAM.RISINR.model.dto.access.SeleccionRolRequestDTO;
import com.UAM.RISINR.model.dto.shared.UsuarioDTO;
import com.UAM.RISINR.model.dto.shared.AreaDTO;
import com.UAM.RISINR.model.dto.shared.RolDTO;
import com.UAM.RISINR.repository.AreaDeServicioRepository;
import com.UAM.RISINR.repository.DatosAccesoRepository;
import com.UAM.RISINR.repository.PerfilRepository;
import com.UAM.RISINR.repository.RolRepository;
import com.UAM.RISINR.repository.SesionRepository;
import com.UAM.RISINR.repository.projection.PerfilRolView;
import com.UAM.RISINR.repository.projection.RolView;
import com.UAM.RISINR.service.access.AccessService;
import com.UAM.RISINR.service.shared.JwtService;
import com.UAM.RISINR.service.model.JwtSessionInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
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

    private final DatosAccesoRepository accesoRepo;
    private final PerfilRepository perfilRepo;
    private final RolRepository rolRepo;
    private final AreaDeServicioRepository areaRepo;
    private final SesionRepository sesionRepo;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    // AplicacionID fijo en 1
    private static final int APLICACION_ID = 1;

    public AccessServiceImpl(DatosAccesoRepository accesoRepo,
                             PerfilRepository perfilRepo,
                             RolRepository rolRepo,
                             AreaDeServicioRepository areaRepo,
                             SesionRepository sesionRepo,
                             JwtService jwtService,
                             ObjectMapper objectMapper) {
        this.accesoRepo=accesoRepo;
        this.perfilRepo = perfilRepo;
        this.rolRepo = rolRepo;
        this.areaRepo = areaRepo;
        this.sesionRepo = sesionRepo;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
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
        // 1) Autenticar 
        var match = accesoRepo.findByIdUsuarioID(request.getUsuario());// Regresa List con datos de usuarios con coincidencias en ID + Contraseñas
        /*
        System.out.println("============================================");
        System.out.println("DEBUG :: tamaño de matches = " + matches.size());
        for (int i = 0; i < matches.size(); i++) {
            var m = matches.get(i);
            System.out.println("DEBUG :: fila " + (i + 1) + " -> "
                + "usuarioId=" + m.getUsuarioId()
                + ", nombre=" + m.getNombre()
                + ", apellidoPaterno=" + m.getApellidoPaterno()
                + ", apellidoMaterno=" + m.getApellidoMaterno()
                + ", areaId=" + m.getAreaId()
                + ", numEmpleado=" + m.getNumEmpleado()
                + ", curp=" + m.getCurp()
                + ", estado=" + m.getEstado());
        }
        System.out.println("============================================");
        */

        if (match == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario invalido");
        }
        if (!match.get().getContrasena().equals(request.getContrasena())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contrasela invalida");
        }

        // 2) Usuario resumen (Util para saludar)
        var usuario = new UsuarioDTO(
                                         match.get().getId().getUsuarioNumEmpleado(),
                                          match.get().getId().getUsuarioCURP(),
                                         match.get().getUsuario().getAreaidArea().getIdArea(),
                                         match.get().getUsuario().getNombre(),
                                         match.get().getUsuario().getApellidoPaterno(),
                                        match.get().getUsuario().getApellidoMaterno());
        

        // 3) Área
        Integer areaId = usuario.getarea_idArea();
        AreaDTO area = areaRepo.findById(areaId)
                .map(a -> new AreaDTO(a.getIdArea(), a.getNombre(), a.getDescripcion()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "El área asociada al usuario no existe (id=" + areaId + ").")
                );

        // 4) Roles: ids/estado (Perfil) → detalle (Rol)
        var perfiles = perfilRepo.findByPerfilPKUsuarioNumEmpleadoAndPerfilPKUsuarioCURP(usuario.getnumEmpleado(), usuario.getcurp()); //List con Roles del usuario
        if (perfiles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no tiene roles asignados");
        }
        List<Integer> rolIds = perfiles.stream()
                .map(PerfilRolView::getPerfilPKRolidRol)
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
                    usuario.getnumEmpleado(),
                    usuario.getcurp(),
                    APLICACION_ID
            );
            var sesion = new Sesion(spk, ip15, match.get().getId().getUsuarioID());
            sesion.setRolNombre(unico.getNombre());
            sesionRepo.save(sesion);

            String token = jwtService.emitirToken(usuario.getnumEmpleado(),
                    usuario.getcurp(),
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
        var usuario = accesoRepo.findByIdUsuarioID(request.getUsuarioId());

        // 2) Área OBLIGATORIA
        Integer areaId = usuario.get().getUsuario().getAreaidArea().getIdArea();
        if (areaId == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario no tiene área asignada.");
        }
        AreaDTO area = areaRepo.findById(areaId)
                .map(a -> new AreaDTO(a.getIdArea(), a.getNombre(), a.getDescripcion()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "El área asociada al usuario no existe (id=" + areaId + ").")
                );

        // 3) Obtener el detalle del rol elegido (nombre/descripcion) — usando la proyección que ya tienes
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
                usuario.get().getId().getUsuarioNumEmpleado(),
                usuario.get().getId().getUsuarioCURP(),
                APLICACION_ID
        );

        Sesion sesion = new Sesion(spk, ip15, usuario.get().getId().getUsuarioID());
        sesion.setRolNombre(rolElegido.getNombre());
        sesionRepo.save(sesion);

        String token = jwtService.emitirToken(
                usuario.get().getId().getUsuarioNumEmpleado(),
                usuario.get().getId().getUsuarioCURP(),
                horaInicio,
                APLICACION_ID
        );

        // 6) Usuario en respuesta (mismo criterio que en login)
        var usuariodto = new UsuarioDTO(
                                         usuario.get().getId().getUsuarioNumEmpleado(),
                                          usuario.get().getId().getUsuarioCURP(),
                                         usuario.get().getUsuario().getAreaidArea().getIdArea(),
                                         usuario.get().getUsuario().getNombre(),
                                         usuario.get().getUsuario().getApellidoPaterno(),
                                        usuario.get().getUsuario().getApellidoMaterno());

        // 7) Responder con SOLO el rol elegido y requiereSeleccionRol=false
        return new LoginResponseDTO(usuariodto, area, List.of(rolElegido), token, false);
    }
    
    @Override
    @Transactional
    public void logout(String subjectJson, String tipoCierre) {
        try {
            // subjectJson tiene { nme, curp, hst, asi } tal como lo emites en JwtService
            JwtSessionInfo info = objectMapper.readValue(subjectJson, JwtSessionInfo.class);

            // Construir la PK EXACTA que insertaste al hacer login
            SesionPK pk = new SesionPK(
                    info.getHoraInicio(),
                    info.getNumEmpleado(),
                    info.getCurp(),
                    info.getAplicacionId()
            );

            var sesionOpt = sesionRepo.findById(pk);
            if (sesionOpt.isEmpty()) {
                return;
            }

            var sesion = sesionOpt.get();
            sesion.setHoraFin(BigInteger.valueOf(System.currentTimeMillis()));
            sesion.setTipoCierre(tipoCierre);
            sesionRepo.save(sesion);

        } catch (Exception e) {
            // Si el subject no se puede parsear, no podemos cerrar sesión
            // Loguea el error si lo necesitas.
        }
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