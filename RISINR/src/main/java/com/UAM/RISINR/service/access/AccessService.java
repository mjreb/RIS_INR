package com.UAM.RISINR.service.access;

import com.UAM.RISINR.model.dto.access.LoginRequestDTO;
import com.UAM.RISINR.model.dto.access.LoginResponseDTO;
import com.UAM.RISINR.model.dto.access.SeleccionRolRequestDTO;


/* 
Definir por separado la interfaz y el servicio permitirá 
cambiar la implementacion si se cambia la base de datos
*/
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
    
    void logout(String subjectJson, String tipoCierre);
}