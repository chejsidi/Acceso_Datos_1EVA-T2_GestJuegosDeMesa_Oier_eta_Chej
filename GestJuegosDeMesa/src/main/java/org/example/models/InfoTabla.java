package org.example.models;

import java.util.ArrayList;
import java.util.List;

/** Metadatos de una tabla: esquema, nombre, clave primaria y columnas. */
public class InfoTabla {
    private String esquema;
    private String nombre;
    private List<String> clavePrimaria = new ArrayList<>();
    private List<InfoColumna> columnas = new ArrayList<>();

    public InfoTabla(String esquema, String nombre) {
        this.esquema = esquema;
        this.nombre = nombre;
    }

    public String getEsquema() { return esquema; }
    public String getNombre() { return nombre; }
    public List<String> getClavePrimaria() { return clavePrimaria; }
    public List<InfoColumna> getColumnas() { return columnas; }

    @Override
    public String toString() {
        return esquema + "." + nombre + " | Clave primaria: " + String.join(", ", clavePrimaria);
    }
}
