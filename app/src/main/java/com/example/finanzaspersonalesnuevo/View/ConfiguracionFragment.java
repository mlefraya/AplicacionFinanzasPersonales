package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.finanzaspersonalesnuevo.R;

public class ConfiguracionFragment extends Fragment {

    public ConfiguracionFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout de configuración (puedes personalizarlo luego)
        return inflater.inflate(R.layout.fragment_configuracion, container, false);
    }
}
