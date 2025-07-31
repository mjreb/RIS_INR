package com.UAM.RISINR.repository.projection;

public interface UsuarioAuthView {
    String  getUsuarioId();       // Usuario.UsuarioID
    String  getNombre();          // Usuario.Nombre
    String  getApellidoPaterno(); // Usuario.ApellidoPaterno
    String  getApellidoMaterno(); // Usuario.ApellidoMaterno
    Integer getAreaId();          // Usuario.Area_idArea
    int getNumEmpleado();     // Usuario.NumEmpleado
    String  getCurp();            // Usuario.CURP
    String  getEstado();          // Usuario.Estado
}
