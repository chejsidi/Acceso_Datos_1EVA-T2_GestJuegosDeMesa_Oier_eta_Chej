package org.example.models;

/** Una jugadora de una partida: una socia o una invitada (de la que solo se guarda el nombre). */
public class Jugadora {
    private int id;
    private Socia socia;      // null si es invitada
    private String invitada;  // null si es socia
    private boolean ganadora;

    public Jugadora(int id, Socia socia, String invitada, boolean ganadora) {
        this.id = id;
        this.socia = socia;
        this.invitada = invitada;
        this.ganadora = ganadora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Socia getSocia() { return socia; }
    public void setSocia(Socia socia) { this.socia = socia; }

    public String getInvitada() { return invitada; }
    public void setInvitada(String invitada) { this.invitada = invitada; }

    public boolean isGanadora() { return ganadora; }
    public void setGanadora(boolean ganadora) { this.ganadora = ganadora; }

    public String getNombreCompleto() {
        return (socia != null) ? socia.getNombre() + " " + socia.getApellidos() : invitada + " (invitada)";
    }

    @Override
    public String toString() {
        return getNombreCompleto() + (ganadora ? " - GANADORA" : "");
    }
}
