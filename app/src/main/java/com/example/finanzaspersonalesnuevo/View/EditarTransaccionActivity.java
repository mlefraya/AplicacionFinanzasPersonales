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
import androidx.lifecycle.ViewModelProvider;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.EditarTransaccionViewModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditarTransaccionActivity extends AppCompatActivity {

    private EditText etDescripcion, etCantidad, etFecha;
    private Spinner spinnerCategoria, spinnerTipo;
    private Button btnActualizar, btnCancelar;
    private EditarTransaccionViewModel viewModel;
    private static final String TAG = "EditarTransaccionActivity";
    private int transaccionId; // ID de la transacción a editar

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
        btnCancelar = findViewById(R.id.btnCancelar);  // Botón de cancelar

        // Poblamos el Spinner para tipo (Ingreso o Gasto)
        String[] tipos = {"Ingreso", "Gasto"};
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        // Obtenemos el ID de la transacción a editar
        transaccionId = getIntent().getIntExtra("transaccionId", -1);
        if (transaccionId == -1) {
            Toast.makeText(this, "Error: Transacción no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializamos el ViewModel
        viewModel = new ViewModelProvider(this).get(EditarTransaccionViewModel.class);

        // Observamos el LiveData para obtener los datos de la transacción
        viewModel.getTransaccionById(transaccionId).observe(this, transaccion -> {
            if (transaccion != null) {
                etDescripcion.setText(transaccion.getDescripcion());
                etCantidad.setText(String.valueOf(transaccion.getCantidad()));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                etFecha.setText(sdf.format(transaccion.getFecha()));

                // Cargar categorías según el tipo
                String tipo = transaccion.getTipo();
                if (tipo.equalsIgnoreCase("Ingreso")) {
                    String[] categoriasIngreso = {"Salario", "Inversión", "Regalo"};
                    ArrayAdapter<String> adapterIngreso = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasIngreso);
                    adapterIngreso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapterIngreso);
                } else if (tipo.equalsIgnoreCase("Gasto")) {
                    String[] categoriasGasto = {"Compras", "Hogar", "Transporte", "Ocio", "Comida"};
                    ArrayAdapter<String> adapterGasto = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasGasto);
                    adapterGasto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapterGasto);
                }

                // Seleccionar el tipo en el Spinner
                if (tipo.equalsIgnoreCase("Ingreso")) {
                    spinnerTipo.setSelection(0);
                } else if (tipo.equalsIgnoreCase("Gasto")) {
                    spinnerTipo.setSelection(1);
                }
            } else {
                Toast.makeText(EditarTransaccionActivity.this, "Error al cargar la transacción", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Botón para actualizar la transacción
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

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date nuevaFecha;
            try {
                nuevaFecha = sdf.parse(nuevaFechaStr);
            } catch (ParseException e) {
                Toast.makeText(EditarTransaccionActivity.this, "Error en el formato de fecha. Usa dd/MM/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }

            // Creamos la transacción actualizada con los datos ingresados
            Transaccion transaccionAEditar = new Transaccion(nuevaDescripcion, nuevaCantidad, nuevaFecha, nuevaCategoria, tipoTransaccion);
            // Para actualizar, es necesario asignar el mismo ID a la transacción
            transaccionAEditar.setId(transaccionId);

            // Llamamos al ViewModel para actualizar la transacción
            viewModel.actualizarTransaccion(transaccionAEditar);

            Toast.makeText(EditarTransaccionActivity.this, "Transacción actualizada", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Botón de cancelar: cierra la actividad sin guardar cambios
        btnCancelar.setOnClickListener(v -> {
            Toast.makeText(EditarTransaccionActivity.this, "Operación cancelada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
