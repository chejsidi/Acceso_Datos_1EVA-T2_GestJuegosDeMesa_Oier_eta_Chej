package org.example;

import org.example.services.AsociacionService;
import org.example.services.SociaService;
import org.example.utils.DatabaseConnection;
import org.example.utils.Lector;

import java.sql.SQLException;

public class Main {
    private static final Lector lector = new Lector();
    private static final AsociacionService asociacionService = new AsociacionService();
    private static final SociaService sociaService = new SociaService();

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
                case 2, 3, 5, 6 -> System.out.println("Pendiente de implementar");
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
                    "Baja de socia");
            switch (opcion) {
                case 1 -> ejecutar(sociaService::listar);
                case 2 -> ejecutar(sociaService::alta);
                case 3 -> ejecutar(sociaService::modificar);
                case 4 -> ejecutar(sociaService::baja);
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