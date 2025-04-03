package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;

public class ObjetivoAhorroView extends Fragment {
    private EditText editNombre, editMonto, editPlazo;
    private Button btnGuardar;
    private ProgressBar progressBar;
    private TextView txtResultado;
    private ObjetivoAhorroViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_objetivo_ahorro_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editNombre = view.findViewById(R.id.editNombre);
        editMonto = view.findViewById(R.id.editMonto);
        editPlazo = view.findViewById(R.id.editPlazo);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        progressBar = view.findViewById(R.id.progressBar);
        txtResultado = view.findViewById(R.id.txtResultado);

        viewModel = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);
        viewModel.getResultado().observe(getViewLifecycleOwner(), resultado -> {
            txtResultado.setText(resultado);
            progressBar.setVisibility(View.GONE);
        });

        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {
                guardarObjetivo();
            }
        });
    }

    private boolean validarCampos() {
        if (editNombre.getText().toString().trim().isEmpty() ||
                editMonto.getText().toString().trim().isEmpty() ||
                editPlazo.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "¡Complete todos los campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void guardarObjetivo() {
        try {
            String nombre = editNombre.getText().toString().trim();
            double monto = Double.parseDouble(editMonto.getText().toString());
            int plazo = Integer.parseInt(editPlazo.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
            // Se crea el objetivo usando el constructor que calcula fechaFin a partir del plazo (en meses)
            ObjetivoAhorro nuevoObjetivo = new ObjetivoAhorro(nombre, monto, plazo);
            viewModel.guardarObjetivo(nuevoObjetivo);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Error en valores numéricos", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }
}
