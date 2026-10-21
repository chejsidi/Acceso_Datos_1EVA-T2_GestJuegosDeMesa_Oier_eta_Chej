package org.example.models;

/** Metadatos de una columna de una tabla. */
public class InfoColumna {
    private String nombre;
    private String tipo;
    private int tamano;
    private boolean nula;           // si puede ser NULL
    private boolean autoincremental;
    private String valorPorDefecto;

    public InfoColumna(String nombre, String tipo, int tamano, boolean nula, boolean autoincremental, String valorPorDefecto) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tamano = tamano;
        this.nula = nula;
        this.autoincremental = autoincremental;
        this.valorPorDefecto = valorPorDefecto;
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getTamano() { return tamano; }
    public boolean isNula() { return nula; }
    public boolean isAutoincremental() { return autoincremental; }
    public String getValorPorDefecto() { return valorPorDefecto; }

    @Override
    public String toString() {
        return nombre + " " + tipo + "(" + tamano + ")" + (nula ? " NULL" : " NOT NULL")
                + (autoincremental ? " AUTO_INCREMENT" : "")
                + (valorPorDefecto != null ? " DEFAULT " + valorPorDefecto : "");
    }
}
