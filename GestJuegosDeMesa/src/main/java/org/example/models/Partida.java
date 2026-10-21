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
    // No se guarda en la BBDD: se recalcula cada vez que cambian las jugadoras o las deseadas.
    // Es un atributo (y no solo un método) para que salga también en los ficheros XML y JSON.
    private int plazasLibres;

    public Partida(int id, Juego juego, LocalDate fecha, LocalTime hora, int jugadorasDeseadas) {
        this.id = id;
        this.juego = juego;
        this.fecha = fecha;
        this.hora = hora;
        this.jugadorasDeseadas = jugadorasDeseadas;
        recalcularPlazasLibres();
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
    public void setJugadorasDeseadas(int jugadorasDeseadas) {
        this.jugadorasDeseadas = jugadorasDeseadas;
        recalcularPlazasLibres();
    }

    public List<Jugadora> getJugadoras() { return jugadoras; }
    public void setJugadoras(List<Jugadora> jugadoras) {
        this.jugadoras = jugadoras;
        recalcularPlazasLibres();
    }

    public int getPlazasLibres() { return plazasLibres; }

    private void recalcularPlazasLibres() {
        plazasLibres = jugadorasDeseadas - jugadoras.size();
    }

    @Override
    public String toString() {
        return "[" + id + "] " + juego.getNombre() + " | " + fecha + " " + hora + " | "
                + jugadoras.size() + "/" + jugadorasDeseadas + " jugadoras | " + getPlazasLibres() + " plazas libres";
    }
}
