package com.isma.inmobiliaria.ui.contratos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentContratosBinding;
import com.isma.inmobiliaria.model.ContratoDetalleDto;

import java.util.ArrayList;
import java.util.List;

public class ContratosFragment extends Fragment {

    private ContratosViewModel mv;
    private FragmentContratosBinding binding;
    private ContratosAdapter adapter;



    private ContratosViewModel mViewModel;

    public static ContratosFragment newInstance() {
        return new ContratosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mv = new ViewModelProvider(this).get(ContratosViewModel.class);
        binding = FragmentContratosBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        mv.getContratos().observe(getViewLifecycleOwner(), new Observer<List<ContratoDetalleDto>>() {
            @Override
            public void onChanged(List<ContratoDetalleDto> contratoDetalleDtos) {
                adapter.actualizarLista(contratoDetalleDtos);
            }
        });

        mv.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mv.cargarContratos();
    }

    private void setupRecyclerView(){
        binding.ReyclerViewContratos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContratosAdapter(new ArrayList<>(), getContext());
        binding.ReyclerViewContratos.setAdapter(adapter);
    }


}