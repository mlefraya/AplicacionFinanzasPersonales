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

    private TextView tvIngresos, tvGastos, tvBalanceTotal;
    private PieChart pieChartIngresos, pieChartGastos;
    private TransaccionViewModel viewModel;

    public ResumenFinancieroFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen_financiero, container, false);

        tvIngresos = view.findViewById(R.id.tvIngresos);
        tvGastos = view.findViewById(R.id.tvGastos);
        tvBalanceTotal = view.findViewById(R.id.tvBalanceTotal);
        pieChartIngresos = view.findViewById(R.id.pieChartIngresos);
        pieChartGastos = view.findViewById(R.id.pieChartGastos);

        viewModel = new ViewModelProvider(this).get(TransaccionViewModel.class);
        viewModel.getTransaccionesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Transaccion>>() {
            @Override
            public void onChanged(List<Transaccion> transacciones) {
                double ingresos = 0.0;
                double gastos = 0.0;

                // Mapas para acumular totales por categorías
                Map<String, Float> ingresosPorCategoria = new HashMap<>();
                Map<String, Float> gastosPorCategoria = new HashMap<>();

                // Categorías predefinidas para ingresos y gastos
                String[] categoriasIngreso = {"Salario", "Inversión", "Regalo"};
                String[] categoriasGasto = {"Compras", "Hogar", "Transporte", "Ocio", "Comida"};

                // Inicializamos los mapas con 0
                for (String cat : categoriasIngreso) {
                    ingresosPorCategoria.put(cat, 0f);
                }
                for (String cat : categoriasGasto) {
                    gastosPorCategoria.put(cat, 0f);
                }

                // Acumulamos totales y sumas por categoría
                for (Transaccion t : transacciones) {
                    if (t.getTipo().equalsIgnoreCase("Ingreso")) {
                        ingresos += t.getCantidad();
                        String cat = t.getCategoria();
                        if (ingresosPorCategoria.containsKey(cat)) {
                            ingresosPorCategoria.put(cat, ingresosPorCategoria.get(cat) + (float)t.getCantidad());
                        } else {
                            // Si la categoría no está predefinida, la añade
                            ingresosPorCategoria.put(cat, (float)t.getCantidad());
                        }
                    } else if (t.getTipo().equalsIgnoreCase("Gasto")) {
                        gastos += t.getCantidad();
                        String cat = t.getCategoria();
                        if (gastosPorCategoria.containsKey(cat)) {
                            gastosPorCategoria.put(cat, gastosPorCategoria.get(cat) + (float)t.getCantidad());
                        } else {
                            // Si la categoría no está predefinida, la añade
                            gastosPorCategoria.put(cat, (float)t.getCantidad());
                        }
                    }
                }

                double balance = ingresos - gastos;
                tvIngresos.setText(String.format(Locale.getDefault(), "Ingresos: %.2f€", ingresos));
                tvGastos.setText(String.format(Locale.getDefault(), "Gastos: %.2f€", gastos));
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
            if (entry.getValue() > 0) { // Sólo añadimos categorías con valores positivos
                entradas.add(new PieEntry(entry.getValue(), entry.getKey()));
            }
        }
        PieDataSet dataSet = new PieDataSet(entradas, label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        // Opcional: Configurar descripción y animación
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);

        pieChart.invalidate();
    }
}
