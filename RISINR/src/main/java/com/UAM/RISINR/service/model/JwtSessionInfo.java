package com.UAM.RISINR.service.model;

public class JwtSessionInfo {

    private final int numEmpleado;     // nme
    private final String curp;         // curp
    private final long horaInicio;     // hst (epoch millis)
    private final int aplicacionId;    // asi
    
    public JwtSessionInfo(int numEmpleado, String curp, long horaInicio, int aplicacionId) {
        this.numEmpleado = numEmpleado;
        this.curp = curp;
        this.horaInicio = horaInicio;
        this.aplicacionId = aplicacionId;
    }

    public int getNumEmpleado() { return numEmpleado; }
    public String getCurp() { return curp; }
    public long getHoraInicio() { return horaInicio; }
    public int getAplicacionId() { return aplicacionId; }
}