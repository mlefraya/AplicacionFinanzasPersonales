package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.Utils.ExportarDatosUtil;

public class ExportarDatosFragment extends Fragment {

    private static final String TAG = "ExportarDatosFrag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exportar_datos, container, false);

        root.findViewById(R.id.btn_exportar).setOnClickListener(v -> {
            Log.d(TAG, "Botón exportar presionado");
            Toast.makeText(requireContext(), "Iniciando exportación...", Toast.LENGTH_SHORT).show();
            ExportarDatosUtil.exportar(requireContext());
        });

        return root;
    }
}
