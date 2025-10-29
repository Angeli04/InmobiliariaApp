package com.isma.inmobiliaria.ui.inmuebles;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentInmueblesBinding;
import com.isma.inmobiliaria.model.Inmueble;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private InmueblesViewModel vm;
    private FragmentInmueblesBinding binding;


    public static InmueblesFragment newInstance() {
        return new InmueblesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        vm.getInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                InmuebleAdapter ia = new InmuebleAdapter(inmuebles, getContext());
                binding.rvInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvInmuebles.setAdapter(ia);

            }

        });
        binding.fabAddInmueble.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Navegar a la pantalla de crear inmueble
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_inmueblesFragment_to_crearInmueble);
            }
        });
        vm.leerInmuebles();
        return binding.getRoot();
    }

}