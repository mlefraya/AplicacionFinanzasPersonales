package com.example.finanzaspersonalesnuevo.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finanzaspersonalesnuevo.Model.ObjetivoAhorro;
import com.example.finanzaspersonalesnuevo.R;
import com.example.finanzaspersonalesnuevo.ViewModel.ObjetivoAhorroViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_objetivo, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Recupera el Objetivo (implementa Serializable en tu modelo)
        if (getArguments() != null) {
            objetivo = (ObjetivoAhorro) getArguments().getSerializable(ARG_OBJETIVO);
        }
        viewModel = new ViewModelProvider(this).get(ObjetivoAhorroViewModel.class);

        TextInputEditText etDesc = view.findViewById(R.id.etDescObjetivo);
        TextInputEditText etMeta = view.findViewById(R.id.etMetaObjetivo);
        MaterialButton btnUpd  = view.findViewById(R.id.btnActualizarObjetivo);
        MaterialButton btnCancel = view.findViewById(R.id.btnCancelarObjetivo);

        if (objetivo != null) {
            etDesc.setText(objetivo.getDescripcion());
            etMeta.setText(String.format(Locale.getDefault(),"%.2f", objetivo.getMontoObjetivo()));
        }

        btnUpd.setOnClickListener(v -> {
            String d = etDesc.getText().toString().trim();
            String m = etMeta.getText().toString().trim();
            if (TextUtils.isEmpty(d) || TextUtils.isEmpty(m)) {
                Toast.makeText(requireContext(),
                        "Complete descripción y meta", Toast.LENGTH_SHORT).show();
                return;
            }
            double meta;
            try {
                meta = Double.parseDouble(m);
            } catch (NumberFormatException ex) {
                Toast.makeText(requireContext(),
                        "Meta inválida", Toast.LENGTH_SHORT).show();
                return;
            }
            objetivo.setDescripcion(d);
            objetivo.setMontoObjetivo(meta);
            viewModel.actualizar(objetivo);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}
