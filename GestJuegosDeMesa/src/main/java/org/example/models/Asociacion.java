package org.example.models;

import java.time.LocalDate;

public class Asociacion {
    private String razonSocial;
    private String cif;
    private String direccion;
    private String telefono;
    private String email;
    private LocalDate fechaApertura;

    public Asociacion(String razonSocial, String cif, String direccion, String telefono, String email, LocalDate fechaApertura) {
        this.razonSocial = razonSocial;
        this.cif = cif;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaApertura = fechaApertura;
    }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getCif() { return cif; }
    public void setCif(String cif) { this.cif = cif; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDate fechaApertura) { this.fechaApertura = fechaApertura; }

    @Override
    public String toString() {
        return razonSocial + " (CIF " + cif + ")\n" +
                direccion + "\n" +
                "Tel: " + (telefono == null ? "-" : telefono) + " | Email: " + (email == null ? "-" : email) + "\n" +
                "Abierta desde: " + fechaApertura;
    }
}