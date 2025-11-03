package com.isma.inmobiliaria.ui.inmuebles;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentDetalleInmuebleBinding;

public class DetalleInmueble extends Fragment {

    private DetalleInmuebleViewModel mViewModel;
    private FragmentDetalleInmuebleBinding binding;

    public static DetalleInmueble newInstance() {
        return new DetalleInmueble();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        mViewModel.getMinmueble().observe(getViewLifecycleOwner(), inmueble -> {
            binding.tvId.setText(inmueble.getIdInmuebles() + "");
            binding.tvValor.setText(inmueble.getPrecio() + "");
            binding.tvUso.setText(inmueble.getTipoInmueble()+"");
            binding.tvDireccionDetalle.setText(inmueble.getDireccion());
            binding.tvAmbientes.setText(inmueble.getAmbientes() + "");
            binding.tvLatitud.setText(inmueble.getLatitud() + "");
            binding.tvLongitud.setText(inmueble.getLongitud() + "");
            binding.checkBoxDisponible.setChecked(inmueble.isHabilitado());
            Glide.with(getContext())
                    .load( "http://192.168.0.7:5164" + inmueble.getImagenUrl())
                    .placeholder(R.drawable.ic_no_image)
                    .error("null")
                    .into(binding.imagenDetalle);
        });

        binding.checkBoxDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = binding.checkBoxDisponible.isChecked();
                mViewModel.cambiarEstado(check);
            }
        });

        mViewModel.traerInmueble(getArguments());

        return binding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);
    }

}