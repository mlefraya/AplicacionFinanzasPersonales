package com.example.finanzaspersonalesnuevo.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.data.AppDatabase;
import com.example.finanzaspersonalesnuevo.data.TransaccionDao;

public class EditarTransaccionViewModel extends AndroidViewModel {
    private final TransaccionDao transaccionDao;

    public EditarTransaccionViewModel(Application application) {
        super(application);
        // Inicializa el DAO
        AppDatabase db = AppDatabase.getInstance(application);
        transaccionDao = db.transaccionDao();
    }

    // Devuelve un LiveData con la transacción que se puede observar en la actividad
    public LiveData<Transaccion> getTransaccionById(int id) {
        return transaccionDao.getTransaccionById(id);
    }

    // Método para actualizar una transacción
    public void actualizarTransaccion(Transaccion transaccion) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            transaccionDao.updateTransaccion(transaccion);
        });
    }
}
