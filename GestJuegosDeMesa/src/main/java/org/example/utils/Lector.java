package org.example.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Lector {
    // static para que todos compartan todo
    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private String leerLinea() {
        // Si se cierra la entrada nextLine() lanzaría una excepción no controlada
        if (!sc.hasNextLine()) {
            System.out.println("Entrada cerrada. ¡Hasta luego!");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    public int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(leerLinea());
            } catch (NumberFormatException e) {
                System.out.println("Número entero no válido, inténtelo de nuevo: ");
            }
        }
    }

    public int leerEntero(int min, int max) {
        while (true) {
            int numero = leerEntero();
            if (numero >= min && numero <= max) {
                return numero;
            }
            System.out.println("El número debe estar entre " + min + " y " + max + ", inténtelo de nuevo: ");
        }
    }

    /** Texto obligatorio de hasta 100 caracteres. */
    public String leerTexto() {
        return leerTexto(100);
    }

    public String leerTexto(int maxLongitud) {
        while (true) {
            String linea = leerLinea();
            if (linea.isEmpty()) {
                System.out.println("El texto no puede estar vacío, inténtelo de nuevo: ");
            } else if (linea.length() > maxLongitud) {
                System.out.println("Máximo " + maxLongitud + " caracteres, inténtelo de nuevo: ");
            } else {
                return linea;
            }
        }
    }

    /** Texto que se puede dejar vacío (devuelve null). */
    public String leerTextoOpcional(int maxLongitud) {
        while (true) {
            String linea = leerLinea();
            if (linea.isEmpty()) {
                return null;
            } else if (linea.length() > maxLongitud) {
                System.out.println("Máximo " + maxLongitud + " caracteres, inténtelo de nuevo: ");
            } else {
                return linea;
            }
        }
    }

    public boolean leerSiNo() {
        while (true) {
            String resp = leerLinea();
            if (resp.equalsIgnoreCase("si")) return true;
            if (resp.equalsIgnoreCase("no")) return false;
            System.out.println("Escribe una respuesta válida (si/no): ");
        }
    }

    /** Fecha con formato dd/MM/yyyy. */
    public LocalDate leerFecha() {
        while (true) {
            try {
                return LocalDate.parse(leerLinea(), FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida, usa el formato dd/mm/aaaa: ");
            }
        }
    }

    /** Hora con formato HH:mm. */
    public LocalTime leerHora() {
        while (true) {
            try {
                return LocalTime.parse(leerLinea(), FORMATO_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("Hora no válida, usa el formato hh:mm: ");
            }
        }
    }
}