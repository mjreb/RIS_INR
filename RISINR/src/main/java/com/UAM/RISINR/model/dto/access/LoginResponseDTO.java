package com.UAM.RISINR.model.dto.access;

import com.UAM.RISINR.model.dto.shared.AreaDTO;
import com.UAM.RISINR.model.dto.shared.RolDTO;
import java.util.List;

public class LoginResponseDTO {

    private UsuarioResumenDTO usuario; // Para el saludo al elegir rol
    private AreaDTO area;              // Área del usuario
    private List<RolDTO> roles;        // Roles disponibles

    // Solo cuando ya se eligió un rol (Cuando hay mas de uno) y se creó la sesión:
    private String tokenJWT;
   
    // true cuando roles.size() > 1
    private boolean requiereSeleccionRol;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(UsuarioResumenDTO usuario, AreaDTO area, List<RolDTO> roles, String tokenJWT, boolean requiereSeleccionRol) {
        this.usuario = usuario;
        this.area = area;
        this.roles = roles;
        this.tokenJWT = tokenJWT;
        this.requiereSeleccionRol = requiereSeleccionRol;
    }

    public UsuarioResumenDTO getUsuario() { 
        return usuario; 
    }
    
    public void setUsuario(UsuarioResumenDTO usuario) {
        this.usuario = usuario; 
    }

    public AreaDTO getArea() { 
        return area;
    }
    
    public void setArea(AreaDTO area) { 
        this.area = area; 
    }

    public List<RolDTO> getRoles() { 
        return roles; 
    }
    
    public void setRoles(List<RolDTO> roles) { 
        this.roles = roles; 
    }

    public String getTokenJWT() { 
        return tokenJWT; 
    }
    
    public void setTokenJWT(String tokenJWT) { 
        this.tokenJWT = tokenJWT; 
    }

    public boolean isRequiereSeleccionRol() { 
        return requiereSeleccionRol; 
    }
    
    public void setRequiereSeleccionRol(boolean requiereSeleccionRol) {
        this.requiereSeleccionRol = requiereSeleccionRol;
    }
}
