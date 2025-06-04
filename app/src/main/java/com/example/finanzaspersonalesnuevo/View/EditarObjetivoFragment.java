package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.Utils.CurrencyConverter;
import com.example.finanzaspersonalesnuevo.Utils.PreferenceUtil;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditarObjetivoFragment extends Fragment {

    private static final String ARG_OBJETIVO = "arg_objetivo";
    private ObjetivoAhorro objetivo;
    private ObjetivoAhorroViewModel viewModel;

    /** Crea instancia con el Objetivo a editar */
    public static EditarObjetivoFragment newInstance(ObjetivoAhorro obj) {
        EditarObjetivoFragment f = new EditarObjetivoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_OBJETIVO, obj);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_objetivo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recupera el Objetivo (serializable) desde los argumentos
        if (getArguments() != null) {
            objetivo = (ObjetivoAhorro) getArguments().getSerializable(ARG_OBJETIVO);
        }
        viewModel = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);

        EditText etDesc       = view.findViewById(R.id.etDescObjetivo);
        EditText etMeta       = view.findViewById(R.id.etMetaObjetivo);
        EditText etPlazo      = view.findViewById(R.id.etPlazoObjetivo);
        Button btnUpd         = view.findViewById(R.id.btnActualizarObjetivo);
        Button btnCancel      = view.findViewById(R.id.btnCancelarObjetivo);
        ProgressBar progressBar = view.findViewById(R.id.progressBarObjetivo);

        // Carga valores en los campos
        if (objetivo != null) {
            etDesc.setText(objetivo.getDescripcion());

            // Convertir montoObjetivo (EUR) → moneda local
            String currencyCode = PreferenceUtil.getCurrency(requireContext());
            double metaEnEur    = objetivo.getMontoObjetivo();
            double metaLocal    = CurrencyConverter.fromEur(metaEnEur, currencyCode);
            etMeta.setText(String.format(Locale.getDefault(), "%.2f", metaLocal));

            // Dejamos el plazo en blanco para que el usuario lo ingrese de nuevo
            etPlazo.setText("");
        }

        btnUpd.setOnClickListener(v -> {
            String d = etDesc.getText().toString().trim();
            String m = etMeta.getText().toString().trim();
            String p = etPlazo.getText().toString().trim();

            if (TextUtils.isEmpty(d) || TextUtils.isEmpty(m) || TextUtils.isEmpty(p)) {
                Toast.makeText(requireContext(),
                        "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double metaLocal;
            int plazoMeses;
            try {
                metaLocal  = Double.parseDouble(m);
                plazoMeses = Integer.parseInt(p);
            } catch (NumberFormatException ex) {
                Toast.makeText(requireContext(),
                        "Valores inválidos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir meta local → EUR antes de guardar
            String currencyCode = PreferenceUtil.getCurrency(requireContext());
            double metaEnEur = CurrencyConverter.toEur(metaLocal, currencyCode);

            // Asegurar fechaInicio no sea null
            Date fechaInicio = objetivo.getFechaInicio();
            if (fechaInicio == null) {
                fechaInicio = new Date();
                objetivo.setFechaInicio(fechaInicio);
            }
            // Recalcular fechaFin = fechaInicio + plazoMeses
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaInicio);
            cal.add(Calendar.MONTH, plazoMeses);
            Date nuevaFechaFin = cal.getTime();

            objetivo.setDescripcion(d);
            objetivo.setMontoObjetivo(metaEnEur);
            objetivo.setFechaFin(nuevaFechaFin);

            progressBar.setVisibility(View.VISIBLE);
            viewModel.actualizar(objetivo);
            progressBar.setVisibility(View.GONE);

            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}
