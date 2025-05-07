package com.example.finanzaspersonalesnuevo.Utils;

import android.content.Context;
import androidx.preference.PreferenceManager;

public class PreferenceUtil {

    /**
     * Devuelve el código de moneda elegido, por ejemplo "EUR", "USD", etc.
     */
    public static String getCurrency(Context ctx) {
        return PreferenceManager
                .getDefaultSharedPreferences(ctx)
                .getString("pref_currency", "EUR");
    }

    /**
     * Indica si el usuario ha seleccionado el tema oscuro ("dark").
     *  - "dark"  → devuelve true
     *  - cualquier otro valor (por defecto "light") → devuelve false
     */
    public static boolean isDarkModeEnabled(Context ctx) {
        String theme = PreferenceManager
                .getDefaultSharedPreferences(ctx)
                .getString("pref_theme", "light");
        return "dark".equals(theme);
    }
}
