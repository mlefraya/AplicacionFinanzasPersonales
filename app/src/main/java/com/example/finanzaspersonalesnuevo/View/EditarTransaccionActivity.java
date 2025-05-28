package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.EditarTransaccionViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EditarTransaccionActivity extends AppCompatActivity {

    private EditText etDescripcion, etCantidad, etFecha;
    private Spinner spinnerCategoria, spinnerTipo;
    private Button btnActualizar, btnCancelar;
    private EditarTransaccionViewModel viewModel;
    private int transaccionId;
    private Transaccion currentTransaccion;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private final String[] categoriasIngreso = {"Salario", "Inversión", "Regalo"};
    private final String[] categoriasGasto = {"Compras", "Hogar", "Transporte", "Ocio", "Comida"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_transaccion);

        etDescripcion = findViewById(R.id.etDescripcion);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Spinner Tipo (Ingreso/Gasto)
        String[] tipos = {"Ingreso", "Gasto"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        // Listener: cambiar las categorías al cambiar el tipo
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipoSeleccionado = tipos[position];
                actualizarCategorias(tipoSeleccionado);

                // Si ya se cargó la transacción, intentar mantener la categoría seleccionada
                if (currentTransaccion != null) {
                    seleccionarCategoria(currentTransaccion.getCategoria());
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        transaccionId = getIntent().getIntExtra("transaccionId", -1);
        if (transaccionId == -1) {
            Toast.makeText(this, "Error: Transacción no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this).get(EditarTransaccionViewModel.class);
        viewModel.getTransaccionById(transaccionId).observe(this, transaccion -> {
            if (transaccion != null) {
                currentTransaccion = transaccion;
                etDescripcion.setText(transaccion.getDescripcion());
                etCantidad.setText(String.valueOf(transaccion.getCantidad()));
                etFecha.setText(sdf.format(transaccion.getFecha()));

                // Establecer el tipo y categorías correctamente
                if (transaccion.getTipo().equalsIgnoreCase("Ingreso")) {
                    spinnerTipo.setSelection(0); // Ingreso
                } else {
                    spinnerTipo.setSelection(1); // Gasto
                }

                // Categorías se actualizan automáticamente por el listener del spinnerTipo
            } else {
                Toast.makeText(EditarTransaccionActivity.this, "Error al cargar la transacción", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnActualizar.setOnClickListener(v -> {
            String nuevaDescripcion = etDescripcion.getText().toString().trim();
            String nuevaCantidadStr = etCantidad.getText().toString().trim();
            String nuevaFechaStr = etFecha.getText().toString().trim();
            String nuevaCategoria = spinnerCategoria.getSelectedItem() != null ? spinnerCategoria.getSelectedItem().toString() : "";
            String tipoTransaccion = spinnerTipo.getSelectedItem() != null ? spinnerTipo.getSelectedItem().toString() : "";

            if (nuevaDescripcion.isEmpty() || nuevaCantidadStr.isEmpty() || nuevaFechaStr.isEmpty() || nuevaCategoria.isEmpty() || tipoTransaccion.isEmpty()) {
                Toast.makeText(EditarTransaccionActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double nuevaCantidad;
            try {
                nuevaCantidad = Double.parseDouble(nuevaCantidadStr);
            } catch (NumberFormatException e) {
                Toast.makeText(EditarTransaccionActivity.this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            Date nuevaFecha;
            try {
                nuevaFecha = sdf.parse(nuevaFechaStr);
            } catch (ParseException e) {
                Toast.makeText(EditarTransaccionActivity.this, "Error en el formato de fecha. Usa dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentTransaccion != null) {
                currentTransaccion.setDescripcion(nuevaDescripcion);
                currentTransaccion.setCantidad(nuevaCantidad);
                currentTransaccion.setFecha(nuevaFecha);
                currentTransaccion.setCategoria(nuevaCategoria);
                currentTransaccion.setTipo(tipoTransaccion);
                viewModel.actualizarTransaccion(currentTransaccion);
                Toast.makeText(EditarTransaccionActivity.this, "Transacción actualizada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnCancelar.setOnClickListener(v -> finish());
    }

    // Método para actualizar el spinner de categorías según el tipo
    private void actualizarCategorias(String tipo) {
        ArrayAdapter<String> adapter;
        if (tipo.equalsIgnoreCase("Ingreso")) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasIngreso);
        } else {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasGasto);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    // Método para seleccionar la categoría correcta si ya estaba asignada
    private void seleccionarCategoria(String categoria) {
        for (int i = 0; i < spinnerCategoria.getAdapter().getCount(); i++) {
            String item = (String) spinnerCategoria.getAdapter().getItem(i);
            if (item.equalsIgnoreCase(categoria)) {
                spinnerCategoria.setSelection(i);
                break;
            }
        }
    }
}
