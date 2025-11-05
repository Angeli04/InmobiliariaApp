package com.isma.inmobiliaria.ui.inquilinos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentInquilinosBinding;
import com.isma.inmobiliaria.model.InquilinoAlquilerDto;

import java.util.ArrayList;
import java.util.List;

public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mViewModel;
    private FragmentInquilinosBinding binding; // 3. Declarar el ViewBinding
    private InquilinosAdapter adapter; // 4. Declarar el Adaptador

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 5. Inflar la vista usando ViewBinding
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        // 6. Obtener el ViewModel
        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        return binding.getRoot(); // 7. Devolver la raíz del binding
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 8. Configurar el RecyclerView (la forma profesional)
        setupRecyclerView();

        // 9. Configurar los Observadores
        mViewModel.getInquilinos().observe(getViewLifecycleOwner(), new Observer<List<InquilinoAlquilerDto>>() {
            @Override
            public void onChanged(List<InquilinoAlquilerDto> inquilinos) {
                // Cuando los datos llegan, se actualiza el adaptador
                adapter.actualizarLista(inquilinos);
            }
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 10. Pedir los datos CADA VEZ que el fragmento se vuelve visible
        //     (Esto asegura que la lista esté actualizada si vienes de otro fragmento)
        mViewModel.cargarInquilinos();
    }

    private void setupRecyclerView() {
        // Configura el LayoutManager (lista vertical)
        binding.RecyclerViewInquilinos.setLayoutManager(new LinearLayoutManager(getContext()));


        // Crea el adaptador UNA SOLA VEZ con una lista vacía
        adapter = new InquilinosAdapter(new ArrayList<>(), getContext());

        // Asigna el adaptador al RecyclerView
        binding.RecyclerViewInquilinos.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Evita memory leaks con ViewBinding
    }
}