package com.example.finanzaspersonalesnuevo.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListadoTransaccionesView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransaccionAdapter adapter;
    private List<Transaccion> transacciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_transacciones_view); // Diseño principal de la actividad

        // Vincula el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTransacciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        
        // Datos de prueba
        transacciones = generarDatosPrueba();

        // Configura el adaptador
        adapter = new TransaccionAdapter(transacciones);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListadoTransaccionesView.this, NuevaTransaccionActivity.class);
                startActivity(intent);
            }
        });
    }

    private List<Transaccion> generarDatosPrueba() {
        List<Transaccion> lista = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            lista.add(new Transaccion("Compra supermercado", 50.0, dateFormat.parse("2025-01-10"), "Compras"));
            lista.add(new Transaccion("Pago alquiler", 500.0, dateFormat.parse("2025-01-01"), "Hogar"));
            lista.add(new Transaccion("Transporte público", 25.0, dateFormat.parse("2025-01-12"), "Transporte"));
        } catch (ParseException e) {
            e.printStackTrace(); // Maneja el error si ocurre un problema al analizar las fechas
        }

        return lista;
    }
}
