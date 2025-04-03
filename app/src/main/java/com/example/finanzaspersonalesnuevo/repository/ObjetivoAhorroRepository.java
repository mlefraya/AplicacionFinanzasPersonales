package com.example.finanzaspersonalesnuevo.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.data.AppDatabase;
import com.example.finanzaspersonalesnuevo.data.ObjetivoAhorroDao;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObjetivoAhorroRepository {
    public interface Callback {
        void onSuccess(long id);
        void onError(String error);
    }

    private final ObjetivoAhorroDao objetivoAhorroDao;
    private final LiveData<List<ObjetivoAhorro>> todosObjetivos;
    private final ExecutorService executorService;

    public ObjetivoAhorroRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        objetivoAhorroDao = db.objetivoAhorroDao();
        todosObjetivos = objetivoAhorroDao.getTodosObjetivos();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ObjetivoAhorro>> getTodosObjetivos() {
        return todosObjetivos;
    }

    public void insertar(ObjetivoAhorro objetivo, Callback callback) {
        executorService.execute(() -> {
            try {
                long id = objetivoAhorroDao.insertarObjetivo(objetivo);
                callback.onSuccess(id);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void actualizar(ObjetivoAhorro objetivo) {
        executorService.execute(() -> objetivoAhorroDao.updateObjetivo(objetivo));
    }

    public void eliminar(ObjetivoAhorro objetivo) {
        executorService.execute(() -> objetivoAhorroDao.deleteObjetivo(objetivo));
    }
}
