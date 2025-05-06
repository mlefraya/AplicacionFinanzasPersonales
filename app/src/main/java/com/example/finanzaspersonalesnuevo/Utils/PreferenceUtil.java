// En Utils/PreferenceUtil.java
package com.example.finanzaspersonalesnuevo.Utils;

import android.content.Context;
import androidx.preference.PreferenceManager;

public class PreferenceUtil {
    /** Devuelve el código de moneda elegido, p.ej. "EUR", "USD", etc. */
    public static String getCurrency(Context ctx) {
        return PreferenceManager
                .getDefaultSharedPreferences(ctx)
                .getString("pref_currency", "EUR");
    }
}
