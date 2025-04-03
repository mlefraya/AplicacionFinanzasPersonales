package com.example.finanzaspersonalesnuevo.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.finanzaspersonalesnuevo.data.DateConverter;

import java.util.Date;

@Entity(tableName = "transaccion")
public class Transaccion {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String descripcion;
    private double cantidad;

    @TypeConverters(DateConverter.class)  // Convierto el tipo Date para Room
    private Date fecha;

    private String categoria;
    private String tipo; // "Ingreso" o "Gasto"

    // Constructor sin ID (lo genera automáticamente Room)
    public Transaccion(String descripcion, double cantidad, Date fecha, String categoria, String tipo) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
        this.tipo = tipo;
    }

    // Constructor vacío (requerido por Room)
    public Transaccion() {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
