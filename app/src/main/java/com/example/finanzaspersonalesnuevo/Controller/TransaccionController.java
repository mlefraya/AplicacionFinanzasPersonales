package com.example.finanzaspersonalesnuevo.Controller;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;

import java.util.ArrayList;
import java.util.List;

public class TransaccionController {

    private List<Transaccion> transacciones;

    public TransaccionController() {
        this.transacciones = new ArrayList<>();
    }

    // Agregar una nueva transacción
    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
    }

    // Obtener todas las transacciones
    public List<Transaccion> obtenerTransacciones() {
        return transacciones;
    }

    // Obtener transacciones por categoría
    public List<Transaccion> obtenerTransaccionesPorCategoria(String categoria) {
        List<Transaccion> transaccionesCategoria = new ArrayList<>();
        for (Transaccion t : transacciones) {
            if (t.getCategoria().equals(categoria)) {
                transaccionesCategoria.add(t);
            }
        }
        return transaccionesCategoria;
    }
}
