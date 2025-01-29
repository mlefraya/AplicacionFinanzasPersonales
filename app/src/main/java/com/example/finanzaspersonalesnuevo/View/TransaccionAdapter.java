package com.example.finanzaspersonalesnuevo.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finanzaspersonalesnuevo.Model.Transaccion;
import com.example.finanzaspersonalesnuevo.R;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransaccionAdapter extends RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder> {
    private List<Transaccion> transacciones;

    public TransaccionAdapter(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    @Override
    public TransaccionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
        return new TransaccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransaccionViewHolder holder, int position) {
        Transaccion transaccion = transacciones.get(position);

        // Mostrar descripción
        holder.tvDescripcion.setText(transaccion.getDescripcion());

        // Mostrar cantidad con formato
        holder.tvCantidad.setText(String.format("%.2f €", transaccion.getCantidad()));

        // Formatear la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvFecha.setText(sdf.format(transaccion.getFecha()));

        // Mostrar categoría
        holder.tvCategoria.setText(transaccion.getCategoria());
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    // Método para actualizar la lista de transacciones y notificar al RecyclerView
    public void actualizarLista(List<Transaccion> nuevasTransacciones) {
        this.transacciones = nuevasTransacciones;
        notifyDataSetChanged(); // Notifica que los datos han cambiado
    }

    public static class TransaccionViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion, tvCantidad, tvFecha, tvCategoria;

        public TransaccionViewHolder(View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
        }
    }
}
