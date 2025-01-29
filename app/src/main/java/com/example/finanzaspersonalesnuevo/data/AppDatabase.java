package com.example.finanzaspersonalesnuevo.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Transaccion.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class) // Agrega los conversores
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;
    public abstract TransaccionDao transaccionDao();

    // Executor para las operaciones de base de datos en segundo plano
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4); // Número de hilos que el ejecutor puede manejar

    public static synchronized AppDatabase getInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "finanzas_personales_db")
                    .fallbackToDestructiveMigration() // Manejo básico de migraciones
                    .build();
        }
        return instancia;
    }
}
