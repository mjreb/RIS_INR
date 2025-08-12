package com.UAM.RISINR.model.dto.access;

public class DatosAccesoDTO {
    private int numEmpleado;
    private String curp;
    private String usuarioId;
    private String contrasena;
    private String estado;
    
    public DatosAccesoDTO() {}
    
    public DatosAccesoDTO(int numEmpleado, String curp, String usuarioId, String contrasena, String estado){
        this.numEmpleado = numEmpleado;
        this.curp                     = curp;
        this.usuarioId            = usuarioId;
        this.contrasena         = contrasena;
        this.estado                 = estado;
    }
    
    public int getnumEmpleado() {
        return numEmpleado; 
    }
    
    public void setnumEmpleado(int numEmpleado ) { 
        this.numEmpleado =numEmpleado ; 
    }

    public String getcurp() {
        return curp; 
    }
    
    public void setcurp(String curp ) { 
        this.curp = curp; 
    }
    
    public String getusuarioId(){
        return usuarioId;
    }
    
    public void setusuarioId(String usuarioId){
        this.usuarioId = usuarioId;
    }
    
    public String getcontrasena(){
        return contrasena;
    }
    
    public void setcontrasena(String contrasena){
        this.contrasena = contrasena;
    }
    
    public String getestado(){
        return estado;
    }
    
    public void setestado(String estado){
        this.estado = estado;
    }
}

