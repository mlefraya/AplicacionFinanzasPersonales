package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.finanzaspersonalesnuevo.Utils.CurrencyConverter;
import com.example.finanzaspersonalesnuevo.Utils.PreferenceUtil;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;

import java.util.Calendar;
import java.util.Date;

public class ObjetivoAhorroView extends Fragment {
    private EditText editNombre, editPrecio, editPlazo;
    private Button btnGuardar, btnCancelar;
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
        super.onViewCreated(view, savedInstanceState);

        editNombre  = view.findViewById(R.id.editNombre);
        editPrecio  = view.findViewById(R.id.editPrecio);
        editPlazo   = view.findViewById(R.id.editPlazo);
        btnGuardar  = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        progressBar = view.findViewById(R.id.progressBar);

        viewModel = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);

        btnGuardar.setOnClickListener(v -> {
            if (!validarCampos()) return;
            progressBar.setVisibility(View.VISIBLE);
            guardarObjetivo();
        });

        btnCancelar.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    private boolean validarCampos() {
        if (TextUtils.isEmpty(editNombre.getText())
                || TextUtils.isEmpty(editPrecio.getText())
                || TextUtils.isEmpty(editPlazo.getText())) {
            Toast.makeText(requireContext(),
                    "¡Complete todos los campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardarObjetivo() {
        String nombre = editNombre.getText().toString().trim();
        double precioLocal;
        int plazoMeses;
        try {
            precioLocal = Double.parseDouble(editPrecio.getText().toString());
            plazoMeses  = Integer.parseInt(editPlazo.getText().toString());
        } catch (NumberFormatException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(requireContext(),
                    "Valores numéricos inválidos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertimos precioLocal → EUR
        String currencyCode = PreferenceUtil.getCurrency(requireContext());
        double montoEnEur = CurrencyConverter.toEur(precioLocal, currencyCode);

        Date fechaInicio = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        cal.add(Calendar.MONTH, plazoMeses);
        Date fechaFin = cal.getTime();

        ObjetivoAhorro nuevo = new ObjetivoAhorro(
                nombre,
                montoEnEur,    // almacenamos en EUR
                0.0,           // montoActual inicia en 0
                fechaInicio,
                fechaFin
        );

        viewModel.guardarObjetivo(nuevo, new ObjetivoAhorroViewModel.InsertCallback() {
            @Override
            public void onSuccess(long id) {
                requireActivity().runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(),
                            "Objetivo creado", Toast.LENGTH_SHORT).show();
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
