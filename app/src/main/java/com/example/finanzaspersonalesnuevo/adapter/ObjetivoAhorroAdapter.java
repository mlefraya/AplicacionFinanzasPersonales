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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ObjetivoAhorroAdapter extends RecyclerView.Adapter<ObjetivoAhorroAdapter.ViewHolder> {
    private List<ObjetivoAhorro> listaObjetivos;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ObjetivoAhorroAdapter(List<ObjetivoAhorro> listaObjetivos) {
        this.listaObjetivos = listaObjetivos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_objetivo_ahorro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ObjetivoAhorro objetivo = listaObjetivos.get(position);
        holder.txtNombre.setText(objetivo.getDescripcion());
        holder.txtMontoObjetivo.setText(String.format(Locale.getDefault(), "Meta: %.2f€", objetivo.getMontoObjetivo()));
        holder.txtMontoActual.setText(String.format(Locale.getDefault(), "Ahorrado: %.2f€", objetivo.getMontoActual()));
        holder.txtFechaInicio.setText("Inicio: " + sdf.format(objetivo.getFechaInicio()));
        holder.txtFechaFin.setText("Fin: " + sdf.format(objetivo.getFechaFin()));
        holder.progressBar.setProgress((int) objetivo.getProgreso());
    }

    @Override
    public int getItemCount() {
        return listaObjetivos != null ? listaObjetivos.size() : 0;
    }

    public void actualizarLista(List<ObjetivoAhorro> nuevaLista) {
        this.listaObjetivos = nuevaLista;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtMontoObjetivo, txtMontoActual, txtFechaInicio, txtFechaFin;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreObjetivo);
            txtMontoObjetivo = itemView.findViewById(R.id.txtMontoObjetivo);
            txtMontoActual = itemView.findViewById(R.id.txtMontoActual);
            txtFechaInicio = itemView.findViewById(R.id.txtFechaInicio);
            txtFechaFin = itemView.findViewById(R.id.txtFechaFin);
            progressBar = itemView.findViewById(R.id.progresoAhorro);
        }
    }
}
