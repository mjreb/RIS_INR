package com.UAM.RISINR.model.dto.access;

public class UsuarioResumenDTO {

    private String usuarioId;        // UsuarioID con el que se autentica
    private String nombre;           // Nombre
    private String apellidoPaterno;  // ApellidoPaterno
    private String apellidoMaterno;  // ApellidoMaterno

    public UsuarioResumenDTO() {
    }

    public UsuarioResumenDTO(String usuarioId, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getUsuarioId() {
        return usuarioId; 
    }
    
    public void setUsuarioId(String usuarioId) { 
        this.usuarioId = usuarioId; 
    }

    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getApellidoPaterno() {
        return apellidoPaterno; 
    }
    
    public void setApellidoPaterno(String apellidoPaterno) { 
        this.apellidoPaterno = apellidoPaterno; 
    }

    public String getApellidoMaterno() { 
        return apellidoMaterno; 
    }
    public void setApellidoMaterno(String apellidoMaterno) { 
        this.apellidoMaterno = apellidoMaterno; 
    }
}