package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.BalanceViewModel;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;
import com.example.finanzaspersonalesnuevo.adapter.ObjetivoAhorroAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class ObjetivosAhorroFragment extends Fragment {

    private ObjetivoAhorroViewModel objetivoVm;
    private BalanceViewModel balanceVm;
    private ObjetivoAhorroAdapter adapter;
    private RecyclerView recyclerView;
    private double currentBalance = 0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_objetivos_ahorro, container, false);

        // RecyclerView y Adapter
        recyclerView = root.findViewById(R.id.recyclerViewObjetivos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ObjetivoAhorroAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModels
        objetivoVm = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);
        balanceVm = new ViewModelProvider(this).get(BalanceViewModel.class);

        // Observa balance para actualizar progreso
        balanceVm.getBalance().observe(getViewLifecycleOwner(), balance -> {
            currentBalance = balance;
            adapter.setBalance(balance);
        });

        // Observa objetivos
        objetivoVm.getTodosObjetivos().observe(getViewLifecycleOwner(), objetivos -> {
            adapter.setItems(objetivos);
        });

        // Clicks de editar y eliminar
        adapter.setOnItemClickListener(this::showEditDialog);
        adapter.setOnItemLongClickListener(this::showDeleteConfirmation);

        // FAB para añadir nuevo objetivo
        FloatingActionButton fab = root.findViewById(R.id.fab_add_objetivo);
        fab.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ObjetivoAhorroView())
                    .addToBackStack(null)
                    .commit();
        });

        return root;
    }

    private void showEditDialog(ObjetivoAhorro obj) {
        // Infla el layout personalizado sin título
        View form = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_editar_objetivo, null, false);

        TextInputEditText etDesc = form.findViewById(R.id.etDescObjetivo);
        TextInputEditText etMeta = form.findViewById(R.id.etMetaObjetivo);

        etDesc.setText(obj.getDescripcion());
        etMeta.setText(String.valueOf(obj.getMontoObjetivo()));
        etMeta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new MaterialAlertDialogBuilder(requireContext()) // sin estilo adicional
                .setView(form)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nuevaDesc = etDesc.getText().toString().trim();
                    double nuevaMeta;
                    try {
                        nuevaMeta = Double.parseDouble(etMeta.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(requireContext(), "Meta inválida", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    obj.setDescripcion(nuevaDesc);
                    obj.setMontoObjetivo(nuevaMeta);
                    objetivoVm.actualizar(obj);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showDeleteConfirmation(ObjetivoAhorro obj) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Eliminar objetivo")
                .setMessage(String.format(Locale.getDefault(),
                        "¿Eliminar \"%s\"?", obj.getDescripcion()))
                .setPositiveButton("Sí", (d, w) -> {
                    objetivoVm.eliminar(obj);
                    Toast.makeText(requireContext(),
                            "Objetivo eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
