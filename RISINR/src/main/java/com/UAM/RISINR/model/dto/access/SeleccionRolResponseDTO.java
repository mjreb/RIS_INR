package com.UAM.RISINR.model.dto.access;

import com.UAM.RISINR.model.dto.shared.RolDTO;

public class SeleccionRolResponseDTO {

    private String tokenJWT;
    private RolDTO rolSeleccionado;

    public SeleccionRolResponseDTO() {
    }

    public SeleccionRolResponseDTO(String tokenJWT, RolDTO rolSeleccionado) {
        this.tokenJWT = tokenJWT;
        this.rolSeleccionado = rolSeleccionado;
    }

    public String getTokenJWT() { 
        return tokenJWT; 
    }
    
    public void setTokenJWT(String tokenJWT) { 
        this.tokenJWT = tokenJWT; 
    }

    public RolDTO getRolSeleccionado() {
        return rolSeleccionado; 
    }
    
    public void setRolSeleccionado(RolDTO rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado; 
    }
}
