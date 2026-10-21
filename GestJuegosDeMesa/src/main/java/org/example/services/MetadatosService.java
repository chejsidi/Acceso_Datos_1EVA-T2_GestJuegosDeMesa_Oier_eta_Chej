package org.example.services;

import org.example.dao.MetadatosDao;
import org.example.models.InfoBaseDatos;
import org.example.models.InfoColumna;
import org.example.models.InfoTabla;
import org.example.utils.Exportador;

public class MetadatosService {
    private final MetadatosDao metadatosDao = new MetadatosDao();
    private final Exportador exportador = new Exportador();

    public void mostrar() throws Exception {
        InfoBaseDatos info = metadatosDao.obtener();
        System.out.println(info);
        System.out.println("\nTablas (" + info.getTablas().size() + "):");
        for (InfoTabla tabla : info.getTablas()) {
            System.out.println(" " + tabla);
            for (InfoColumna columna : tabla.getColumnas()) {
                System.out.println("     - " + columna);
            }
        }
    }

    public void exportar() throws Exception {
        InfoBaseDatos info = metadatosDao.obtener();
        String xml = exportador.exportarXml(info, "metadatos");
        String json = exportador.exportarJson(info, "metadatos");
        System.out.println("Metadatos exportados a " + xml + " y " + json);
    }
}
