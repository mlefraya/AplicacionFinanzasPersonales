package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.preference.EditTextPreference;
import com.example.finanzaspersonalesnuevo.R;

public class ConfiguracionFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Opcional: reaccionar a cambios inmediatos
        ListPreference temaPref = findPreference("pref_theme");
        temaPref.setOnPreferenceChangeListener((pref, newValue) -> {
            // Aquí podrías aplicar el tema inmediatamente
            // e.g. AppCompatDelegate.setDefaultNightMode(...)
            return true;
        });

        SwitchPreferenceCompat notifPref = findPreference("pref_notifications");
        notifPref.setOnPreferenceChangeListener((pref, newValue) -> {
            // Habilitar/deshabilitar notificaciones
            return true;
        });
    }
}
