package com.UAM.RISINR.model.dto.access;

public class LoginRequestDTO {
    
    private String usuarioId;
    private String passwd;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String usuarioId, String passwd) {
        this.usuarioId = usuarioId;
        this.passwd = passwd;
    }

    public String getUsuario() {
        return usuarioId;
    }

    public void setUsuario(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getContrasena() {
        return passwd;
    }

    public void setContrasena(String contrasena) {
        this.passwd = contrasena;
    }
}