package com.example.finanzaspersonalesnuevo.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finanzaspersonalesnuevo.Controller.TransaccionController;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;

import java.util.List;

public class TransaccionViewModel extends ViewModel {

    private final TransaccionController transaccionController;
    private final MutableLiveData<List<Transaccion>> transaccionesLiveData;

    public TransaccionViewModel() {
        transaccionController = new TransaccionController();
        transaccionesLiveData = new MutableLiveData<>();
        cargarTransacciones();
    }

    // Método para cargar las transacciones iniciales en LiveData
    private void cargarTransacciones() {
        transaccionesLiveData.setValue(transaccionController.obtenerTransacciones());
    }

    // Obtener LiveData de las transacciones
    public LiveData<List<Transaccion>> obtenerTransaccionesLiveData() {
        return transaccionesLiveData;
    }

    // Agregar una transacción y actualizar LiveData
    public void agregarTransaccion(Transaccion transaccion) {
        transaccionController.agregarTransaccion(transaccion);
        cargarTransacciones();
    }

    // Filtrar transacciones por categoría
    public void filtrarTransaccionesPorCategoria(String categoria) {
        List<Transaccion> filtradas = transaccionController.obtenerTransaccionesPorCategoria(categoria);
        transaccionesLiveData.setValue(filtradas);
    }
}
