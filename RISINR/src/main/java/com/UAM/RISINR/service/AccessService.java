package com.UAM.RISINR.service;

import com.UAM.RISINR.model.dto.access.LoginRequestDTO;
import com.UAM.RISINR.model.dto.access.LoginResponseDTO;
import com.UAM.RISINR.model.dto.access.SeleccionRolRequestDTO;

public interface AccessService {

    /**
     * Autentica al usuario. Si tiene un solo rol: crea sesión y devuelve token.
     * Si tiene varios roles: devuelve la lista de roles y requiereSeleccionRol=true (sin token).
     */
    LoginResponseDTO login(LoginRequestDTO request, String ipDispositivo);

    /**
     * Confirma el rol elegido cuando el usuario tenía varias opciones; crea sesión y devuelve token.
     */
    LoginResponseDTO seleccionarRol(SeleccionRolRequestDTO request, String ipDispositivo);

    /**
     * Cierra la sesión asociada al token: registra horaFin y tipoCierre.
     */
    void logout(String tokenJWT, String tipoCierre);
}