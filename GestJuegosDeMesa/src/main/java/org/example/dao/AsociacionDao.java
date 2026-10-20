package org.example.dao;

import org.example.models.Asociacion;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AsociacionDao {

    public Asociacion obtener() throws SQLException {
        String query = "select * from asociacion where id = 1";
        try (PreparedStatement ps = conexion().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new Asociacion(rs.getString("razon_social"),
                        rs.getString("cif"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fecha_apertura").toLocalDate());
            }
            return null;
        }
    }

    public void actualizar(Asociacion asociacion) throws SQLException {
        String query = "update asociacion set razon_social=?, cif=?, direccion=?, telefono=?, email=?, fecha_apertura=? where id = 1";
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            ps.setString(1, asociacion.getRazonSocial());
            ps.setString(2, asociacion.getCif());
            ps.setString(3, asociacion.getDireccion());
            ps.setString(4, asociacion.getTelefono());
            ps.setString(5, asociacion.getEmail());
            ps.setDate(6, Date.valueOf(asociacion.getFechaApertura()));
            ps.executeUpdate();
        }
    }

    private Connection conexion() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
}