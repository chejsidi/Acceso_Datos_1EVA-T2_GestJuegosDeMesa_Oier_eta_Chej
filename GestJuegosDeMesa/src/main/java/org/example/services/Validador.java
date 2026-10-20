package org.example.services;

/**
 * Comprobaciones de formato que se repiten en varios services.
 * Si el dato no es válido lanzan una excepción con un mensaje para el usuario.
 */
public class Validador {

    /** DNI, NIE o pasaporte: entre 5 y 20 letras o números. Se guarda en mayúsculas. */
    public static String documento(String documento) throws Exception {
        String doc = documento.toUpperCase();
        if (!doc.matches("[A-Z0-9]{5,20}")) {
            throw new Exception("El documento solo puede tener letras y números (entre 5 y 20)");
        }
        return doc;
    }

    /** CIF: una letra seguida de 8 caracteres (números o letra final). */
    public static String cif(String cif) throws Exception {
        String c = cif.toUpperCase();
        if (!c.matches("[A-Z][0-9]{7}[0-9A-Z]")) {
            throw new Exception("El CIF debe tener una letra y 8 caracteres más (ejemplo: G01234567)");
        }
        return c;
    }

    /** Teléfono opcional: null o entre 9 y 15 dígitos (puede empezar por +). */
    public static String telefono(String telefono) throws Exception {
        if (telefono != null && !telefono.matches("\\+?[0-9]{9,15}")) {
            throw new Exception("El teléfono debe tener entre 9 y 15 dígitos");
        }
        return telefono;
    }

    /** Email opcional: null o con formato algo@algo.algo */
    public static String email(String email) throws Exception {
        if (email != null && !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new Exception("El email no tiene un formato válido");
        }
        return email;
    }
}