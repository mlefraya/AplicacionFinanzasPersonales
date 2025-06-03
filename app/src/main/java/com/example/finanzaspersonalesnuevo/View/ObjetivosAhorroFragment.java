package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ObjetivosAhorroFragment extends Fragment {

    private ObjetivoAhorroViewModel objetivoVm;
    private BalanceViewModel balanceVm;
    private ObjetivoAhorroAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_objetivos_ahorro, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewObjetivos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ObjetivoAhorroAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        objetivoVm = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);
        balanceVm  = new ViewModelProvider(this).get(BalanceViewModel.class);

        // 1) Observar objetivos y enviarlos al adapter
        objetivoVm.getTodosObjetivos().observe(getViewLifecycleOwner(), objetivos -> {
            adapter.setItems(objetivos);
        });

        // 2) Observar el balance y enviarlo al adapter
        balanceVm.getBalance().observe(getViewLifecycleOwner(), bal -> {
            if (bal == null) bal = 0.0;
            adapter.setBalance(bal);
        });

        adapter.setOnItemClickListener(this::navigateToEditar);
        adapter.setOnItemLongClickListener(obj -> {
            objetivoVm.eliminar(obj);
            Toast.makeText(requireContext(), "Objetivo eliminado", Toast.LENGTH_SHORT).show();
        });

        FloatingActionButton fab = root.findViewById(R.id.fab_add_objetivo);
        fab.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ObjetivoAhorroView())
                .addToBackStack(null)
                .commit()
        );

        return root;
    }

    private void navigateToEditar(ObjetivoAhorro obj) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, EditarObjetivoFragment.newInstance(obj))
                .addToBackStack(null)
                .commit();
    }
}
