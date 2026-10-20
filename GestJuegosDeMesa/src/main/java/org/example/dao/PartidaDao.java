package org.example.dao;

import org.example.models.Jugadora;
import org.example.models.Partida;
import org.example.models.Socia;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class PartidaDao {
    private final JuegoDao juegoDao = new JuegoDao();

    /** Todas las partidas, de la más reciente a la más antigua, con su juego y sus jugadoras. */
    public List<Partida> listar() throws SQLException {
        return consultar("select * from partidas order by fecha desc, hora desc", 0);
    }

    /** Partidas en las que está inscrita una socia. */
    public List<Partida> listarPorSocia(int sociaId) throws SQLException {
        return consultar("select * from partidas where id in "
                + "(select partida_id from partida_jugadoras where socia_id = ?) order by fecha desc, hora desc", sociaId);
    }

    /** Partida con ese id, o null si no existe. */
    public Partida buscarPorId(int id) throws SQLException {
        List<Partida> partidas = consultar("select * from partidas where id = ?", id);
        return partidas.isEmpty() ? null : partidas.get(0);
    }

    /** Inserta la partida (sin jugadoras) y devuelve el id generado. */
    public int insertar(Partida partida) throws SQLException {
        String query = "insert into partidas (juego_id, fecha, hora, jugadoras_deseadas) values (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            rellenar(ps, partida);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    partida.setId(rs.getInt(1));
                }
            }
        }
        return partida.getId();
    }

    public void actualizar(Partida partida) throws SQLException {
        String query = "update partidas set juego_id=?, fecha=?, hora=?, jugadoras_deseadas=? where id=?";
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            rellenar(ps, partida);
            ps.setInt(5, partida.getId());
            ps.executeUpdate();
        }
    }

    /** Baja física: sus jugadoras se borran solas por el ON DELETE CASCADE. */
    public void borrar(int id) throws SQLException {
        try (PreparedStatement ps = conexion().prepareStatement("delete from partidas where id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ---------- jugadoras de una partida ----------

    public void anadirSocia(int partidaId, int sociaId) throws SQLException {
        try (PreparedStatement ps = conexion().prepareStatement(
                "insert into partida_jugadoras (partida_id, socia_id) values (?, ?)")) {
            ps.setInt(1, partidaId);
            ps.setInt(2, sociaId);
            ps.executeUpdate();
        }
    }

    public void anadirInvitada(int partidaId, String nombre) throws SQLException {
        try (PreparedStatement ps = conexion().prepareStatement(
                "insert into partida_jugadoras (partida_id, invitada) values (?, ?)")) {
            ps.setInt(1, partidaId);
            ps.setString(2, nombre);
            ps.executeUpdate();
        }
    }

    public void quitarJugadora(int jugadoraId) throws SQLException {
        try (PreparedStatement ps = conexion().prepareStatement("delete from partida_jugadoras where id = ?")) {
            ps.setInt(1, jugadoraId);
            ps.executeUpdate();
        }
    }

    public void marcarGanadora(int jugadoraId, boolean ganadora) throws SQLException {
        try (PreparedStatement ps = conexion().prepareStatement("update partida_jugadoras set ganadora = ? where id = ?")) {
            ps.setBoolean(1, ganadora);
            ps.setInt(2, jugadoraId);
            ps.executeUpdate();
        }
    }

    // ---------- privados ----------

    /**
     * Ejecuta una consulta de partidas y completa cada una con su juego y sus jugadoras.
     * Si parametro es distinto de 0, se pone en el único "?".
     */
    private List<Partida> consultar(String query, int parametro) throws SQLException {
        List<Partida> partidas = new ArrayList<>();
        List<Integer> juegoIds = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            if (parametro != 0) {
                ps.setInt(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    partidas.add(new Partida(rs.getInt("id"),
                            null,  // el juego se carga después
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime(),
                            rs.getInt("jugadoras_deseadas")));
                    juegoIds.add(rs.getInt("juego_id"));
                }
            }
        }
        // El juego y las jugadoras se cargan con otras consultas, ya cerrada la anterior
        for (int i = 0; i < partidas.size(); i++) {
            Partida partida = partidas.get(i);
            partida.setJuego(juegoDao.buscarPorIdConBajas(juegoIds.get(i)));
            partida.setJugadoras(listarJugadoras(partida.getId()));
        }
        return partidas;
    }

    /** Jugadoras de una partida. LEFT JOIN porque las invitadas no tienen socia. */
    private List<Jugadora> listarJugadoras(int partidaId) throws SQLException {
        String query = "select pj.id as pj_id, pj.socia_id, pj.invitada, pj.ganadora, s.documento, s.nombre, "
                + "s.apellidos, s.telefono, s.edad, s.email "
                + "from partida_jugadoras pj left join socias s on pj.socia_id = s.id "
                + "where pj.partida_id = ? order by pj.id";
        List<Jugadora> jugadoras = new ArrayList<>();
        try (PreparedStatement ps = conexion().prepareStatement(query)) {
            ps.setInt(1, partidaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Socia socia = null;
                    int sociaId = rs.getInt("socia_id");
                    if (!rs.wasNull()) {
                        socia = new Socia(sociaId, rs.getString("documento"), rs.getString("nombre"),
                                rs.getString("apellidos"), rs.getString("telefono"), rs.getInt("edad"),
                                rs.getString("email"));
                    }
                    jugadoras.add(new Jugadora(rs.getInt("pj_id"), socia, rs.getString("invitada"),
                            rs.getBoolean("ganadora")));
                }
            }
        }
        return jugadoras;
    }

    private void rellenar(PreparedStatement ps, Partida partida) throws SQLException {
        ps.setInt(1, partida.getJuego().getId());
        ps.setDate(2, Date.valueOf(partida.getFecha()));
        ps.setTime(3, Time.valueOf(partida.getHora()));
        ps.setInt(4, partida.getJugadorasDeseadas());
    }

    private Connection conexion() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
}
