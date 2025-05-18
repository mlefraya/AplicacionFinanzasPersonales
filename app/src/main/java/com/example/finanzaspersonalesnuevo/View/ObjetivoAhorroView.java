package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;

import java.util.Calendar;
import java.util.Date;

public class ObjetivoAhorroView extends Fragment {
    private EditText editNombre, editMonto, editPlazo;
    private Button btnGuardar;
    private ProgressBar progressBar;
    private ObjetivoAhorroViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_objetivo_ahorro_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editNombre  = view.findViewById(R.id.editNombre);
        editMonto   = view.findViewById(R.id.editMonto);
        editPlazo   = view.findViewById(R.id.editPlazo);
        btnGuardar  = view.findViewById(R.id.btnGuardar);
        progressBar = view.findViewById(R.id.progressBar);

        viewModel = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);

        btnGuardar.setOnClickListener(v -> {
            if (!validarCampos()) return;
            progressBar.setVisibility(View.VISIBLE);
            guardarObjetivo();
        });
    }

    private boolean validarCampos() {
        if (editNombre.getText().toString().trim().isEmpty()
                || editMonto.getText().toString().trim().isEmpty()
                || editPlazo.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(),
                    "¡Complete todos los campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardarObjetivo() {
        String nombre = editNombre.getText().toString().trim();
        double monto;
        int plazo;
        try {
            monto = Double.parseDouble(editMonto.getText().toString());
            plazo = Integer.parseInt(editPlazo.getText().toString());
        } catch (NumberFormatException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(),
                    "Valores numéricos inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        Date fechaInicio = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        cal.add(Calendar.MONTH, plazo);
        Date fechaFin = cal.getTime();

        ObjetivoAhorro nuevo = new ObjetivoAhorro(
                nombre, monto, 0.0, fechaInicio, fechaFin
        );

        viewModel.guardarObjetivo(nuevo, new ObjetivoAhorroViewModel.InsertCallback() {
            @Override
            public void onSuccess(long id) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(),
                            "Objetivo creado", Toast.LENGTH_SHORT).show();
                    // Volver al listado
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
            }
            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(),
                            "Error: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
