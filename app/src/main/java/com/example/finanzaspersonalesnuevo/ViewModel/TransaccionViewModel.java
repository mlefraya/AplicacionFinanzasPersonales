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
        AppDatabase db = AppDatabase.getInstance(application);
        transaccionDao = db.transaccionDao();
        transaccionesLiveData = transaccionDao.getAllTransacciones();
    }

    // Obtener LiveData de todas las transacciones
    public LiveData<List<Transaccion>> getTransaccionesLiveData() {
        return transaccionesLiveData;
    }

    // Agregar una nueva transacción (se ejecuta en un hilo secundario)
    public void addTransaccion(Transaccion transaccion) {
        AppDatabase.databaseWriteExecutor.execute(() -> transaccionDao.insertTransaccion(transaccion));
    }

    // Eliminar una transacción (se ejecuta en un hilo secundario)
    public void deleteTransaccion(Transaccion transaccion) {
        AppDatabase.databaseWriteExecutor.execute(() -> transaccionDao.deleteTransaccion(transaccion));
    }

    // Filtrar transacciones por categoría
    public LiveData<List<Transaccion>> getTransaccionesByCategoria(String categoria) {
        return transaccionDao.getTransaccionesByCategoria(categoria);
    }
}
