package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.finanzaspersonalesnuevo.R;

public class ConfiguracionFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // ------------------------------------------------------------
        // 1) Tema: solo Claro u Oscuro, con cambio en tiempo real
        // ------------------------------------------------------------
        ListPreference temaPref = findPreference("pref_theme");
        if (temaPref != null) {
            temaPref.setOnPreferenceChangeListener((preference, newValue) -> {
                String mode = (String) newValue;
                if ("light".equals(mode)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                // Actualizar summary
                int idx = temaPref.findIndexOfValue(mode);
                if (idx >= 0) {
                    temaPref.setSummary(temaPref.getEntries()[idx]);
                }
                return true;
            });
            // Inicializar summary
            String current = temaPref.getValue();
            int idx = temaPref.findIndexOfValue(current);
            if (idx >= 0) {
                temaPref.setSummary(temaPref.getEntries()[idx]);
            } else {
                temaPref.setSummary(temaPref.getEntries()[0]);
            }
        }

        // ------------------------------------------------------------
        // 2) Moneda predeterminada: lista ampliable
        // ------------------------------------------------------------
        ListPreference currencyPref = findPreference("pref_currency");
        if (currencyPref != null) {
            currencyPref.setOnPreferenceChangeListener((preference, newValue) -> {
                String code = (String) newValue;
                int idxCur = currencyPref.findIndexOfValue(code);
                if (idxCur >= 0) {
                    currencyPref.setSummary(currencyPref.getEntries()[idxCur]);
                }
                return true;
            });
            // Inicializar summary
            String curr = currencyPref.getValue();
            int idxCur = currencyPref.findIndexOfValue(curr);
            if (idxCur >= 0) {
                currencyPref.setSummary(currencyPref.getEntries()[idxCur]);
            } else {
                currencyPref.setSummary(currencyPref.getEntries()[0]);
            }
        }
    }
}
