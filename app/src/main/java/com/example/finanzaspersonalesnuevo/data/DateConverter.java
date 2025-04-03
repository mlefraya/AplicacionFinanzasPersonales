package com.example.finanzaspersonalesnuevo.data;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    @TypeConverter
    public static Date fromTimestamp(String value) {
        try {
            return value == null ? null : sdf.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : sdf.format(date);
    }
}
