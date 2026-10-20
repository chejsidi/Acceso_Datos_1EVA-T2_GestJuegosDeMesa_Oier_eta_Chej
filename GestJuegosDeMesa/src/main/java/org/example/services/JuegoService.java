package org.example.services;

import org.example.dao.JuegoDao;
import org.example.models.Juego;
import org.example.models.Socia;
import org.example.utils.Lector;

import java.time.LocalDate;
import java.util.List;

public class JuegoService {
    private final JuegoDao juegoDao = new JuegoDao();
    private final SociaService sociaService = new SociaService();
    private final Lector lector = new Lector();

    public void listar() throws Exception {
        mostrar(juegoDao.listar());
    }

    public void alta() throws Exception {
        Juego juego = new Juego(0, null, null, null, 0, 0, 0, null, null, null, null, null, LocalDate.now());
        pedirDatos(juego);
        int id = juegoDao.insertar(juego);
        System.out.println("Juego dado de alta con id " + id);
    }

    public void modificar() throws Exception {
        Juego juego = elegirJuego("¿Qué juego quieres modificar? (id)");
        pedirDatos(juego);
        juegoDao.actualizar(juego);
        System.out.println("Juego actualizado");
    }

    public void baja() throws Exception {
        Juego juego = elegirJuego("¿Qué juego quieres dar de baja? (id)");
        // no se da de baja un juego con partidas pendientes de jugar
        int partidasFuturas = juegoDao.contarPartidasFuturas(juego.getId());
        if (partidasFuturas > 0) {
            throw new Exception(juego.getNombre() + " tiene " + partidasFuturas
                    + " partida(s) pendiente(s). Bórralas antes de dar de baja el juego.");
        }
        System.out.println("¿Seguro que quieres dar de baja " + juego.getNombre() + "? (si/no)");
        if (!lector.leerSiNo()) {
            System.out.println("Operación cancelada");
            return;
        }
        juegoDao.darDeBaja(juego.getId());
        System.out.println("Juego dado de baja");
    }

    public void juegosDeSocia() throws Exception {
        Socia socia = sociaService.elegirSocia("¿De qué socia? (id)");
        System.out.println("Juegos de " + socia.getNombre() + " " + socia.getApellidos() + ":");
        mostrar(juegoDao.listarPorSocia(socia.getId()));
    }

    public Juego elegirJuego(String pregunta) throws Exception {
        List<Juego> juegos = juegoDao.listar();
        if (juegos.isEmpty()) {
            throw new Exception("No hay ningún juego");
        }
        mostrar(juegos);
        System.out.println(pregunta);
        int id = lector.leerEntero();
        Juego juego = juegoDao.buscarPorId(id);
        if (juego == null) {
            throw new Exception("No existe ningún juego activo con el id " + id);
        }
        return juego;
    }

    // ---------- privados ----------

    /** Pide todos los datos del juego (menos el id y la fecha de alta). Se usa en el alta y en la modificación. */
    private void pedirDatos(Juego juego) throws Exception {
        System.out.println("Nombre" + actual(juego.getNombre()) + ":");
        juego.setNombre(lector.leerTexto());
        System.out.println("Autor o autores" + actual(juego.getAutores()) + ":");
        juego.setAutores(lector.leerTexto(150));
        System.out.println("Descripción" + actual(juego.getDescripcion()) + " (Enter para dejarla vacía):");
        juego.setDescripcion(lector.leerTextoOpcional(500));
        System.out.println("Número mínimo de jugadoras" + actual(juego.getMinJugadores()) + ":");
        juego.setMinJugadores(lector.leerEntero(1, 20));
        System.out.println("Número máximo de jugadoras" + actual(juego.getMaxJugadores()) + ":");
        juego.setMaxJugadores(lector.leerEntero(juego.getMinJugadores(), 20));
        System.out.println("Duración estimada en minutos" + actual(juego.getDuracion()) + ":");
        juego.setDuracion(lector.leerEntero(1, 1440));
        System.out.println("Temática" + actual(juego.getTematica()) + ":");
        juego.setTematica(lector.leerTexto(50));

        System.out.println("Tipo" + actual(juego.getTipo()) + ": 1) Competitivo  2) Cooperativo");
        juego.setTipo(Juego.Tipo.values()[lector.leerEntero(1, 2) - 1]);

        System.out.println("Ubicación" + actual(juego.getUbicacion()) + ":");
        juego.setUbicacion(lector.leerTexto());

        System.out.println("Estado" + actual(juego.getEstado()) + ": 1) Disponible  2) Prestado  3) En reparación");
        juego.setEstado(Juego.Estado.values()[lector.leerEntero(1, 3) - 1]);

        System.out.println("¿El juego es de la asociación? (si/no)");
        if (lector.leerSiNo()) {
            juego.setPropietaria(null);
        } else {
            juego.setPropietaria(sociaService.elegirSocia("¿De qué socia es? (id)"));
        }
    }

    private void mostrar(List<Juego> juegos) {
        if (juegos.isEmpty()) {
            System.out.println("(No hay ningún juego)");
            return;
        }
        for (Juego j : juegos) {
            System.out.println(" " + j);
        }
    }

    private String actual(Object valor) {
        if (valor == null || valor.equals(0)) {
            return "";
        }
        return " (actual: " + valor + ")";
    }
}