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
        WHERE BINARY u.UsuarioID = :usuarioId
          AND BINARY u.Passwd    = :passwd
          AND u.Estado = 'activo'
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
        WHERE BINARY u.UsuarioID = :usuarioId
        """, nativeQuery = true)
    List<UsuarioBasicoView> datosBasicos(@Param("usuarioId") String usuarioId);
    
    @Query(value="select u.UsuarioID as usuarioId, u.Nombre as nombre, u.ApellidoPaterno as apellidoPaterno, " +
       "u.ApellidoMaterno as apellidoMaterno, u.Area_idArea as areaId, " +
       "u.NumEmpleado as numEmpleado, u.CURP as curp " +
       "from Usuario u " +
       "where BINARY u.usuarioID = :usuario", nativeQuery=true)
    List<UsuarioAuthView> buscarBasicoPorUsuario(@Param("usuario") String usuario);
}
