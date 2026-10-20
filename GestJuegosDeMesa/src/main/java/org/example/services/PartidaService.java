package org.example.services;

import org.example.dao.PartidaDao;
import org.example.models.Juego;
import org.example.models.Jugadora;
import org.example.models.Partida;
import org.example.models.Socia;
import org.example.utils.Lector;

import java.time.LocalDate;
import java.util.List;

public class PartidaService {
    private final PartidaDao partidaDao = new PartidaDao();
    private final JuegoService juegoService = new JuegoService();
    private final SociaService sociaService = new SociaService();
    private final Lector lector = new Lector();

    /** Listado de partidas con las personas inscritas en cada una. */
    public void listar() throws Exception {
        List<Partida> partidas = partidaDao.listar();
        if (partidas.isEmpty()) {
            System.out.println("(No hay ninguna partida)");
            return;
        }
        for (Partida p : partidas) {
            mostrarConJugadoras(p);
        }
    }

    public void alta() throws Exception {
        Juego juego = juegoService.elegirJuego("¿De qué juego es la partida? (id)");
        comprobarDisponible(juego);

        System.out.println("Fecha (dd/mm/aaaa):");
        LocalDate fecha = lector.leerFecha();
        System.out.println("Hora (hh:mm):");
        Partida partida = new Partida(0, juego, fecha, lector.leerHora(), 0);
        System.out.println("Número de jugadoras deseadas (entre " + juego.getMinJugadores() + " y "
                + juego.getMaxJugadores() + "):");
        partida.setJugadorasDeseadas(lector.leerEntero(juego.getMinJugadores(), juego.getMaxJugadores()));

        int id = partidaDao.insertar(partida);
        System.out.println("Partida creada con id " + id + ". Usa \"Apuntarse a una partida\" para añadir jugadoras.");
    }

    public void modificar() throws Exception {
        Partida partida = elegirPartida("¿Qué partida quieres modificar? (id)");

        System.out.println("Juego actual: " + partida.getJuego().getNombre() + ". ¿Quieres cambiarlo? (si/no)");
        if (lector.leerSiNo()) {
            Juego juego = juegoService.elegirJuego("¿Qué juego? (id)");
            comprobarDisponible(juego);
            partida.setJuego(juego);
        }
        System.out.println("Fecha dd/mm/aaaa (actual: " + partida.getFecha() + "):");
        partida.setFecha(lector.leerFecha());
        System.out.println("Hora hh:mm (actual: " + partida.getHora() + "):");
        partida.setHora(lector.leerHora());

        // No puede haber menos plazas que jugadoras ya inscritas, ni salirse del rango del juego
        Juego juego = partida.getJuego();
        int minimo = Math.max(juego.getMinJugadores(), partida.getJugadoras().size());
        if (minimo > juego.getMaxJugadores()) {
            throw new Exception("Hay " + partida.getJugadoras().size() + " jugadoras inscritas y "
                    + juego.getNombre() + " admite como máximo " + juego.getMaxJugadores());
        }
        System.out.println("Jugadoras deseadas (actual: " + partida.getJugadorasDeseadas() + ", entre " + minimo
                + " y " + juego.getMaxJugadores() + "):");
        partida.setJugadorasDeseadas(lector.leerEntero(minimo, juego.getMaxJugadores()));

        partidaDao.actualizar(partida);
        System.out.println("Partida actualizada");
    }

    public void baja() throws Exception {
        Partida partida = elegirPartida("¿Qué partida quieres borrar? (id)");
        System.out.println("¿Seguro que quieres borrar la partida de " + partida.getJuego().getNombre() + " del "
                + partida.getFecha() + "? Se borrarán también sus " + partida.getJugadoras().size()
                + " inscripciones. (si/no)");
        if (!lector.leerSiNo()) {
            System.out.println("Operación cancelada");
            return;
        }
        partidaDao.borrar(partida.getId());
        System.out.println("Partida borrada");
    }

    /** Apunta a una socia o a una invitada en una partida que aún no se ha jugado y tiene plazas. */
    public void apuntarse() throws Exception {
        Partida partida = elegirPartida("¿A qué partida? (id)");
        if (partida.getFecha().isBefore(LocalDate.now())) {
            throw new Exception("Esa partida ya se ha jugado");
        }
        if (partida.getPlazasLibres() <= 0) {
            throw new Exception("La partida no tiene plazas libres");
        }

        System.out.println("¿Es una socia? (si/no)");
        if (lector.leerSiNo()) {
            Socia socia = sociaService.elegirSocia("¿Qué socia? (id)");
            for (Jugadora j : partida.getJugadoras()) {
                if (j.getSocia() != null && j.getSocia().getId() == socia.getId()) {
                    throw new Exception(socia.getNombre() + " ya está apuntada a esta partida");
                }
            }
            partidaDao.anadirSocia(partida.getId(), socia.getId());
            System.out.println(socia.getNombre() + " apuntada a la partida");
        } else {
            System.out.println("Nombre y apellidos de la invitada:");
            String nombre = lector.leerTexto();
            partidaDao.anadirInvitada(partida.getId(), nombre);
            System.out.println(nombre + " (invitada) apuntada a la partida");
        }
    }

    public void quitarJugadora() throws Exception {
        Partida partida = elegirPartida("¿De qué partida? (id)");
        Jugadora jugadora = elegirJugadora(partida, "¿Qué jugadora quieres quitar? (número)");
        partidaDao.quitarJugadora(jugadora.getId());
        System.out.println(jugadora.getNombreCompleto() + " quitada de la partida");
    }

    /** Pregunta por cada jugadora si ha ganado. Solo para partidas ya jugadas (o de hoy). */
    public void registrarGanadoras() throws Exception {
        Partida partida = elegirPartida("¿De qué partida? (id)");
        if (partida.getFecha().isAfter(LocalDate.now())) {
            throw new Exception("La partida todavía no se ha jugado");
        }
        if (partida.getJugadoras().isEmpty()) {
            throw new Exception("La partida no tiene jugadoras");
        }
        for (Jugadora j : partida.getJugadoras()) {
            System.out.println("¿Ha ganado " + j.getNombreCompleto() + "? (si/no)");
            partidaDao.marcarGanadora(j.getId(), lector.leerSiNo());
        }
        System.out.println("Ganadoras registradas");
    }

    /** Opción del menú de socias: partidas en las que está inscrita, con día y hora. */
    public void partidasDeSocia() throws Exception {
        Socia socia = sociaService.elegirSocia("¿De qué socia? (id)");
        List<Partida> partidas = partidaDao.listarPorSocia(socia.getId());
        System.out.println("Partidas de " + socia.getNombre() + " " + socia.getApellidos() + ":");
        if (partidas.isEmpty()) {
            System.out.println("(No está inscrita en ninguna partida)");
            return;
        }
        for (Partida p : partidas) {
            System.out.println(" " + p);
        }
    }

    // ---------- privados ----------

    private Partida elegirPartida(String pregunta) throws Exception {
        List<Partida> partidas = partidaDao.listar();
        if (partidas.isEmpty()) {
            throw new Exception("No hay ninguna partida");
        }
        for (Partida p : partidas) {
            System.out.println(" " + p);
        }
        System.out.println(pregunta);
        int id = lector.leerEntero();
        Partida partida = partidaDao.buscarPorId(id);
        if (partida == null) {
            throw new Exception("No existe ninguna partida con el id " + id);
        }
        return partida;
    }

    private Jugadora elegirJugadora(Partida partida, String pregunta) throws Exception {
        List<Jugadora> jugadoras = partida.getJugadoras();
        if (jugadoras.isEmpty()) {
            throw new Exception("La partida no tiene jugadoras");
        }
        for (int i = 0; i < jugadoras.size(); i++) {
            System.out.println(" " + (i + 1) + ") " + jugadoras.get(i));
        }
        System.out.println(pregunta);
        return jugadoras.get(lector.leerEntero(1, jugadoras.size()) - 1);
    }

    /** Solo se pueden programar partidas con juegos disponibles (no prestados ni en reparación). */
    private void comprobarDisponible(Juego juego) throws Exception {
        if (juego.getEstado() != Juego.Estado.DISPONIBLE) {
            throw new Exception(juego.getNombre() + " no está disponible (estado: " + juego.getEstado() + ")");
        }
    }

    private void mostrarConJugadoras(Partida partida) {
        System.out.println(" " + partida);
        if (partida.getJugadoras().isEmpty()) {
            System.out.println("     (sin jugadoras inscritas)");
        }
        for (Jugadora j : partida.getJugadoras()) {
            System.out.println("     - " + j);
        }
    }
}
