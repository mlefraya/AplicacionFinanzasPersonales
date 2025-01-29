package com.example.finanzaspersonalesnuevo.data;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    // Convierte un timestamp (Long) a un objeto Date
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    // Convierte un objeto Date a un timestamp (Long)
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
