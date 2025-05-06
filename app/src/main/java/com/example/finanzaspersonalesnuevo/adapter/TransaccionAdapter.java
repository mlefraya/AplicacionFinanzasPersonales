package com.example.finanzaspersonalesnuevo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.Utils.CurrencyConverter;
import com.example.finanzaspersonalesnuevo.Utils.PreferenceUtil;
import com.example.finanzaspersonalesnuevo.View.EditarTransaccionActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.ViewHolder> {

    private List<Transaccion> transacciones;
    private OnItemLongClickListener longClickListener;
    private SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public interface OnItemLongClickListener {
        void onItemLongClicked(Transaccion transaccion);
    }

    public TransaccionAdapter(List<Transaccion> transacciones, OnItemLongClickListener longClickListener) {
        this.transacciones = transacciones != null ? transacciones : new ArrayList<>();
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaccion t = transacciones.get(position);
        Context ctx = holder.itemView.getContext();

        // 1) Descripción y categoría
        holder.tvDescripcion.setText(t.getDescripcion());
        holder.tvCategoria.setText(t.getCategoria());

        // 2) Fecha
        if (t.getFecha() != null) {
            holder.tvFecha.setText(sdfDisplay.format(t.getFecha()));
        } else {
            holder.tvFecha.setText("N/D");
        }

        // 3) Obtener moneda y convertir cantidad
        String currencyCode = PreferenceUtil.getCurrency(ctx);
        double converted = CurrencyConverter.fromEur(t.getCantidad(), currencyCode);
        String symbol = getSymbolFor(currencyCode);

        // 4) Formatear y asignar
        holder.tvCantidad.setText(String.format(Locale.getDefault(), "%s %.2f", symbol, converted));

        // 5) Click para editar
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, EditarTransaccionActivity.class);
            intent.putExtra("transaccionId", t.getId());
            ctx.startActivity(intent);
        });

        // 6) Long click
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClicked(t);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    public void actualizarLista(List<Transaccion> nuevaLista) {
        if (nuevaLista != null) {
            transacciones.clear();
            transacciones.addAll(nuevaLista);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvCantidad, tvFecha, tvCategoria;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvCantidad    = itemView.findViewById(R.id.tvCantidad);
            tvFecha       = itemView.findViewById(R.id.tvFecha);
            tvCategoria   = itemView.findViewById(R.id.tvCategoria);
        }
    }

    // Método auxiliar para obtener el símbolo de la moneda
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
            case "ARS": return "$";    // podría ser "AR$"
            case "ZAR": return "R";
            default:    return "€";
        }
    }
}
