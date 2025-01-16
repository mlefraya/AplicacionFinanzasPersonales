package com.example.finanzaspersonalesnuevo.Model;


public class Categoria {
    private String nombre;
    private double presupuesto;
    private String descripcion;

    // Constructor vacío
    public Categoria() {}

    // Constructor con parámetros
    public Categoria(String nombre, double presupuesto, String descripcion) {
        this.nombre = nombre;
        this.presupuesto = presupuesto;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
