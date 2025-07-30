package com.UAM.RISINR.repository;

import com.UAM.RISINR.model.Perfil;
import com.UAM.RISINR.model.PerfilPK;
import com.UAM.RISINR.repository.projection.PerfilRolView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerfilRepository extends JpaRepository<Perfil, PerfilPK> {

    // JPQL: propiedades de la entidad (no nombres de columnas)
    @Query("""
       select p.perfilPK.rolidRol   as rolId,
                    p.estado                    as estado
       from Perfil p
       where p.perfilPK.usuarioNumEmpleado = :numEmpleado
                        and p.perfilPK.usuarioCURP        = :curp
       """)
    List<PerfilRolView> rolesDeUsuario(@Param("numEmpleado") Integer numEmpleado,
                                                                        @Param("curp")        String  curp);

    // (Opcional) Validar si un rol pertenece al usuario (útil en /seleccionar-rol)
    @Query("""
       select (count(p) > 0)
       from Perfil p
       where p.perfilPK.usuarioNumEmpleado = :numEmpleado
                        and p.perfilPK.usuarioCURP        = :curp
                        and p.perfilPK.rolidRol           = :rolId
       """)
    boolean perteneceRol(@Param("numEmpleado") Integer numEmpleado,
                                                @Param("curp")        String  curp,
                                                @Param("rolId")       Integer rolId);
}
