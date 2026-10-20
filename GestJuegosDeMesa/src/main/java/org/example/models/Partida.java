package org.example.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Partida {
    private int id;
    private Juego juego;
    private LocalDate fecha;
    private LocalTime hora;
    private int jugadorasDeseadas;  // dentro del rango del juego
    private List<Jugadora> jugadoras = new ArrayList<>();

    public Partida(int id, Juego juego, LocalDate fecha, LocalTime hora, int jugadorasDeseadas) {
        this.id = id;
        this.juego = juego;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadorasDeseadas = jugadorasDeseadas;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Juego getJuego() { return juego; }
    public void setJuego(Juego juego) { this.juego = juego; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public int getJugadorasDeseadas() { return jugadorasDeseadas; }
    public void setJugadorasDeseadas(int jugadorasDeseadas) { this.jugadorasDeseadas = jugadorasDeseadas; }

    public List<Jugadora> getJugadoras() { return jugadoras; }
    public void setJugadoras(List<Jugadora> jugadoras) { this.jugadoras = jugadoras; }

    /** Las plazas libres no se guardan en la BBDD: se calculan para que nunca estén desactualizadas. */
    public int getPlazasLibres() {
        return jugadorasDeseadas - jugadoras.size();
    }

    @Override
    public String toString() {
        return "[" + id + "] " + juego.getNombre() + " | " + fecha + " " + hora + " | "
                + jugadoras.size() + "/" + jugadorasDeseadas + " jugadoras | " + getPlazasLibres() + " plazas libres";
    }
}
