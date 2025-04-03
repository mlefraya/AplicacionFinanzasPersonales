package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;
import com.example.finanzaspersonalesnuevo.adapter.ObjetivoAhorroAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ObjetivosAhorroFragment extends Fragment {

    private EditText etDescripcion, etMontoObjetivo, etMontoActual, etFechaInicio, etFechaFin;
    private Button btnGuardarObjetivo;
    private RecyclerView recyclerView;
    private ObjetivoAhorroAdapter adapter;
    private ObjetivoAhorroViewModel viewModel;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objetivos_ahorro, container, false);
        inicializarVistas(view);
        configurarRecyclerView();
        configurarViewModel();
        configurarBotonGuardar();
        return view;
    }

    private void inicializarVistas(View view) {
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etMontoObjetivo = view.findViewById(R.id.etMontoObjetivo);
        etMontoActual = view.findViewById(R.id.etMontoActual);
        etFechaInicio = view.findViewById(R.id.etFechaInicio);
        etFechaFin = view.findViewById(R.id.etFechaFin);
        btnGuardarObjetivo = view.findViewById(R.id.btnGuardarObjetivo);
        recyclerView = view.findViewById(R.id.recyclerViewObjetivos);
    }

    private void configurarRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ObjetivoAhorroAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void configurarViewModel() {
        viewModel = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);
        viewModel.getTodosObjetivos().observe(getViewLifecycleOwner(), objetivos -> {
            if (objetivos != null) {
                adapter.actualizarLista(objetivos);
            }
        });
    }

    private void configurarBotonGuardar() {
        btnGuardarObjetivo.setOnClickListener(v -> {
            if (validarCampos()) guardarObjetivo();
        });
    }

    private boolean validarCampos() {
        if (etDescripcion.getText().toString().trim().isEmpty() ||
                etMontoObjetivo.getText().toString().trim().isEmpty() ||
                etMontoActual.getText().toString().trim().isEmpty() ||
                etFechaInicio.getText().toString().trim().isEmpty() ||
                etFechaFin.getText().toString().trim().isEmpty()) {

            mostrarToast("¡Complete todos los campos!");
            return false;
        }
        return true;
    }

    private void guardarObjetivo() {
        try {
            String descripcion = etDescripcion.getText().toString().trim();
            double montoObjetivo = Double.parseDouble(etMontoObjetivo.getText().toString());
            double montoActual = Double.parseDouble(etMontoActual.getText().toString());
            Date fechaInicio = sdf.parse(etFechaInicio.getText().toString());
            Date fechaFin = sdf.parse(etFechaFin.getText().toString());

            if (fechaFin.before(fechaInicio)) {
                mostrarToast("La fecha final debe ser posterior a la inicial");
                return;
            }

            ObjetivoAhorro nuevoObjetivo = new ObjetivoAhorro(
                    descripcion,
                    montoObjetivo,
                    montoActual,
                    fechaInicio,
                    fechaFin
            );

            viewModel.insertar(nuevoObjetivo);
            limpiarCampos();
            mostrarToast("Objetivo guardado exitosamente");

        } catch (NumberFormatException | ParseException e) {
            mostrarToast("Error en formato de datos");
        }
    }

    private void limpiarCampos() {
        etDescripcion.setText("");
        etMontoObjetivo.setText("");
        etMontoActual.setText("");
        etFechaInicio.setText("");
        etFechaFin.setText("");
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}