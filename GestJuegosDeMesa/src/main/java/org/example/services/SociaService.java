package org.example.services;

import org.example.dao.SociaDao;
import org.example.models.Socia;
import org.example.utils.Lector;

import java.util.List;

public class SociaService {
    private final SociaDao sociaDao = new SociaDao();
    private final Lector lector = new Lector();

    public void listar() throws Exception {
        List<Socia> socias = sociaDao.listar();
        if (socias.isEmpty()) {
            System.out.println("(No hay ninguna socia)");
            return;
        }
        for (Socia s : socias) {
            System.out.println(" " + s);
        }
    }

    public void alta() throws Exception {
        System.out.println("DNI, NIE o pasaporte:");
        String documento = Validador.documento(lector.leerTexto(20));
        if (sociaDao.existeDocumento(documento, 0)) {
            throw new Exception("Ya existe una socia (activa o dada de baja) con el documento " + documento);
        }

        Socia socia = new Socia(0, documento, null, null, null, 0, null);
        pedirDatos(socia);

        int id = sociaDao.insertar(socia);
        System.out.println("Socia dada de alta con id " + id);
    }

    public void modificar() throws Exception {
        Socia socia = elegirSocia("¿Qué socia quieres modificar? (id)");

        System.out.println("DNI, NIE o pasaporte (actual: " + socia.getDocumento() + "):");
        String documento = Validador.documento(lector.leerTexto(20));
        if (sociaDao.existeDocumento(documento, socia.getId())) {
            throw new Exception("Ya existe otra socia con el documento " + documento);
        }
        socia.setDocumento(documento);
        pedirDatos(socia);

        sociaDao.actualizar(socia);
        System.out.println("Socia actualizada");
    }

    public void baja() throws Exception {
        Socia socia = elegirSocia("¿Qué socia quieres dar de baja? (id)");
        System.out.println("¿Seguro que quieres dar de baja a " + socia.getNombre() + " " + socia.getApellidos()
                + "? Se le quitará de las partidas que aún no se han jugado. (si/no)");
        if (!lector.leerSiNo()) {
            System.out.println("Operación cancelada");
            return;
        }
        sociaDao.darDeBaja(socia.getId());
        System.out.println("Socia dada de baja");
    }

    /** Muestra las socias y devuelve la elegida. Lo usan también juegos y partidas. */
    public Socia elegirSocia(String pregunta) throws Exception {
        List<Socia> socias = sociaDao.listar();
        if (socias.isEmpty()) {
            throw new Exception("No hay ninguna socia");
        }
        for (Socia s : socias) {
            System.out.println(" " + s);
        }
        System.out.println(pregunta);
        int id = lector.leerEntero();
        Socia socia = sociaDao.buscarPorId(id);
        if (socia == null) {
            throw new Exception("No existe ninguna socia activa con el id " + id);
        }
        return socia;
    }

    /** Pide el resto de datos (todo menos el documento). Se usa en el alta y en la modificación. */
    private void pedirDatos(Socia socia) throws Exception {
        System.out.println("Nombre" + actual(socia.getNombre()) + ":");
        socia.setNombre(lector.leerTexto(50));
        System.out.println("Apellidos" + actual(socia.getApellidos()) + ":");
        socia.setApellidos(lector.leerTexto(100));
        System.out.println("Edad" + (socia.getEdad() > 0 ? " (actual: " + socia.getEdad() + ")" : "") + ":");
        socia.setEdad(lector.leerEntero(1, 120));
        System.out.println("Teléfono" + actual(socia.getTelefono()) + " (Enter para dejarlo vacío):");
        socia.setTelefono(Validador.telefono(lector.leerTextoOpcional(15)));
        System.out.println("Email" + actual(socia.getEmail()) + " (Enter para dejarlo vacío):");
        socia.setEmail(Validador.email(lector.leerTextoOpcional(100)));
    }

    private String actual(String valor) {
        return valor == null ? "" : " (actual: " + valor + ")";
    }
}