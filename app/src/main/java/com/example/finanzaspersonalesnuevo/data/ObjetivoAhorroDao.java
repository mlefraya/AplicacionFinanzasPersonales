package com.example.finanzaspersonalesnuevo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;

import java.util.List;

@Dao
public interface ObjetivoAhorroDao {

    @Insert
    long insertarObjetivo(ObjetivoAhorro objetivo);

    @Update
    int updateObjetivo(ObjetivoAhorro objetivo);

    @Delete
    void deleteObjetivo(ObjetivoAhorro objetivo);

    @Query("SELECT * FROM objetivo_ahorro ORDER BY fecha_fin ASC")
    LiveData<List<ObjetivoAhorro>> getTodosObjetivos();

    @Query("SELECT * FROM objetivo_ahorro ORDER BY id DESC LIMIT 1")
    LiveData<ObjetivoAhorro> getUltimoObjetivo();
    @Query("SELECT * FROM objetivo_ahorro WHERE id = :id")
    LiveData<ObjetivoAhorro> getObjetivoById(int id);
}
