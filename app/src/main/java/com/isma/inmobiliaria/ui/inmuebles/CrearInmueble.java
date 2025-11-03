package com.isma.inmobiliaria.ui.inmuebles;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentCrearInmuebleBinding;
import com.isma.inmobiliaria.model.Inmueble;
import com.isma.inmobiliaria.model.TipoInmueble;

import java.util.Collections;
import java.util.List;

public class CrearInmueble extends Fragment {

    private CrearInmuebleViewModel mv;
    private FragmentCrearInmuebleBinding binding;
    private ProgressDialog progressDialog;

    private final ActivityResultLauncher<Intent> arl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // cuando el usuario elige una foto, esto se ejecuta
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mv.recibirFoto(result); // llama al ViewModel
                    }
                }
            });


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCrearInmuebleBinding.inflate(inflater, container, false);

        mv = new ViewModelProvider(this).get(CrearInmuebleViewModel.class);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Guardando inmueble...");
        progressDialog.setCancelable(false);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            arl.launch(intent); // Lanza el "receptor" de la galería
        });


        binding.btnGuardarInmueble.setOnClickListener(v -> {

            TipoInmueble tipoSeleccionado = (TipoInmueble) binding.spTipoInmueble.getSelectedItem();


            mv.cargarInmueble(
                    binding.etDireccion.getText().toString(),
                    binding.etPrecio.getText().toString(),
                    binding.etAmbientes.getText().toString(),
                    binding.etSuperficie.getText().toString(),
                    tipoSeleccionado, // Pasa el objeto TipoInmueble
                    binding.etLatitud.getText().toString(),
                    binding.etLongitud.getText().toString(),
                    true
            );
        });


        mv.getUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivInmueblePreview.setImageURI(uri);
                binding.ivInmueblePreview.setVisibility(View.VISIBLE);
            }
        });


        mv.getTipoInmueble().observe(getViewLifecycleOwner(), new Observer<List<TipoInmueble>>() {
            @Override
            public void onChanged(List<TipoInmueble> tipoInmuebles) {

                ArrayAdapter<TipoInmueble> adapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        tipoInmuebles
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spTipoInmueble.setAdapter(adapter);
            }
        });


        mv.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });


        mv.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null && !error.isEmpty()) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                }
            }
        });


        mv.getToast().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                if(mensaje != null && !mensaje.isEmpty()){
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });

        mv.getNavegacion().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navegar) {
                // Obedece la orden del ViewModel de navegar. llevar logica al view model.
                if(navegar != null && navegar){
                    NavController navController = Navigation.findNavController(view);
                    navController.navigateUp();
                }
            }
        });


        mv.traerTipos();
    }
}