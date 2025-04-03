package com.example.finanzaspersonalesnuevo.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.View.EditarTransaccionActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.ViewHolder> {

    private List<Transaccion> transacciones;
    private OnItemLongClickListener longClickListener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaccion transaccion = transacciones.get(position);
        holder.tvDescripcion.setText(transaccion.getDescripcion());
        holder.tvCantidad.setText(String.format(Locale.getDefault(), "%.2f€", transaccion.getCantidad()));

        if (transaccion.getFecha() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvFecha.setText(sdf.format(transaccion.getFecha()));
        } else {
            holder.tvFecha.setText("Fecha no disponible");
        }

        holder.tvCategoria.setText(transaccion.getCategoria());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditarTransaccionActivity.class);
            intent.putExtra("transaccionId", transaccion.getId());
            v.getContext().startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClicked(transaccion);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvCantidad, tvFecha, tvCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
        }
    }
}
