package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.TransaccionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ResumenFinancieroFragment extends Fragment {

    private TextView tvTotalIngresos, tvTotalGastos, tvBalanceTotal;
    private PieChart pieChartIngresos, pieChartGastos;
    private TransaccionViewModel viewModel;

    public ResumenFinancieroFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen_financiero, container, false);

        // Referencias a las vistas del XML
        tvTotalIngresos = view.findViewById(R.id.tvTotalIngresos);
        tvTotalGastos = view.findViewById(R.id.tvTotalGastos);
        tvBalanceTotal = view.findViewById(R.id.tvBalanceTotal);
        pieChartIngresos = view.findViewById(R.id.pieChartIngresos);
        pieChartGastos = view.findViewById(R.id.pieChartGastos);

        // Inicializamos el ViewModel
        viewModel = new ViewModelProvider(this).get(TransaccionViewModel.class);
        viewModel.getTransaccionesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transacciones) {
                double ingresos = 0.0;
                double gastos = 0.0;
                // Utilizamos mapas para acumular los totales por categoría (manteniendo la lógica que ya usas)
                Map<String, Float> ingresosPorCategoria = new HashMap<>();
                Map<String, Float> gastosPorCategoria = new HashMap<>();

                // Categorías predefinidas (puedes modificar o extender esta lista según convenga)
                String[] categoriasIngreso = {"Salario", "Inversión", "Regalo"};
                String[] categoriasGasto = {"Compras", "Hogar", "Transporte", "Ocio", "Comida"};

                // Inicializamos los mapas
                for (String cat : categoriasIngreso) {
                    ingresosPorCategoria.put(cat, 0f);
                }
                for (String cat : categoriasGasto) {
                    gastosPorCategoria.put(cat, 0f);
                }

                // Acumulamos totales y actualizamos los mapas según la categoría de cada transacción
                for (Transaccion t : transacciones) {
                    if (t.getTipo().equalsIgnoreCase("Ingreso")) {
                        ingresos += t.getCantidad();
                        String cat = t.getCategoria();
                        float total = ingresosPorCategoria.containsKey(cat) ? ingresosPorCategoria.get(cat) : 0f;
                        ingresosPorCategoria.put(cat, total + (float)t.getCantidad());
                    } else if (t.getTipo().equalsIgnoreCase("Gasto")) {
                        gastos += t.getCantidad();
                        String cat = t.getCategoria();
                        float total = gastosPorCategoria.containsKey(cat) ? gastosPorCategoria.get(cat) : 0f;
                        gastosPorCategoria.put(cat, total + (float)t.getCantidad());
                    }
                }

                double balance = ingresos - gastos;
                tvTotalIngresos.setText(String.format(Locale.getDefault(), "Total Ingresos: %.2f€", ingresos));
                tvTotalGastos.setText(String.format(Locale.getDefault(), "Total Gastos: %.2f€", gastos));
                tvBalanceTotal.setText(String.format(Locale.getDefault(), "Balance: %.2f€", balance));

                // Actualizamos los gráficos
                actualizarGrafico(pieChartIngresos, ingresosPorCategoria, "Ingresos");
                actualizarGrafico(pieChartGastos, gastosPorCategoria, "Gastos");
            }
        });

        return view;
    }

    private void actualizarGrafico(PieChart pieChart, Map<String, Float> datos, String label) {
        List<PieEntry> entradas = new ArrayList<>();
        for (Map.Entry<String, Float> entry : datos.entrySet()) {
            if (entry.getValue() > 0) { // Solo mostramos categorías con valor positivo
                entradas.add(new PieEntry(entry.getValue(), entry.getKey()));
            }
        }
        PieDataSet dataSet = new PieDataSet(entradas, label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        // Opcional: configurar la animación y desactivar la descripción
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}
