package com.example.finanzaspersonalesnuevo.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.data.AppDatabase;

import java.util.List;

public class BalanceViewModel extends AndroidViewModel {

    private final LiveData<Double> balance;

    public BalanceViewModel(@NonNull Application application) {
        super(application);
        // Observa la lista de transacciones
        LiveData<List<Transaccion>> transLive = AppDatabase
                .getInstance(application)
                .transaccionDao()
                .getAllLive();

        // Transforma la lista en un único Double (ingresos - gastos)
        balance = Transformations.map(transLive, list -> {
            double ingresos = 0, gastos = 0;
            if (list != null) {
                for (Transaccion t : list) {
                    if ("Ingreso".equalsIgnoreCase(t.getTipo())) {
                        ingresos += t.getCantidad();
                    } else {
                        gastos += t.getCantidad();
                    }
                }
            }
            return ingresos - gastos;
        });
    }

    /** LiveData con el balance actual (ingresos - gastos). */
    public LiveData<Double> getBalance() {
        return balance;
    }
}
