package com.UAM.RISINR.model.dto.access;

public class CredencialesDTO {
    
    private String contrasena;
    private String usuarioId;
    private String ipAdress;
    
    
    public CredencialesDTO() {}
    
    public CredencialesDTO(String usuarioId,String contrasena,String ipAdress){
        this.usuarioId=usuarioId;
        this.contrasena=contrasena;
        this.ipAdress=ipAdress;
    }
    
    public String getusuarioId(){
        return usuarioId;
    }
    
    public void setusuarioId(String usuarioId){
        this.usuarioId =usuarioId ;
    }
    
    public String getcontrasena(){
        return contrasena;
    }
    
    public void setcontrasena(String contrasena){
        this.contrasena =contrasena ;
    }
    
    public String getipAdress(){
        return ipAdress;
    }
    
    public void setipAdress(String ipAdress){
        this.ipAdress = ipAdress;
    }
}
