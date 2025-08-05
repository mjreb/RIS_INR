package com.UAM.RISINR.controller;

import com.UAM.RISINR.model.dto.access.LoginRequestDTO;
import com.UAM.RISINR.model.dto.access.LoginResponseDTO;
import com.UAM.RISINR.model.dto.access.SeleccionRolRequestDTO;
import com.UAM.RISINR.service.AccessService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request,
                                                  HttpServletRequest httpReq) {

        String ip = extraerIp(httpReq);
        LoginResponseDTO resp = accessService.login(request, ip);
        return ResponseEntity.ok(resp);
    }

    // ---------- SELECCIONAR ROL ----------
    @PostMapping("/seleccionar-rol")
    public ResponseEntity<LoginResponseDTO> seleccionarRol(@RequestBody SeleccionRolRequestDTO request,
                                                           HttpServletRequest httpReq) {

        String ip = extraerIp(httpReq);
        LoginResponseDTO resp = accessService.seleccionarRol(request, ip);
        return ResponseEntity.ok(resp);
    }

    // ---------- LOGOUT ----------
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                       @RequestParam("tipoCierre") String tipoCierre) {

        String token = extraerToken(authHeader);
        accessService.logout(token, tipoCierre);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
    }

    /** Extrae la primera IP válida (X-Forwarded-For o remoteAddr) y la acorta a 15 chars. */
    private String extraerIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            int coma = ip.indexOf(',');
            return (coma > 0 ? ip.substring(0, coma) : ip).trim(); // sin truncar
        }
        ip = req.getHeader("X-Real-IP");
        if (ip != null && !ip.isBlank()) return ip.trim();
        return req.getRemoteAddr();
    }

    /** Obtiene el token de 'Authorization: Bearer <token>'. */
    private String extraerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Header Authorization inválido o faltante");
        }
        return authHeader.substring("Bearer ".length());
    }
}
