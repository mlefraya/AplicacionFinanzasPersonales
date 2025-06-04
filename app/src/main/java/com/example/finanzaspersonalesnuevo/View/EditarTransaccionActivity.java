package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.finanzaspersonalesnuevo.Utils.CurrencyConverter;
import com.example.finanzaspersonalesnuevo.Utils.PreferenceUtil;
import com.example.finanzaspersonalesnuevo.ViewModel.EditarTransaccionViewModel;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditarTransaccionActivity extends AppCompatActivity {

    private EditText etDescripcion, etPrecio, etFecha;
    private Spinner spinnerTipo, spinnerCategoria;
    private Button btnActualizar, btnCancelar;
    private EditarTransaccionViewModel viewModel;
    private Transaccion currentTransaccion;

    private final SimpleDateFormat sdf =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final int MIN_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private final String[] tipos       = {"Ingreso","Gasto"};
    private final String[] catsIngreso = {"Salario","Inversión","Regalo"};
    private final String[] catsGasto   = {"Compras","Hogar","Transporte","Ocio","Comida"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_transaccion);

        sdf.setLenient(false);

        etDescripcion   = findViewById(R.id.etDescripcion);
        etPrecio        = findViewById(R.id.etPrecio);
        etFecha         = findViewById(R.id.etFecha);
        spinnerTipo     = findViewById(R.id.spinnerTipo);
        spinnerCategoria= findViewById(R.id.spinnerCategoria);
        btnActualizar   = findViewById(R.id.btnActualizar);
        btnCancelar     = findViewById(R.id.btnCancelar);

        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tipos);
        adapterTipo.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);

        spinnerTipo.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(android.widget.AdapterView<?> parent,
                                                         View view,
                                                         int pos,
                                                         long id) {
                        actualizarCategorias(tipos[pos]);
                        if (currentTransaccion != null) {
                            seleccionarCategoria(currentTransaccion.getCategoria());
                        }
                    }
                    @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
                });

        int id = getIntent().getIntExtra("transaccionId", -1);
        if (id < 0) {
            Toast.makeText(this,"ID inválido",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this)
                .get(EditarTransaccionViewModel.class);
        viewModel.getTransaccionById(id)
                .observe(this, transaccion -> {
                    if (transaccion == null) {
                        Toast.makeText(this,"No se encontró transacción",Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    currentTransaccion = transaccion;
                    etDescripcion.setText(transaccion.getDescripcion());

                    // Convertimos la cantidad (EUR) a la moneda local para mostrar:
                    String currencyCode = PreferenceUtil.getCurrency(this);
                    double precioLocal = CurrencyConverter.fromEur(
                            transaccion.getCantidad(), currencyCode);
                    etPrecio.setText(
                            String.format(Locale.getDefault(),"%.2f", precioLocal));

                    etFecha.setText(sdf.format(transaccion.getFecha()));
                    spinnerTipo.setSelection(
                            transaccion.getTipo().equalsIgnoreCase("Ingreso") ? 0 : 1);
                });

        btnActualizar.setOnClickListener(v -> actualizarTransaccion());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void actualizarTransaccion() {
        String desc    = etDescripcion.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();
        String fechaStr = etFecha.getText().toString().trim();
        String tipo    = (String) spinnerTipo.getSelectedItem();
        String cat     = (String) spinnerCategoria.getSelectedItem();

        if (TextUtils.isEmpty(desc)
                || TextUtils.isEmpty(precioStr)
                || TextUtils.isEmpty(fechaStr)
                || tipo == null
                || cat == null) {
            Toast.makeText(this,"Completa todos los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double precioLocal;
        try {
            precioLocal = Double.parseDouble(precioStr);
        } catch(NumberFormatException ex) {
            Toast.makeText(this,"Precio inválido",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Date fecha;
        try {
            fecha = sdf.parse(fechaStr);
        } catch(ParseException ex) {
            Toast.makeText(this,
                    "Fecha inválida. Usa dd/MM/yyyy",
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

        // Convertimos de la moneda local a EUR antes de guardar:
        String currencyCode = PreferenceUtil.getCurrency(this);
        double cantidadEnEur = CurrencyConverter.toEur(precioLocal, currencyCode);

        currentTransaccion.setDescripcion(desc);
        currentTransaccion.setCantidad(cantidadEnEur);
        currentTransaccion.setFecha(fecha);
        currentTransaccion.setTipo(tipo);
        currentTransaccion.setCategoria(cat);
        viewModel.actualizarTransaccion(currentTransaccion);
        Toast.makeText(this,
                "Transacción actualizada",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void actualizarCategorias(String tipo) {
        String[] arr = tipo.equalsIgnoreCase("Ingreso")
                ? catsIngreso : catsGasto;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void seleccionarCategoria(String categoria) {
        for (int i = 0; i < spinnerCategoria.getAdapter().getCount(); i++) {
            if (spinnerCategoria.getAdapter().getItem(i)
                    .toString().equalsIgnoreCase(categoria)) {
                spinnerCategoria.setSelection(i);
                return;
            }
        }
    }
}
