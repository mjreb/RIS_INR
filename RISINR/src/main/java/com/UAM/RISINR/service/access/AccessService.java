package com.UAM.RISINR.service.access;

import com.UAM.RISINR.model.dto.access.LoginRequestDTO;
import com.UAM.RISINR.model.dto.access.LoginResponseDTO;
import com.UAM.RISINR.model.dto.access.SeleccionRolRequestDTO;
import com.UAM.RISINR.model.dto.shared.UsuarioDTO;


/* 
Definir por separado la interfaz y el servicio permitirá 
cambiar la implementacion si se cambia la base de datos
*/
public interface AccessService {

    LoginResponseDTO login(LoginRequestDTO request, String ipDispositivo);

    LoginResponseDTO seleccionarRol(SeleccionRolRequestDTO request, String ipDispositivo);
   
    void logout(String subjectJson, String tipoCierre);
    
    //void bloqueaUsuario(UsuarioDTO usuario);
}