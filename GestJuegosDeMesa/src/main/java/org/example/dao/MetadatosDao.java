package org.example.dao;

import org.example.models.InfoBaseDatos;
import org.example.models.InfoColumna;
import org.example.models.InfoTabla;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Lee los metadatos con DatabaseMetaData: la información que JDBC da sobre la propia base de datos. */
public class MetadatosDao {

    public InfoBaseDatos obtener() throws SQLException {
        Connection conexion = DatabaseConnection.getInstance().getConnection();
        DatabaseMetaData meta = conexion.getMetaData();
        String baseDatos = conexion.getCatalog();  // nombre de la base de datos en uso

        InfoBaseDatos info = new InfoBaseDatos(baseDatos,
                meta.getDatabaseProductName(),
                meta.getDatabaseProductVersion(),
                meta.getDriverName(),
                meta.getDriverVersion(),
                meta.getURL(),
                meta.getUserName());

        // Solo las tablas normales (TABLE), no las vistas ni las del sistema
        try (ResultSet rs = meta.getTables(baseDatos, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                // En MariaDB el esquema viene en TABLE_CAT; en otros SGBD, en TABLE_SCHEM
                String esquema = rs.getString("TABLE_SCHEM") != null ? rs.getString("TABLE_SCHEM") : rs.getString("TABLE_CAT");
                info.getTablas().add(new InfoTabla(esquema, rs.getString("TABLE_NAME")));
            }
        }

        for (InfoTabla tabla : info.getTablas()) {
            try (ResultSet rs = meta.getPrimaryKeys(baseDatos, null, tabla.getNombre())) {
                while (rs.next()) {
                    tabla.getClavePrimaria().add(rs.getString("COLUMN_NAME"));
                }
            }
            try (ResultSet rs = meta.getColumns(baseDatos, null, tabla.getNombre(), "%")) {
                while (rs.next()) {
                    // MariaDB devuelve el texto "NULL" cuando la columna no tiene valor por defecto
                    String porDefecto = rs.getString("COLUMN_DEF");
                    if ("NULL".equalsIgnoreCase(porDefecto)) {
                        porDefecto = null;
                    }
                    tabla.getColumnas().add(new InfoColumna(rs.getString("COLUMN_NAME"),
                            rs.getString("TYPE_NAME"),
                            rs.getInt("COLUMN_SIZE"),
                            "YES".equals(rs.getString("IS_NULLABLE")),
                            "YES".equals(rs.getString("IS_AUTOINCREMENT")),
                            porDefecto));
                }
            }
        }
        return info;
    }
}
