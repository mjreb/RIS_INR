package com.UAM.RISINR.repository.projection;

public interface PerfilRolView {
    int getRolId();   // PerfilPK.rolidRol (PK → no nulo)
    int getEstado();  // Perfil.estado (en tu entidad es int)
}
