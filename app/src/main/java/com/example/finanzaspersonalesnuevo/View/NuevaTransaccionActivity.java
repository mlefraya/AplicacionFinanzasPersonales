package com.example.finanzaspersonalesnuevo.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NuevaTransaccionActivity extends Activity {

    private EditText etDescripcion, etCantidad, etFecha;
    private Spinner spinnerCategoria;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_transaccion);

        // Vincular vistas del XML
        etDescripcion = findViewById(R.id.etDescripcion);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Forzar teclado para que acepte solo números y el punto como decimal
        etCantidad.setKeyListener(android.text.method.DigitsKeyListener.getInstance("0123456789."));

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener datos ingresados por el usuario
                String descripcion = etDescripcion.getText().toString().trim();
                String cantidadStr = etCantidad.getText().toString().trim();
                String fechaStr = etFecha.getText().toString().trim();
                String categoria = spinnerCategoria.getSelectedItem().toString();

                // Validar descripción
                if (descripcion.isEmpty()) {
                    etDescripcion.setError("La descripción no puede estar vacía");
                    return;
                }

                // Validar cantidad
                double cantidad = 0;
                if (cantidadStr.isEmpty() || !cantidadStr.matches("\\d+(\\.\\d+)?")) {
                    etCantidad.setError("Introduce una cantidad válida");
                    return;
                } else {
                    cantidad = Double.parseDouble(cantidadStr);
                }

                // Validar fecha
                Date fecha = null;
                if (fechaStr.isEmpty()) {
                    etFecha.setError("La fecha no puede estar vacía");
                    return;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        fecha = sdf.parse(fechaStr);
                    } catch (Exception e) {
                        etFecha.setError("Introduce una fecha válida (dd/MM/yyyy)");
                        return;
                    }
                }

                // Crear una nueva transacción
                Transaccion nuevaTransaccion = new Transaccion(descripcion, cantidad, fecha, categoria);

                // TODO: Guardar la transacción en la base de datos
                Toast.makeText(NuevaTransaccionActivity.this, "Transacción guardada", Toast.LENGTH_SHORT).show();

                // Cerrar la actividad y volver
                finish();
            }
        });
    }
}
