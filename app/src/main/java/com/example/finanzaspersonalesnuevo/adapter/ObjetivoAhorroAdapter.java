package com.example.finanzaspersonalesnuevo.adapter;

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

    private List<ObjetivoAhorro> items = new ArrayList<>();
    private double balance = 0;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public void setItems(List<ObjetivoAhorro> list) {
        this.items = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

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
        h.txtMontoObjetivo.setText(String.format(Locale.getDefault(),
                "Meta: %.2f", o.getMontoObjetivo()));
        // progreso dinámico: porcentaje de balance sobre meta
        double pct = o.getMontoObjetivo() == 0
                ? 0
                : (balance / o.getMontoObjetivo()) * 100;
        h.progressBar.setProgress((int) Math.min(pct, 100));

        h.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClicked(o);
        });
        h.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClicked(o);
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
        TextView txtNombre, txtMontoObjetivo;
        ProgressBar progressBar;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre        = itemView.findViewById(R.id.txtNombreObjetivo);
            txtMontoObjetivo = itemView.findViewById(R.id.txtMontoObjetivo);
            progressBar      = itemView.findViewById(R.id.progresoAhorro);
        }
    }
}
