package com.example.finanzaspersonalesnuevo.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

        etDescripcion = findViewById(R.id.etDescripcion);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String descripcion = etDescripcion.getText().toString();
                double cantidad = Double.parseDouble(etCantidad.getText().toString());
                String fechaStr = etFecha.getText().toString();

                // Convertir la fecha de String a Date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = null;
                try {
                    fecha = sdf.parse(fechaStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String categoria = spinnerCategoria.getSelectedItem().toString();

                // Crear una nueva transacción
                Transaccion nuevaTransaccion = new Transaccion(descripcion, cantidad, fecha, categoria);

                // Guardar la transacción (esto se hará en la base de datos en el futuro)

                finish(); // Cerrar la actividad
            }
        });
    }
}
