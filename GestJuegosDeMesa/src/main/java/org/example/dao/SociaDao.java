package org.example.dao;

import org.example.models.Socia;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SociaDao {

    /** Socias activas (las dadas de baja no aparecen). */
    public List<Socia> listar() throws SQLException {
        String query = "select * from socias where eliminada = false order by apellidos, nombre";
        List<Socia> socias = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                socias.add(crearSocia(rs));
            }
        }
        return socias;
    }

    /** Socia activa con ese id, o null si no existe o está dada de baja. */
    public Socia buscarPorId(int id) throws SQLException {
        String query = "select * from socias where id = ? and eliminada = false";
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? crearSocia(rs) : null;
            }
        }
    }

    /** Comprueba si otra socia (activa o de baja) ya tiene ese documento. idExcluido: la propia socia al modificar. */
    public boolean existeDocumento(String documento, int idExcluido) throws SQLException {
        String query = "select count(*) from socias where documento = ? and id <> ?";
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            ps.setString(1, documento);
            ps.setInt(2, idExcluido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /** Inserta la socia y devuelve el id generado. */
    public int insertar(Socia socia) throws SQLException {
        String query = "insert into socias (documento, nombre, apellidos, telefono, edad, email) values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            rellenar(ps, socia);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    socia.setId(rs.getInt(1));
                }
            }
        }
        return socia.getId();
    }

    public void actualizar(Socia socia) throws SQLException {
        String query = "update socias set documento=?, nombre=?, apellidos=?, telefono=?, edad=?, email=? where id=?";
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            rellenar(ps, socia);
            ps.setInt(7, socia.getId());
            ps.executeUpdate();
        }
    }

    // nO borramos el registro, lo marcamos como borrado para no perder partidas pasadas.
    //pero si que le borramos las partidas que todavia no ha jugado
    public void darDeBaja(int id) throws SQLException {
        String quitarDePartidasFuturas = "delete from partida_jugadoras where socia_id = ? "
                + "and partida_id in (select id from partidas where fecha >= curdate())";
        try (PreparedStatement ps = conexion().prepareStatement(quitarDePartidasFuturas)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = conexion().prepareStatement("update socias set eliminada = true where id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ---------- privados ----------

    private void rellenar(PreparedStatement ps, Socia socia) throws SQLException {
        ps.setString(1, socia.getDocumento());
        ps.setString(2, socia.getNombre());
        ps.setString(3, socia.getApellidos());
        ps.setString(4, socia.getTelefono());
        ps.setInt(5, socia.getEdad());
        ps.setString(6, socia.getEmail());
    }

    /** Convierte la fila actual del ResultSet en una Socia. */
    public static Socia crearSocia(ResultSet rs) throws SQLException {
        return new Socia(rs.getInt("id"),
                rs.getString("documento"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("telefono"),
                rs.getInt("edad"),
                rs.getString("email"));
    }

    private Connection conexion() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
}