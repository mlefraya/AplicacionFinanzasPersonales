package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.Utils.CurrencyConverter;
import com.example.finanzaspersonalesnuevo.Utils.PreferenceUtil;
import com.example.finanzaspersonalesnuevo.ViewModel.TransaccionViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
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

    public ResumenFinancieroFragment() { /* Required empty constructor */ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen_financiero, container, false);

        tvTotalIngresos  = view.findViewById(R.id.tvTotalIngresos);
        tvTotalGastos    = view.findViewById(R.id.tvTotalGastos);
        tvBalanceTotal   = view.findViewById(R.id.tvBalanceTotal);
        pieChartIngresos = view.findViewById(R.id.pieChartIngresos);
        pieChartGastos   = view.findViewById(R.id.pieChartGastos);

        viewModel = new ViewModelProvider(this).get(TransaccionViewModel.class);
        viewModel.getTransaccionesLiveData().observe(getViewLifecycleOwner(),
                new Observer<List<Transaccion>>() {
                    @Override
                    public void onChanged(List<Transaccion> transacciones) {
                        actualizarVista(transacciones);
                    }
                });

        return view;
    }

    private void actualizarVista(List<Transaccion> transacciones) {
        String currencyCode = PreferenceUtil.getCurrency(requireContext());
        String symbol = getSymbolFor(currencyCode);

        double totalIngresos = 0, totalGastos = 0;
        Map<String, Float> ingresosPorCat = new HashMap<>();
        Map<String, Float> gastosPorCat   = new HashMap<>();
        String[] catsIngresos = {"Salario","Inversión","Regalo"};
        String[] catsGastos   = {"Compras","Hogar","Transporte","Ocio","Comida"};

        for (String c : catsIngresos) ingresosPorCat.put(c, 0f);
        for (String c : catsGastos)   gastosPorCat.put(c,   0f);

        for (Transaccion t : transacciones) {
            double amtConv = CurrencyConverter.fromEur(t.getCantidad(), currencyCode);
            if ("Ingreso".equalsIgnoreCase(t.getTipo())) {
                totalIngresos += amtConv;
                float prev = ingresosPorCat.getOrDefault(t.getCategoria(), 0f);
                ingresosPorCat.put(t.getCategoria(), prev + (float) amtConv);
            } else {
                totalGastos += amtConv;
                float prev = gastosPorCat.getOrDefault(t.getCategoria(), 0f);
                gastosPorCat.put(t.getCategoria(), prev + (float) amtConv);
            }
        }

        double balance = totalIngresos - totalGastos;

        tvTotalIngresos.setText(String.format(Locale.getDefault(),
                "Ingresos: %s %.2f", symbol, totalIngresos));
        tvTotalGastos.setText(String.format(Locale.getDefault(),
                "Gastos: %s %.2f", symbol, totalGastos));
        tvBalanceTotal.setText(String.format(Locale.getDefault(),
                "Balance: %s %.2f", symbol, balance));

        actualizarGrafico(pieChartIngresos, ingresosPorCat, "Ingresos", symbol);
        actualizarGrafico(pieChartGastos,   gastosPorCat,   "Gastos",   symbol);
    }

    private void actualizarGrafico(PieChart chart,
                                   Map<String, Float> datos,
                                   String etiqueta,
                                   String symbol) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> e : datos.entrySet()) {
            if (e.getValue() > 0) {
                entries.add(new PieEntry(e.getValue(), e.getKey()));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, etiqueta);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%s %.2f", symbol, value);
            }
        });

        chart.setData(data);
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.animateY(800);
        chart.invalidate();
    }

    private String getSymbolFor(String code) {
        switch (code) {
            case "USD": return "$";
            case "GBP": return "£";
            case "JPY": return "¥";
            case "CHF": return "CHF";
            case "CAD": return "CA$";
            case "AUD": return "AU$";
            case "CNY": return "¥";
            case "INR": return "₹";
            case "BRL": return "R$";
            case "RUB": return "₽";
            case "MXN": return "MX$";
            case "SGD": return "S$";
            case "NZD": return "NZ$";
            case "SEK": return "kr";
            case "NOK": return "kr";
            case "KRW": return "₩";
            case "ARS": return "$";
            case "ZAR": return "R";
            default:    return "€";
        }
    }
}
