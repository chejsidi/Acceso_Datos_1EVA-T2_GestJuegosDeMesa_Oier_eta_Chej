package org.example.dao;

import org.example.models.Juego;
import org.example.models.Socia;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class JuegoDao {
    /**
     * Juego con los datos de su propietaria. LEFT JOIN porque si el juego es de la asociación
     * no tiene socia (socia_id es NULL). Las columnas de la socia llevan alias s_ para no
     * confundirse con las del juego.
     */
    public static final String SELECT_JUEGOS = "select j.*, s.documento as s_documento, s.nombre as s_nombre, "
            + "s.apellidos as s_apellidos, s.telefono as s_telefono, s.edad as s_edad, s.email as s_email "
            + "from juegos j left join socias s on j.socia_id = s.id ";

    /** Juegos activos (los dados de baja no aparecen). */
    public List<Juego> listar() throws SQLException {
        return consultar(SELECT_JUEGOS + "where j.eliminado = false order by j.nombre", 0);
    }

    /** Juegos activos de una socia. */
    public List<Juego> listarPorSocia(int sociaId) throws SQLException {
        return consultar(SELECT_JUEGOS + "where j.eliminado = false and j.socia_id = ? order by j.nombre", sociaId);
    }

    /** Juego activo con ese id, o null si no existe o está dado de baja. */
    public Juego buscarPorId(int id) throws SQLException {
        List<Juego> juegos = consultar(SELECT_JUEGOS + "where j.eliminado = false and j.id = ?", id);
        return juegos.isEmpty() ? null : juegos.get(0);
    }

    /** Inserta el juego y devuelve el id generado. */
    public int insertar(Juego juego) throws SQLException {
        String query = "insert into juegos (nombre, autores, descripcion, min_jugadores, max_jugadores, duracion, "
                + "tematica, tipo, ubicacion, estado, socia_id, fecha_alta) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            rellenar(ps, juego);
            ps.setDate(12, Date.valueOf(juego.getFechaAlta()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    juego.setId(rs.getInt(1));
                }
            }
        }
        return juego.getId();
    }

    public void actualizar(Juego juego) throws SQLException {
        String query = "update juegos set nombre=?, autores=?, descripcion=?, min_jugadores=?, max_jugadores=?, "
                + "duracion=?, tematica=?, tipo=?, ubicacion=?, estado=?, socia_id=? where id=?";
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            rellenar(ps, juego);
            ps.setInt(12, juego.getId());
            ps.executeUpdate();
        }
    }

    /** Número de partidas del juego que todavía no se han jugado. */
    public int contarPartidasFuturas(int juegoId) throws SQLException {
        String query = "select count(*) from partidas where juego_id = ? and fecha >= curdate()";
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            ps.setInt(1, juegoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /** El juego deja de aparecer, pero las partidas pasadas siguen apuntando a él. */
    public void darDeBaja(int id) throws SQLException {
        try (PreparedStatement ps = conexion().prepareStatement("update juegos set eliminado = true where id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ---------- privados ----------

    /** Ejecuta una consulta de juegos. Si parametro es distinto de 0, se pone en el único "?". */
    private List<Juego> consultar(String query, int parametro) throws SQLException {
        List<Juego> juegos = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            if (parametro != 0) {
                ps.setInt(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    juegos.add(crearJuego(rs));
                }
            }
        }
        return juegos;
    }

    private void rellenar(PreparedStatement ps, Juego juego) throws SQLException {
        ps.setString(1, juego.getNombre());
        ps.setString(2, juego.getAutores());
        ps.setString(3, juego.getDescripcion());
        ps.setInt(4, juego.getMinJugadores());
        ps.setInt(5, juego.getMaxJugadores());
        ps.setInt(6, juego.getDuracion());
        ps.setString(7, juego.getTematica());
        ps.setString(8, juego.getTipo().name());
        ps.setString(9, juego.getUbicacion());
        ps.setString(10, juego.getEstado().name());
        if (juego.getPropietaria() == null) {
            ps.setNull(11, Types.INTEGER);  // el juego es de la asociación
        } else {
            ps.setInt(11, juego.getPropietaria().getId());
        }
    }

    /** Convierte la fila actual en un Juego. */
    public static Juego crearJuego(ResultSet rs) throws SQLException {
        Socia propietaria = null;
        int sociaId = rs.getInt("socia_id");
        if (!rs.wasNull()) {
            propietaria = new Socia(sociaId,
                    rs.getString("s_documento"),
                    rs.getString("s_nombre"),
                    rs.getString("s_apellidos"),
                    rs.getString("s_telefono"),
                    rs.getInt("s_edad"),
                    rs.getString("s_email"));
        }
        return new Juego(rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("autores"),
                rs.getString("descripcion"),
                rs.getInt("min_jugadores"),
                rs.getInt("max_jugadores"),
                rs.getInt("duracion"),
                rs.getString("tematica"),
                Juego.Tipo.valueOf(rs.getString("tipo")),
                rs.getString("ubicacion"),
                Juego.Estado.valueOf(rs.getString("estado")),
                propietaria,
                rs.getDate("fecha_alta").toLocalDate());
    }

    private Connection conexion() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
}