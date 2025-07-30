package com.UAM.RISINR.repository;

import com.UAM.RISINR.model.Usuario;
import com.UAM.RISINR.model.UsuarioPK;
import com.UAM.RISINR.repository.projection.UsuarioAuthView;
import com.UAM.RISINR.repository.projection.UsuarioBasicoView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioPK> {

    // Autenticación por UsuarioID + Passwd (NO exponemos passwd)
    @Query(value = """
        SELECT
            u.UsuarioID       AS usuarioId,
            u.Nombre          AS nombre,
            u.ApellidoPaterno AS apellidoPaterno,
            u.ApellidoMaterno AS apellidoMaterno,
            u.Area_idArea     AS areaId,
            u.NumEmpleado     AS numEmpleado,
            u.CURP            AS curp,
            u.Estado          AS estado
        FROM Usuario u
        WHERE u.UsuarioID = :usuarioId
          AND u.Passwd    = :passwd
        """, nativeQuery = true)
    List<UsuarioAuthView> autenticar(@Param("usuarioId") String usuarioId,
                                                                        @Param("passwd")    String passwd);

    // Datos mínimos para reconstruir la PK (numEmpleado + curp) en endpoint para seleccionar rol
    @Query(value = """
        SELECT
            u.UsuarioID   AS usuarioId,
            u.NumEmpleado AS numEmpleado,
            u.CURP        AS curp
        FROM Usuario u
        WHERE u.UsuarioID = :usuarioId
        """, nativeQuery = true)
    List<UsuarioBasicoView> datosBasicos(@Param("usuarioId") String usuarioId);
}
