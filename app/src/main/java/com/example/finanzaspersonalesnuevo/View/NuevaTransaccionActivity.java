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
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NuevaTransaccionActivity extends AppCompatActivity {

    private EditText etDescripcion, etCantidad, etFecha;
    private Spinner spinnerTipo, spinnerCategoria;
    private Button btnGuardar, btnCancelar;
    private AppDatabase db;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);

        etDescripcion = findViewById(R.id.etDescripcion);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Configuramos el Spinner de tipo (Ingreso o Gasto)
        String[] tipos = {"Ingreso", "Gasto"};
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipos);

        // Configuramos el listener para spinnerTipo para cambiar dinámicamente las categorías
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Ingreso
                    String[] categoriasIngreso = {"Salario", "Inversión", "Regalo"};
                    ArrayAdapter<String> adapterIngreso = new ArrayAdapter<>(NuevaTransaccionActivity.this, android.R.layout.simple_spinner_item, categoriasIngreso);
                    adapterIngreso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapterIngreso);
                } else { // Gasto
                    String[] categoriasGasto = {"Compras", "Hogar", "Transporte", "Ocio", "Comida"};
                    ArrayAdapter<String> adapterGasto = new ArrayAdapter<>(NuevaTransaccionActivity.this, android.R.layout.simple_spinner_item, categoriasGasto);
                    adapterGasto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapterGasto);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        db = AppDatabase.getInstance(getApplicationContext());

        btnGuardar.setOnClickListener(v -> guardarTransaccion());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarTransaccion() {
        String descripcion = etDescripcion.getText().toString().trim();
        String cantidadStr = etCantidad.getText().toString().trim();
        String fechaStr = etFecha.getText().toString().trim();
        String tipo = (String) spinnerTipo.getSelectedItem();
        String categoria = (String) spinnerCategoria.getSelectedItem();

        if (descripcion.isEmpty() || cantidadStr.isEmpty() || fechaStr.isEmpty() || tipo == null || categoria == null) {
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

        Date fecha;
        try {
            fecha = sdf.parse(fechaStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Error en el formato de fecha. Usa dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return;
        }

        Transaccion nuevaTransaccion = new Transaccion(descripcion, cantidad, fecha, categoria, tipo);

        executor.execute(() -> {
            long id = db.transaccionDao().insertTransaccion(nuevaTransaccion);
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
