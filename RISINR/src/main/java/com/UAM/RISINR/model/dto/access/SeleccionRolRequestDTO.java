package com.UAM.RISINR.model.dto.access;

public class SeleccionRolRequestDTO {

    private String usuarioId;  
    private Integer idRol;     

    public SeleccionRolRequestDTO() {
    }

    public SeleccionRolRequestDTO(String usuarioId, Integer idRol) {
        this.usuarioId = usuarioId;
        this.idRol = idRol;
    }

    public String getUsuarioId() {
        return usuarioId; 
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId; 
    }

    public Integer getIdRol() { 
        return idRol; 
    }
    public void setIdRol(Integer idRol) { 
        this.idRol = idRol; 
    }
}