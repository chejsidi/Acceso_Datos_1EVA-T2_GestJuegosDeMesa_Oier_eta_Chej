package org.example.models;

public class Socia {
    private int id;
    private String documento;  // DNI, NIE o pasaporte
    private String nombre;
    private String apellidos;
    private String telefono;
    private int edad;
    private String email;

    public Socia(int id, String documento, String nombre, String apellidos, String telefono, int edad, String email) {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.edad = edad;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " " + apellidos + " | " + documento + " | " + edad + " años | Tel: "
                + (telefono == null ? "-" : telefono) + " | Email: " + (email == null ? "-" : email);
    }
}