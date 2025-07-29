package com.UAM.RISINR.model.dto.access;

public class LogoutRequestDTO {

    private String token;       // JWT actual
    private String tipoCierre;  // "NATURAL" | "INACTIVIDAD"

    public LogoutRequestDTO() {
    }

    public LogoutRequestDTO(String token, String tipoCierre) {
        this.token = token;
        this.tipoCierre = tipoCierre;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipoCierre() { return tipoCierre; }
    public void setTipoCierre(String tipoCierre) { this.tipoCierre = tipoCierre; }
}
