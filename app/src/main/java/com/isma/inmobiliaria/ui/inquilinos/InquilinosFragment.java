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
    private FragmentInquilinosBinding binding;
    private InquilinosAdapter adapter;

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

        mViewModel.getInquilinos().observe(getViewLifecycleOwner(), new Observer<List<InquilinoAlquilerDto>>() {
            @Override
            public void onChanged(List<InquilinoAlquilerDto> inquilinos) {
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
        mViewModel.cargarInquilinos();
    }

    private void setupRecyclerView() {
        binding.RecyclerViewInquilinos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InquilinosAdapter(new ArrayList<>(), getContext());
        binding.RecyclerViewInquilinos.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}