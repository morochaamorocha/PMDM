package com.example.rals.ejer1bd;

/**
 * Created by rals1_000 on 27/12/2014.
 */
public class Contacto {

    private String nombre;
    private String apellido;

    public Contacto(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
