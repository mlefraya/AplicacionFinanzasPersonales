package com.example.finanzaspersonalesnuevo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ObjetivoAhorroAdapter extends RecyclerView.Adapter<ObjetivoAhorroAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(ObjetivoAhorro objetivo);
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(ObjetivoAhorro objetivo);
    }

    private final List<ObjetivoAhorro> items = new ArrayList<>();
    private double balance = 0.0;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;
    private final Context context;

    public ObjetivoAhorroAdapter(Context context) {
        this.context = context;
    }

    /** Actualiza la lista de objetivos */
    public void setItems(List<ObjetivoAhorro> list) {
        items.clear();
        if (list != null) {
            items.addAll(list);
        }
        notifyDataSetChanged();
    }

    /** Actualiza el balance general (ingresos − gastos) */
    public void setBalance(double bal) {
        this.balance = bal;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        clickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        longClickListener = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_objetivo_ahorro, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        ObjetivoAhorro o = items.get(pos);
        h.txtNombre.setText(o.getDescripcion());
        h.txtMontoObjetivo.setText(
                String.format(Locale.getDefault(), "Meta: %.2f €", o.getMontoObjetivo())
        );

        // Aseguramos que el máximo de la barra sea 100
        h.progressBar.setMax(100);

        // Cálculo correcto: porcentaje = (balance_actual / monto_objetivo) × 100
        double porcentaje = 0.0;
        if (o.getMontoObjetivo() > 0.0) {
            porcentaje = (balance / o.getMontoObjetivo()) * 100.0;
        }
        // Clamp entre 0 y 100
        int progreso = (int) Math.min(Math.max(porcentaje, 0.0), 100.0);
        h.progressBar.setProgress(progreso);

        // Pintamos sólo la porción de progreso en azul_700
        int azul = context.getColor(R.color.blue_700);
        h.progressBar.setProgressTintList(ColorStateList.valueOf(azul));

        h.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClicked(o);
            }
        });

        h.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Eliminar objetivo")
                        .setMessage("¿Seguro que deseas eliminar este objetivo de ahorro?")
                        .setPositiveButton("Sí", (dialog, which) ->
                                longClickListener.onItemLongClicked(o)
                        )
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView    txtNombre, txtMontoObjetivo;
        ProgressBar progressBar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre        = itemView.findViewById(R.id.txtNombreObjetivo);
            txtMontoObjetivo = itemView.findViewById(R.id.txtMontoObjetivo);
            progressBar      = itemView.findViewById(R.id.progresoAhorro);
        }
    }
}

