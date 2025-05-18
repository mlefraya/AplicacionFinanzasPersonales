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

    /** Inserta una transacción y devuelve su nuevo ID. */
    @Insert
    long insertTransaccion(Transaccion transaccion);

    /** Actualiza los datos de una transacción existente. */
    @Update
    void updateTransaccion(Transaccion transaccion);

    /** Elimina una transacción. */
    @Delete
    void deleteTransaccion(Transaccion transaccion);

    /** Devuelve todas las transacciones en un LiveData ordenadas por fecha descendente. */
    @Query("SELECT * FROM transaccion ORDER BY fecha DESC")
    LiveData<List<Transaccion>> getAllTransacciones();

    /** Igual que getAllTransacciones(), pero con nombre genérico para balance. */
    @Query("SELECT * FROM transaccion")
    LiveData<List<Transaccion>> getAllLive();

    /** Devuelve todas las transacciones de forma síncrona. */
    @Query("SELECT * FROM transaccion ORDER BY fecha DESC")
    List<Transaccion> getAllSync();

    /** Filtra transacciones por categoría. */
    @Query("SELECT * FROM transaccion WHERE categoria = :categoria ORDER BY fecha DESC")
    LiveData<List<Transaccion>> getTransaccionesByCategoria(String categoria);

    /** Filtra transacciones por descripción parcial. */
    @Query("SELECT * FROM transaccion WHERE descripcion LIKE '%' || :descripcion || '%' ORDER BY fecha DESC")
    LiveData<List<Transaccion>> getTransaccionesByDescripcion(String descripcion);

    /** Obtiene una transacción por su ID. */
    @Query("SELECT * FROM transaccion WHERE id = :id")
    LiveData<Transaccion> getTransaccionById(int id);
}
