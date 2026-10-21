package org.example;

import org.example.services.AsociacionService;
import org.example.services.ExportacionService;
import org.example.services.JuegoService;
import org.example.services.PartidaService;
import org.example.services.SociaService;
import org.example.utils.DatabaseConnection;
import org.example.utils.Lector;

import java.sql.SQLException;

public class Main {
    private static final Lector lector = new Lector();
    private static final AsociacionService asociacionService = new AsociacionService();
    private static final SociaService sociaService = new SociaService();
    private static final JuegoService juegoService = new JuegoService();
    private static final PartidaService partidaService = new PartidaService();
    private static final ExportacionService exportacionService = new ExportacionService();

    /** Una opción de menú que puede fallar. */
    private interface Accion {
        void ejecutar() throws Exception;
    }

    public static void main(String[] args) {
        System.out.println("=== Gestión de la asociación de juegos de mesa ===");
        try {
            DatabaseConnection.getInstance();
            System.out.println("Conectado a la base de datos");
        } catch (SQLException e) {
            System.out.println("ERROR: no se pudo conectar a la base de datos (" + e.getMessage() + ")");
            System.out.println("Comprueba que el contenedor de MariaDB está arrancado: docker compose up -d");
            return;
        }

        int opcion;
        do {
            ejecutar(() -> System.out.println("\n" + asociacionService.cabecera()));
            opcion = elegirOpcion("Menú principal", "Salir",
                    "Gestionar socias",
                    "Gestionar juegos",
                    "Gestionar partidas",
                    "Datos de la asociación",
                    "Exportar datos (XML y JSON)",
                    "Metadatos de la base de datos");
            switch (opcion) {
                case 1 -> menuSocias();
                case 2 -> menuJuegos();
                case 3 -> menuPartidas();
                case 5 -> menuExportar();
                case 6 -> System.out.println("Pendiente de implementar");
                case 4 -> menuAsociacion();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    // =====================================================
    //  MENÚS
    // =====================================================

    private static void menuSocias() {
        int opcion;
        do {
            opcion = elegirOpcion("Socias", "Volver",
                    "Listar socias",
                    "Alta de socia",
                    "Modificar socia",
                    "Baja de socia",
                    "Ver juegos de una socia",
                    "Ver partidas de una socia");
            switch (opcion) {
                case 1 -> ejecutar(sociaService::listar);
                case 2 -> ejecutar(sociaService::alta);
                case 3 -> ejecutar(sociaService::modificar);
                case 4 -> ejecutar(sociaService::baja);
                case 5 -> ejecutar(juegoService::juegosDeSocia);
                case 6 -> ejecutar(partidaService::partidasDeSocia);
                case 0 -> { }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void menuJuegos() {
        int opcion;
        do {
            opcion = elegirOpcion("Juegos", "Volver",
                    "Listar juegos",
                    "Alta de juego",
                    "Modificar juego",
                    "Baja de juego");
            switch (opcion) {
                case 1 -> ejecutar(juegoService::listar);
                case 2 -> ejecutar(juegoService::alta);
                case 3 -> ejecutar(juegoService::modificar);
                case 4 -> ejecutar(juegoService::baja);
                case 0 -> { }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void menuPartidas() {
        int opcion;
        do {
            opcion = elegirOpcion("Partidas", "Volver",
                    "Listar partidas e inscritas",
                    "Alta de partida",
                    "Modificar partida",
                    "Borrar partida",
                    "Apuntarse a una partida",
                    "Quitar una jugadora de una partida",
                    "Registrar ganadoras");
            switch (opcion) {
                case 1 -> ejecutar(partidaService::listar);
                case 2 -> ejecutar(partidaService::alta);
                case 3 -> ejecutar(partidaService::modificar);
                case 4 -> ejecutar(partidaService::baja);
                case 5 -> ejecutar(partidaService::apuntarse);
                case 6 -> ejecutar(partidaService::quitarJugadora);
                case 7 -> ejecutar(partidaService::registrarGanadoras);
                case 0 -> { }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void menuExportar() {
        int opcion;
        do {
            opcion = elegirOpcion("Exportar datos a XML y JSON", "Volver",
                    "Exportar socias",
                    "Exportar juegos",
                    "Exportar partidas",
                    "Exportar todo");
            switch (opcion) {
                case 1 -> ejecutar(exportacionService::exportarSocias);
                case 2 -> ejecutar(exportacionService::exportarJuegos);
                case 3 -> ejecutar(exportacionService::exportarPartidas);
                case 4 -> ejecutar(exportacionService::exportarTodo);
                case 0 -> { }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    private static void menuAsociacion() {
        int opcion;
        do {
            opcion = elegirOpcion("Datos de la asociación", "Volver",
                    "Ver datos",
                    "Modificar datos");
            switch (opcion) {
                case 1 -> ejecutar(asociacionService::mostrar);
                case 2 -> ejecutar(asociacionService::modificar);
                case 0 -> { }
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
    }

    // =====================================================
    //  UTILIDADES
    // =====================================================

    /** Ejecuta una opción y, si falla, muestra el error y vuelve al menú sin cerrar el programa. */
    private static void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (SQLException e) {
            System.out.println("ERROR de base de datos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /** Muestra un menú numerado y devuelve la opción elegida. */
    private static int elegirOpcion(String titulo, String textoSalir, String... opciones) {
        System.out.println("\n--- " + titulo + " ---");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println(" " + (i + 1) + ") " + opciones[i]);
        }
        System.out.println(" 0) " + textoSalir);
        System.out.println("Elige una opción:");
        return lector.leerEntero();
    }
}
