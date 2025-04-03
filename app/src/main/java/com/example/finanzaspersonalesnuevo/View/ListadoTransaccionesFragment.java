package com.example.finanzaspersonalesnuevo.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.adapter.TransaccionAdapter;
import com.example.finanzaspersonalesnuevo.data.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListadoTransaccionesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TransaccionAdapter adapter;
    private AppDatabase db;
    private SearchView searchView;
    private FloatingActionButton fabAgregar;

    public ListadoTransaccionesFragment() {
        // Constructor vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado_transacciones, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTransacciones);
        searchView = view.findViewById(R.id.searchViewTransacciones);
        fabAgregar = view.findViewById(R.id.fabAgregar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(getContext());

        // Observa los cambios en la base de datos
        db.transaccionDao().getAllTransacciones().observe(getViewLifecycleOwner(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transacciones) {
                if (adapter == null) {
                    adapter = new TransaccionAdapter(transacciones, new TransaccionAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClicked(Transaccion transaccion) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Eliminar transacción")
                                    .setMessage("¿Estás seguro de que deseas eliminar esta transacción?")
                                    .setPositiveButton("Sí", (dialog, which) -> {
                                        AppDatabase.databaseWriteExecutor.execute(() -> {
                                            db.transaccionDao().deleteTransaccion(transaccion);
                                        });
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.actualizarLista(transacciones);
                }
            }
        });

        // Configura la búsqueda
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarTransacciones(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarTransacciones(newText);
                return false;
            }
        });

        // Botón para agregar nueva transacción
        fabAgregar.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), NuevaTransaccionActivity.class));
        });

        return view;
    }

    private void filtrarTransacciones(String query) {
        if (query.isEmpty()) {
            db.transaccionDao().getAllTransacciones().observe(getViewLifecycleOwner(), new Observer<List<Transaccion>>() {
                @Override
                public void onChanged(List<Transaccion> transacciones) {
                    adapter.actualizarLista(transacciones);
                }
            });
        } else {
            db.transaccionDao().getTransaccionesByDescripcion(query).observe(getViewLifecycleOwner(), new Observer<List<Transaccion>>() {
                @Override
                public void onChanged(List<Transaccion> transacciones) {
                    adapter.actualizarLista(transacciones);
                }
            });
        }
    }
}
