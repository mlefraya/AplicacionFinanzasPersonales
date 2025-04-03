package com.example.finanzaspersonalesnuevo.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.repository.ObjetivoAhorroRepository;
import java.util.List;

public class ObjetivoAhorroViewModel extends AndroidViewModel {
    private final ObjetivoAhorroRepository repository;
    private final LiveData<List<ObjetivoAhorro>> todosObjetivos;
    private final MutableLiveData<String> resultado = new MutableLiveData<>();

    public ObjetivoAhorroViewModel(Application application) {
        super(application);
        repository = new ObjetivoAhorroRepository(application);
        todosObjetivos = repository.getTodosObjetivos();
    }

    public LiveData<List<ObjetivoAhorro>> getTodosObjetivos() {
        return todosObjetivos;
    }

    public LiveData<String> getResultado() {
        return resultado;
    }

    public void guardarObjetivo(ObjetivoAhorro objetivo) {
        repository.insertar(objetivo, new ObjetivoAhorroRepository.Callback() {
            @Override
            public void onSuccess(long id) {
                resultado.postValue("Objetivo guardado (ID: " + id + ")");
            }

            @Override
            public void onError(String error) {
                resultado.postValue("Error: " + error);
            }
        });
    }

    public void insertar(ObjetivoAhorro objetivo) {
        repository.insertar(objetivo, new ObjetivoAhorroRepository.Callback() {
            @Override
            public void onSuccess(long id) {
                // Se puede manejar si se desea
            }
            @Override
            public void onError(String error) {
                // Manejar el error si es necesario
            }
        });
    }

    public void actualizar(ObjetivoAhorro objetivo) {
        repository.actualizar(objetivo);
    }

    public void eliminar(ObjetivoAhorro objetivo) {
        repository.eliminar(objetivo);
    }
}
