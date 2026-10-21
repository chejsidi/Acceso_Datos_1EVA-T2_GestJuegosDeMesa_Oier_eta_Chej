package org.example.services;

import org.example.dao.JuegoDao;
import org.example.dao.PartidaDao;
import org.example.dao.SociaDao;
import org.example.utils.Exportador;

import java.util.List;

public class ExportacionService {
    private final SociaDao sociaDao = new SociaDao();
    private final JuegoDao juegoDao = new JuegoDao();
    private final PartidaDao partidaDao = new PartidaDao();
    private final Exportador exportador = new Exportador();

    public void exportarSocias() throws Exception {
        exportar(sociaDao.listar(), "socias");
    }

    public void exportarJuegos() throws Exception {
        exportar(juegoDao.listar(), "juegos");
    }

    public void exportarPartidas() throws Exception {
        exportar(partidaDao.listar(), "partidas");
    }

    public void exportarTodo() throws Exception {
        exportarSocias();
        exportarJuegos();
        exportarPartidas();
    }

    /** Exporta la lista a XML y a JSON con el mismo nombre de fichero. */
    private void exportar(List<?> lista, String nombre) throws Exception {
        String xml = exportador.exportarXml(lista, nombre, nombre);
        String json = exportador.exportarJson(lista, nombre);
        System.out.println("Exportados " + lista.size() + " registros de " + nombre + " a " + xml + " y " + json);
    }
}
