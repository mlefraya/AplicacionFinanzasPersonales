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

    // Obtener todas las transacciones
    @Query("SELECT * FROM transaccion") // Cambia el nombre de la tabla a "transaccion"
    LiveData<List<Transaccion>> obtenerTodas();

    // Obtener transacciones filtradas por categoría
    @Query("SELECT * FROM transaccion WHERE categoria = :categoria") // Cambia el nombre de la tabla
    LiveData<List<Transaccion>> obtenerPorCategoria(String categoria);

    // Insertar una nueva transacción
    @Insert
    void insertar(Transaccion transaccion);

    // Eliminar una transacción
    @Delete
    void eliminar(Transaccion transaccion);

    // (Opcional) Actualizar una transacción existente
    @Update
    void actualizar(Transaccion transaccion);
}
