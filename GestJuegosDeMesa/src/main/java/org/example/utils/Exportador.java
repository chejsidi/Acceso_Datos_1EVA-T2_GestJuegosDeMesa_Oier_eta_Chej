package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.thoughtworks.xstream.XStream;
import org.example.models.Juego;
import org.example.models.Jugadora;
import org.example.models.Partida;
import org.example.models.Socia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Exporta listas de objetos a ficheros XML (con XStream) y JSON (con Gson) en la carpeta exportaciones. */
public class Exportador {
    private static final String CARPETA = "exportaciones";

    // Gson no sabe convertir LocalDate y LocalTime: se le indica que los escriba como texto (2025-10-04, 18:00)
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class,
                    (JsonSerializer<LocalDate>) (fecha, tipo, contexto) -> new JsonPrimitive(fecha.toString()))
            .registerTypeAdapter(LocalTime.class,
                    (JsonSerializer<LocalTime>) (hora, tipo, contexto) -> new JsonPrimitive(hora.toString()))
            .create();

    /** Crea exportaciones/[nombre].xml. etiquetaLista es la etiqueta raíz (por ejemplo "socias"). */
    public String exportarXml(List<?> lista, String etiquetaLista, String nombre) throws IOException {
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);  // repite los datos en vez de usar referencias internas
        // List.class y no ArrayList.class: XStream trata ArrayList como "la List por defecto"
        xstream.alias(etiquetaLista, List.class);
        xstream.alias("socia", Socia.class);
        xstream.alias("juego", Juego.class);
        xstream.alias("partida", Partida.class);
        xstream.alias("jugadora", Jugadora.class);

        File fichero = crearFichero(nombre + ".xml");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(fichero), StandardCharsets.UTF_8)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xstream.toXML(new ArrayList<>(lista), writer);
        }
        return fichero.getPath();
    }

    /** Crea exportaciones/[nombre].json */
    public String exportarJson(Object datos, String nombre) throws IOException {
        File fichero = crearFichero(nombre + ".json");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(fichero), StandardCharsets.UTF_8)) {
            gson.toJson(datos, writer);
        }
        return fichero.getPath();
    }

    private File crearFichero(String nombreFichero) throws IOException {
        File carpeta = new File(CARPETA);
        if (!carpeta.exists() && !carpeta.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta " + CARPETA);
        }
        return new File(carpeta, nombreFichero);
    }
}
