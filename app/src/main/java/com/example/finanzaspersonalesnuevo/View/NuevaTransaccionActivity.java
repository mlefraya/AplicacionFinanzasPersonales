package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.data.AppDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NuevaTransaccionActivity extends AppCompatActivity {

    private EditText etDescripcion, etCantidad, etFecha;
    private Spinner spinnerTipo, spinnerCategoria;
    private Button btnGuardar, btnCancelar;
    private AppDatabase db;
    private final SimpleDateFormat sdf =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();
    private final int MIN_YEAR = Calendar.getInstance().get(Calendar.YEAR); // 2025

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);

        sdf.setLenient(false);

        etDescripcion   = findViewById(R.id.etDescripcion);
        etCantidad      = findViewById(R.id.etPrecio);
        etFecha         = findViewById(R.id.etFecha);
        spinnerTipo     = findViewById(R.id.spinnerTipo);
        spinnerCategoria= findViewById(R.id.spinnerCategoria);
        btnGuardar      = findViewById(R.id.btnGuardar);
        btnCancelar     = findViewById(R.id.btnCancelar);

        String[] tipos = {"Ingreso", "Gasto"};
        ArrayAdapter<String> adapterTipos =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        tipos);
        adapterTipos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipos);

        spinnerTipo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(AdapterView<?> parent,
                                                         View view,
                                                         int position,
                                                         long id) {
                        String[] categorias = position == 0
                                ? new String[]{"Salario", "Inversión", "Regalo"}
                                : new String[]{"Compras", "Hogar", "Transporte", "Ocio", "Comida"};
                        ArrayAdapter<String> adapterCat =
                                new ArrayAdapter<>(
                                        NuevaTransaccionActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        categorias
                                );
                        adapterCat.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item);
                        spinnerCategoria.setAdapter(adapterCat);
                    }
                    @Override public void onNothingSelected(AdapterView<?> parent) { }
                });

        db = AppDatabase.getInstance(getApplicationContext());

        btnGuardar.setOnClickListener(v -> guardarTransaccion());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarTransaccion() {
        String descripcion = etDescripcion.getText()
                .toString().trim();
        String cantidadStr = etCantidad.getText()
                .toString().trim();
        String fechaStr    = etFecha.getText()
                .toString().trim();
        String tipo        = (String) spinnerTipo
                .getSelectedItem();
        String categoria   = (String) spinnerCategoria
                .getSelectedItem();

        if (descripcion.isEmpty() || cantidadStr.isEmpty()
                || fechaStr.isEmpty()
                || tipo == null || categoria == null) {
            Toast.makeText(this,
                    "Por favor, completa todos los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Cantidad inválida",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Date fecha;
        try {
            fecha = sdf.parse(fechaStr);
        } catch (ParseException e) {
            Toast.makeText(this,
                    "Formato de fecha inválido. Usa dd/MM/yyyy",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int year = cal.get(Calendar.YEAR);
        if (year < MIN_YEAR) {
            Toast.makeText(this,
                    "El año debe ser >= " + MIN_YEAR,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Transaccion t = new Transaccion(
                descripcion,
                cantidad,
                fecha,
                categoria,
                tipo
        );

        executor.execute(() -> {
            long id = db.transaccionDao()
                    .insertTransaccion(t);
            runOnUiThread(() -> {
                if (id != -1) {
                    Toast.makeText(this,
                            "Transacción guardada",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this,
                            "Error al guardar la transacción",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
