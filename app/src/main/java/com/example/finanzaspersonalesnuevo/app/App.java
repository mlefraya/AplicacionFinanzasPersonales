package com.example.finanzaspersonalesnuevo.app;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.finanzaspersonalesnuevo.Utils.PreferenceUtil;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Lee la preferencia de tema y aplícalo antes de inflar cualquier Activity
        boolean darkMode = PreferenceUtil.isDarkModeEnabled(this);
        AppCompatDelegate.setDefaultNightMode(
                darkMode
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}
