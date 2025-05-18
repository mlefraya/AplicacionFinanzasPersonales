package com.example.finanzaspersonalesnuevo.View;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.finanzaspersonalesnuevo.ViewModel.BalanceViewModel;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;
import com.example.finanzaspersonalesnuevo.adapter.ObjetivoAhorroAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
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
        balanceVm  = new ViewModelProvider(this).get(BalanceViewModel.class);

        // 1) Observa balance para actualizar progreso
        balanceVm.getBalance().observe(getViewLifecycleOwner(), balance -> {
            currentBalance = balance;
            adapter.setBalance(balance);
        });

        // 2) Observa objetivos
        objetivoVm.getTodosObjetivos().observe(getViewLifecycleOwner(), objetivos -> {
            adapter.setItems(objetivos);
        });

        // 3) Clicks de editar y eliminar
        adapter.setOnItemClickListener(this::showEditDialog);
        adapter.setOnItemLongClickListener(this::showDeleteConfirmation);

        // 4) FAB para añadir nuevo objetivo
        FloatingActionButton fab = root.findViewById(R.id.fab_add_objetivo);
        fab.setOnClickListener(v -> {
            // Navegar al fragmento de creación de objetivo
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ObjetivoAhorroView())
                    .addToBackStack(null)
                    .commit();
        });

        return root;
    }

    private void showEditDialog(ObjetivoAhorro obj) {
        View form = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_editar_objetivo, null, false);
        EditText etDesc = form.findViewById(R.id.etDescObjetivo);
        EditText etMeta = form.findViewById(R.id.etMetaObjetivo);
        etDesc.setText(obj.getDescripcion());
        etMeta.setText(String.valueOf(obj.getMontoObjetivo()));
        etMeta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(requireContext())
                .setTitle("Editar objetivo")
                .setView(form)
                .setPositiveButton("Guardar", (d, w) -> {
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
        new AlertDialog.Builder(requireContext())
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
