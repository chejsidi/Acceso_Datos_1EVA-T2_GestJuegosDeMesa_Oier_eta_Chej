package org.example.models;

import java.time.LocalDate;

public class Juego {
    public enum Tipo { COMPETITIVO, COOPERATIVO }
    public enum Estado { DISPONIBLE, PRESTADO, EN_REPARACION }

    private int id;
    private String nombre;
    private String autores;
    private String descripcion;
    private int minJugadores;
    private int maxJugadores;
    private int duracion;  // en minutos
    private String tematica;
    private Tipo tipo;
    private String ubicacion;
    private Estado estado;
    private Socia propietaria;  // null = el juego es de la asociación
    private LocalDate fechaAlta;

    public Juego(int id, String nombre, String autores, String descripcion, int minJugadores, int maxJugadores,
                 int duracion, String tematica, Tipo tipo, String ubicacion, Estado estado, Socia propietaria,
                 LocalDate fechaAlta) {
        this.id = id;
        this.nombre = nombre;
        this.autores = autores;
        this.descripcion = descripcion;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.duracion = duracion;
        this.tematica = tematica;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.propietaria = propietaria;
        this.fechaAlta = fechaAlta;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getAutores() { return autores; }
    public void setAutores(String autores) { this.autores = autores; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getMinJugadores() { return minJugadores; }
    public void setMinJugadores(int minJugadores) { this.minJugadores = minJugadores; }

    public int getMaxJugadores() { return maxJugadores; }
    public void setMaxJugadores(int maxJugadores) { this.maxJugadores = maxJugadores; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public String getTematica() { return tematica; }
    public void setTematica(String tematica) { this.tematica = tematica; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public Socia getPropietaria() { return propietaria; }
    public void setPropietaria(Socia propietaria) { this.propietaria = propietaria; }

    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    @Override
    public String toString() {
        String duena = (propietaria == null) ? "Asociación" : propietaria.getNombre() + " " + propietaria.getApellidos();
        return "[" + id + "] " + nombre + " de " + autores + " | " + minJugadores + "-" + maxJugadores
                + " jugadoras | " + duracion + " min | " + tematica + " | " + tipo + " | " + ubicacion
                + " | " + estado + " | Propietaria: " + duena + " | Alta: " + fechaAlta;
    }
}
