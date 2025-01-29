package com.example.finanzaspersonalesnuevo.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "transaccion") // Cambiado a "transaccion"
public class Transaccion {

    @PrimaryKey(autoGenerate = true)
    private int id; // ID para identificar cada transacción en la base de datos

    private String descripcion;
    private double cantidad;
    private Date fecha; // Room puede guardar fechas como long internamente
    private String categoria;

    // Constructor
    public Transaccion(String descripcion, double cantidad, Date fecha, String categoria) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
