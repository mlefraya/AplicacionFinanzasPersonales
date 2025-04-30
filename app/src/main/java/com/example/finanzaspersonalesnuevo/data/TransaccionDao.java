package com.example.finanzaspersonalesnuevo.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;

import java.util.List;

@Dao
public interface TransaccionDao {

    // Ahora devuelve el id de la transacción insertada
    @Insert
    long insertTransaccion(Transaccion transaccion);

    @Update
    void updateTransaccion(Transaccion transaccion);

    @Delete
    void deleteTransaccion(Transaccion transaccion);

    @Query("SELECT * FROM transaccion ORDER BY fecha DESC")
    LiveData<List<Transaccion>> getAllTransacciones();

    @Query("SELECT * FROM transaccion ORDER BY fecha DESC")
    List<Transaccion> getAllSync();

    @Query("SELECT * FROM transaccion WHERE categoria = :categoria ORDER BY fecha DESC")
    LiveData<List<Transaccion>> getTransaccionesByCategoria(String categoria);

    @Query("SELECT * FROM transaccion WHERE descripcion LIKE '%' || :descripcion || '%' ORDER BY fecha DESC")
    LiveData<List<Transaccion>> getTransaccionesByDescripcion(String descripcion);

    @Query("SELECT * FROM transaccion WHERE id = :id")
    LiveData<Transaccion> getTransaccionById(int id);
}
