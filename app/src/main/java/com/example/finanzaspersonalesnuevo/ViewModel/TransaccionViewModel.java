package com.example.finanzaspersonalesnuevo.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finanzaspersonalesnuevo.data.AppDatabase;
import com.example.finanzaspersonalesnuevo.data.TransaccionDao;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;

import java.util.List;

public class TransaccionViewModel extends AndroidViewModel {

    private final TransaccionDao transaccionDao;
    private final LiveData<List<Transaccion>> transaccionesLiveData;

    public TransaccionViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstancia(application);
        transaccionDao = db.transaccionDao();
        transaccionesLiveData = transaccionDao.obtenerTodas(); // LiveData que observa todas las transacciones
    }

    // Obtener LiveData de todas las transacciones
    public LiveData<List<Transaccion>> obtenerTransaccionesLiveData() {
        return transaccionesLiveData;
    }

    // Agregar una nueva transacción (esto se debe ejecutar en un hilo secundario)
    public void agregarTransaccion(Transaccion transaccion) {
        AppDatabase.databaseWriteExecutor.execute(() -> transaccionDao.insertar(transaccion));
    }

    // Eliminar una transacción (también en un hilo secundario)
    public void eliminarTransaccion(Transaccion transaccion) {
        AppDatabase.databaseWriteExecutor.execute(() -> transaccionDao.eliminar(transaccion));
    }

    // Filtrar transacciones por categoría
    public LiveData<List<Transaccion>> obtenerTransaccionesPorCategoria(String categoria) {
        return transaccionDao.obtenerPorCategoria(categoria);
    }
}
