package com.example.finanzaspersonalesnuevo.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.repository.ObjetivoAhorroRepository;

import java.util.List;

public class ObjetivoAhorroViewModel extends AndroidViewModel {

    /** Callback para informar de éxito o fracaso en la inserción de un objetivo. */
    public interface InsertCallback {
        void onSuccess(long id);
        void onError(String error);
    }

    private final ObjetivoAhorroRepository repository;
    private final LiveData<List<ObjetivoAhorro>> todosObjetivos;

    public ObjetivoAhorroViewModel(@NonNull Application application) {
        super(application);
        repository = new ObjetivoAhorroRepository(application);
        todosObjetivos = repository.getTodosObjetivos();
    }

    /** LiveData con la lista de todos los objetivos de ahorro. */
    public LiveData<List<ObjetivoAhorro>> getTodosObjetivos() {
        return todosObjetivos;
    }

    /**
     * Inserta un nuevo objetivo en segundo plano y notifica el resultado mediante callback.
     *
     * @param objetivo El objetivo a insertar.
     * @param cb       Callback para recibir onSuccess(id) o onError(msg).
     */
    public void guardarObjetivo(ObjetivoAhorro objetivo, InsertCallback cb) {
        repository.insertar(objetivo, new ObjetivoAhorroRepository.Callback() {
            @Override
            public void onSuccess(long id) {
                cb.onSuccess(id);
            }
            @Override
            public void onError(String error) {
                cb.onError(error);
            }
        });
    }

    /** Actualiza un objetivo existente en segundo plano. */
    public void actualizar(ObjetivoAhorro objetivo) {
        repository.actualizar(objetivo);
    }

    /** Elimina un objetivo existente en segundo plano. */
    public void eliminar(ObjetivoAhorro objetivo) {
        repository.eliminar(objetivo);
    }
}
