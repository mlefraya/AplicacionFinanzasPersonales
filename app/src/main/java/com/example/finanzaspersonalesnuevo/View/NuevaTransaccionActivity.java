package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NuevaTransaccionActivity extends AppCompatActivity {

    private EditText etDescripcion, etCantidad, etFecha;
    private Spinner spinnerCategoria, spinnerTipo;
    private Button btnGuardar, btnCancelar;
    private AppDatabase db;
    private static final String TAG = "NuevaTransaccionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);

        etDescripcion = findViewById(R.id.etDescripcion);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);  // Nuevo botón de cancelar

        // Poblamos el Spinner de categorías
        String[] categorias = {"Compras", "Hogar", "Transporte", "Ocio", "Comida"};
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categorias);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategorias);

        // Poblamos el Spinner de tipo (Ingreso o Gasto)
        String[] tipos = {"Ingreso", "Gasto"};
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipos);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipos);

        // Instancia única de la base de datos
        db = AppDatabase.getInstance(getApplicationContext());

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTransaccion();
            }
        });

        // Botón de cancelar: simplemente cierra la actividad sin guardar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NuevaTransaccionActivity.this, "Operación cancelada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void guardarTransaccion() {
        String descripcion = etDescripcion.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String fechaStr = etFecha.getText().toString().trim();
        String categoria = (String) spinnerCategoria.getSelectedItem();
        String tipo = (String) spinnerTipo.getSelectedItem();

        if (descripcion.isEmpty() || cantidadStr.isEmpty() || fechaStr.isEmpty() || categoria == null || tipo == null) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fecha;
        try {
            fecha = sdf.parse(fechaStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Error en el formato de fecha. Usa dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        Transaccion nuevaTransaccion = new Transaccion(descripcion, cantidad, fecha, categoria, tipo);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            long id = db.transaccionDao().insertTransaccion(nuevaTransaccion);
            Log.d(TAG, "Transacción insertada con ID: " + id);
            runOnUiThread(() -> {
                if (id != -1) {
                    Toast.makeText(NuevaTransaccionActivity.this, "Transacción guardada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NuevaTransaccionActivity.this, "Error al guardar la transacción", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
