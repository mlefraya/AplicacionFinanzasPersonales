package com.example.finanzaspersonalesnuevo.Model;

import java.util.Date;

public class Transaccion {
    private String descripcion;
    private double cantidad;
    private Date fecha;
    private String categoria;

    // Constructor
    public Transaccion(String descripcion, double cantidad, Date fecha, String categoria) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
