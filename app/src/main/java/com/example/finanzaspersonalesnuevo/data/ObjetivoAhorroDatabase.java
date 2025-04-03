package com.example.finanzaspersonalesnuevo.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;

@Database(entities = {ObjetivoAhorro.class}, version = 1, exportSchema = false)
public abstract class ObjetivoAhorroDatabase extends RoomDatabase {

    public abstract ObjetivoAhorroDao objetivoAhorroDao();

    private static volatile ObjetivoAhorroDatabase INSTANCE;

    public static ObjetivoAhorroDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ObjetivoAhorroDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ObjetivoAhorroDatabase.class, "objetivo_ahorro_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
