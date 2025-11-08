package com.isma.inmobiliaria.ui.inicio;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentInicioBinding;
import com.isma.inmobiliaria.utils.ShakeDetector;


public class InicioFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap miMapa;
    private InicioViewModel vm;
    private FragmentInicioBinding binding;

    private ActivityResultLauncher<String> requestPermissionLauncher;


    private ShakeDetector shakeDetector; // <<< REEMPLAZA A SensorManager

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d("InicioFragment", "Permiso CALL_PHONE CONCEDIDO.");
                        realizarLlamada();
                    } else {
                        Log.d("InicioFragment", "Permiso CALL_PHONE DENEGADO.");
                        Toast.makeText(getContext(), "Permiso de llamada denegado.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InicioViewModel.class);
        shakeDetector = new ShakeDetector(requireContext().getApplicationContext(), vm);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        vm.getIniciarLlamada().observe(getViewLifecycleOwner(), event -> {
            if (event.getContentIfNotHandled() != null) {
                Log.d("InicioFragment", "Evento de sacudida recibido del VM. Pidiendo permiso...");
                pedirPermisoYLlamar(); // Esto es Lógica de UI (permisos)
                vm.onEventoDeLlamadaManejado(); // Informa al VM (opcional)
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shakeDetector.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        shakeDetector.stop();
    }


    private void pedirPermisoYLlamar() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED) {
            Log.d("InicioFragment", "Permiso ya concedido. Llamando.");
            realizarLlamada();
        } else {
            Log.d("InicioFragment", "Permiso no concedido. Solicitando...");
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }


    private void realizarLlamada() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:9999")); // Número de emergencia

        try {
            startActivity(callIntent);
        } catch (SecurityException se) {
            Log.e("InicioFragment", "Error de seguridad al llamar.", se);
            Toast.makeText(getContext(), "Error de seguridad. Revisa los permisos.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("InicioFragment", "No se pudo iniciar la llamada.", e);
            Toast.makeText(getContext(), "No se encontró app para llamar.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        miMapa = googleMap;
        LatLng ulp = new LatLng(-33.148953, -66.3078767);
        miMapa.addMarker(new MarkerOptions()
                .position(ulp)
                .title("Marcador en San Luis"));
        miMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ulp, 15));
    }
}