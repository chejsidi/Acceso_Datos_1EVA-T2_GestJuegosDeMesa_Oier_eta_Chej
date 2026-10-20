package org.example;

import org.example.utils.DatabaseConnection;
import org.example.utils.Lector;

import java.sql.SQLException;

public class Main {
    private static final Lector lector = new Lector();

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
            opcion = elegirOpcion("Menú principal", "Salir",
                    "Gestionar socias",
                    "Gestionar juegos",
                    "Gestionar partidas",
                    "Datos de la asociación",
                    "Exportar datos (XML y JSON)",
                    "Metadatos de la base de datos");
            switch (opcion) {
                case 1, 2, 3, 4, 5, 6 -> System.out.println("Pendiente de implementar");
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida");
            }
        } while (opcion != 0);
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