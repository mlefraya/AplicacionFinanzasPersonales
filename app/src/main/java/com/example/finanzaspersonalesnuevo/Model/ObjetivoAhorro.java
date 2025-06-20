package com.example.finanzaspersonalesnuevo.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.finanzaspersonalesnuevo.data.Converters;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "objetivo_ahorro")
@TypeConverters(Converters.class)
public class ObjetivoAhorro implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    /** Monto objetivo en EUR */
    @ColumnInfo(name = "monto_objetivo")
    private double montoObjetivo;

    /** Monto ahorrado en EUR */
    @ColumnInfo(name = "monto_actual")
    private double montoActual;

    @ColumnInfo(name = "fecha_inicio")
    private Date fechaInicio;

    @ColumnInfo(name = "fecha_fin")
    private Date fechaFin;

    public ObjetivoAhorro() { }

    public ObjetivoAhorro(String descripcion,
                          double montoObjetivo,
                          double montoActual,
                          Date fechaInicio,
                          Date fechaFin) {
        this.descripcion    = descripcion;
        this.montoObjetivo  = montoObjetivo;
        this.montoActual    = montoActual;
        this.fechaInicio    = fechaInicio;
        this.fechaFin       = fechaFin;
    }

    public ObjetivoAhorro(String descripcion,
                          double montoObjetivo,
                          int plazoMeses) {
        this.descripcion   = descripcion;
        this.montoObjetivo = montoObjetivo;
        this.montoActual   = 0;
        this.fechaInicio   = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.fechaInicio);
        cal.add(Calendar.MONTH, plazoMeses);
        this.fechaFin      = cal.getTime();
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMontoObjetivo() { return montoObjetivo; }
    public void setMontoObjetivo(double montoObjetivo) { this.montoObjetivo = montoObjetivo; }

    public double getMontoActual() { return montoActual; }
    public void setMontoActual(double montoActual) { this.montoActual = montoActual; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    /** (Compatibilidad con UI antigua) */
    public double getMeta() {
        return montoObjetivo;
    }

    /** (Compatibilidad con UI antigua) */
    public double getCantidadAhorrada() {
        return montoActual;
    }

    /** Progreso en % */
    public double getProgreso() {
        if (montoObjetivo <= 0) return 0;
        return (montoActual / montoObjetivo) * 100;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s - Progreso: %.2f%%",
                descripcion, getProgreso());
    }
}
