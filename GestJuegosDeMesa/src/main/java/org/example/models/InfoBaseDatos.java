package org.example.models;

import java.util.ArrayList;
import java.util.List;

/** Metadatos de la base de datos y de sus tablas. */
public class InfoBaseDatos {
    private String nombre;
    private String sgbd;
    private String versionSgbd;
    private String driver;
    private String versionDriver;
    private String url;
    private String usuario;
    private List<InfoTabla> tablas = new ArrayList<>();

    public InfoBaseDatos(String nombre, String sgbd, String versionSgbd, String driver, String versionDriver,
                         String url, String usuario) {
        this.nombre = nombre;
        this.sgbd = sgbd;
        this.versionSgbd = versionSgbd;
        this.driver = driver;
        this.versionDriver = versionDriver;
        this.url = url;
        this.usuario = usuario;
    }

    public String getNombre() { return nombre; }
    public String getSgbd() { return sgbd; }
    public String getVersionSgbd() { return versionSgbd; }
    public String getDriver() { return driver; }
    public String getVersionDriver() { return versionDriver; }
    public String getUrl() { return url; }
    public String getUsuario() { return usuario; }
    public List<InfoTabla> getTablas() { return tablas; }

    @Override
    public String toString() {
        return "Base de datos: " + nombre + "\n" +
                "SGBD: " + sgbd + " " + versionSgbd + "\n" +
                "Driver: " + driver + " " + versionDriver + "\n" +
                "URL: " + url + "\n" +
                "Usuario: " + usuario;
    }
}
