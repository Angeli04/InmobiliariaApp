package com.isma.inmobiliaria.ui.inmuebles;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentCrearInmuebleBinding;

public class CrearInmueble extends Fragment {

    private CrearInmuebleViewModel mv;
    private FragmentCrearInmuebleBinding binding;
    private Intent intent;
    private ActivityResultLauncher<Intent> arl;


    public static CrearInmueble newInstance() {
        return new CrearInmueble();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearInmuebleBinding.inflate(getLayoutInflater());
        mv = new ViewModelProvider(this).get(CrearInmuebleViewModel.class);
        abrirGaleria();
        binding.btnSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(intent);
            }
        });

        mv.getUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivInmueblePreview.setImageURI(uri);
                binding.ivInmueblePreview.setVisibility(View.VISIBLE);
            }
        });

        binding.btnGuardarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mv.cargarInmueble(binding.etDireccion.getText().toString(),
                        binding.etPrecio.getText().toString(),
                        binding.etAmbientes.getText().toString(),
                        binding.etSuperficie.getText().toString(),
                        binding.spTipoInmueble.getSelectedItem().toString(),
                        binding.etLatitud.getText().toString(),
                        binding.etLongitud.getText().toString(),
                        binding.swHabilitado.isChecked()
                        );
            }

        });

        return binding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mv = new ViewModelProvider(this).get(CrearInmuebleViewModel.class);
        // TODO: Use the ViewModel
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//Es para abrir la galeria
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.d("AgregarInmuebleFragment", "Result: " + result);
                mv.recibirFoto(result);
            }
        });
    }


}