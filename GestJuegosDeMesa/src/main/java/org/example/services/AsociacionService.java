package org.example.services;

import org.example.dao.AsociacionDao;
import org.example.models.Asociacion;
import org.example.utils.Lector;

import java.sql.SQLException;
import java.time.LocalDate;

public class AsociacionService {
    private final AsociacionDao asociacionDao = new AsociacionDao();
    private final Lector lector = new Lector();

    /** Línea corta con los datos principales, para la cabecera del menú principal. */
    public String cabecera() throws SQLException {
        Asociacion a = asociacionDao.obtener();
        if (a == null) {
            return "(No hay datos de la asociación)";
        }
        return a.getRazonSocial() + " | CIF " + a.getCif() + " | " + a.getDireccion();
    }

    public void mostrar() throws Exception {
        System.out.println(obtener());
    }

    public void modificar() throws Exception {
        Asociacion a = obtener();

        System.out.println("Razón social (actual: " + a.getRazonSocial() + "):");
        a.setRazonSocial(lector.leerTexto());
        System.out.println("CIF (actual: " + a.getCif() + "):");
        a.setCif(Validador.cif(lector.leerTexto(9)));
        System.out.println("Dirección (actual: " + a.getDireccion() + "):");
        a.setDireccion(lector.leerTexto(150));
        System.out.println("Teléfono (actual: " + a.getTelefono() + ", Enter para dejarlo vacío):");
        a.setTelefono(Validador.telefono(lector.leerTextoOpcional(15)));
        System.out.println("Email (actual: " + a.getEmail() + ", Enter para dejarlo vacío):");
        a.setEmail(Validador.email(lector.leerTextoOpcional(100)));
        System.out.println("Fecha de apertura dd/mm/aaaa (actual: " + a.getFechaApertura() + "):");
        LocalDate fecha = lector.leerFecha();
        if (fecha.isAfter(LocalDate.now())) {
            throw new Exception("La fecha de apertura no puede ser futura");
        }
        a.setFechaApertura(fecha);

        asociacionDao.actualizar(a);
        System.out.println("Datos de la asociación actualizados");
    }

    private Asociacion obtener() throws Exception {
        Asociacion a = asociacionDao.obtener();
        if (a == null) {
            throw new Exception("No hay datos de la asociación en la base de datos");
        }
        return a;
    }
}