package com.isma.inmobiliaria.ui.inicio;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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


public class InicioFragment extends Fragment implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap miMapa;
    private InicioViewModel vm ;

    private SensorManager sensorManager;
    private Sensor acelerometro;

    // <<< AÑADIDO: Constantes para la detección de sacudida >>>
    private static final float ALPHA = 0.8f; // Factor de suavizado
    private static final int SHAKE_THRESHOLD_GRAVITY = 12; // Umbral de fuerza (m/s^2)
    private static final int SHAKE_COOLDOWN_MS = 1000; // Tiempo de espera (1 seg)

    // <<< AÑADIDO: Arrays para guardar los datos del sensor >>>
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private long lastShakeTime = 0;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos el launcher.
        // Define qué hacer cuando el usuario responde al pop-up de permiso.
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), // El tipo de permiso que pedimos (uno solo)
                isGranted -> { // 'isGranted' será true si el usuario aceptó, false si denegó
                    if (isGranted) {
                        // El usuario acaba de conceder el permiso.
                        Log.d("InicioFragment", "Permiso CALL_PHONE CONCEDIDO.");
                        // Ahora sí, realizamos la llamada.
                        realizarLlamada();
                    } else {
                        // El usuario denegó el permiso.
                        Log.d("InicioFragment", "Permiso CALL_PHONE DENEGADO.");
                        // Informamos al usuario que la función no está disponible.
                        Toast.makeText(getContext(), "Permiso de llamada denegado. No se puede llamar.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    // <<< FIN DE LO AÑADIDO >>>

    private FragmentInicioBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        // Inflamos el layout que nos pasaste, que ya contiene el mapa.
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

        vm = new ViewModelProvider(this).get(InicioViewModel.class);

        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }



        vm.getIniciarLlamada().observe(getViewLifecycleOwner(), debeLlamar -> {
            // ESTA LÍNEA ES LA QUE FALTA:
            if (debeLlamar != null && debeLlamar) {
                Log.d("InicioFragment", "Evento de sacudida recibido (debeLlamar=true)");
                pedirPermisoYLlamar();
                vm.onEventoDeLlamadaManejado();
            } else {
                // Opcional: verás que esto se registra cuando el valor es 'false'
                Log.d("InicioFragment", "Evento de sacudida ignorado (debeLlamar=false)");
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        // Empezamos a escuchar SÓLO si el sensor existe
        // Usamos SENSOR_DELAY_NORMAL para un equilibrio entre velocidad y batería.
        if (acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("ShakeDetector", "Listener del acelerómetro REGISTRADO");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // ¡CRUCIAL! Dejamos de escuchar para ahorrar batería
        sensorManager.unregisterListener(this);
        Log.d("ShakeDetector", "Listener del acelerómetro DEREGISTRADO");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario para este caso, lo dejamos vacío.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Esta es la lógica del "timbre" que discutimos.

        // Solo reaccionamos si el evento es del acelerómetro
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // 1. Aislar la gravedad con un filtro (suavizado exponencial)
            gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
            gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
            gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

            // 2. Obtener la aceleración lineal (movimiento real) restando la gravedad
            linearAcceleration[0] = event.values[0] - gravity[0];
            linearAcceleration[1] = event.values[1] - gravity[1];
            linearAcceleration[2] = event.values[2] - gravity[2];

            // 3. Calcular la magnitud (fuerza) de la aceleración
            float accel = (float) Math.sqrt(
                    linearAcceleration[0] * linearAcceleration[0] +
                            linearAcceleration[1] * linearAcceleration[1] +
                            linearAcceleration[2] * linearAcceleration[2]
            );

            // 4. Comprobar el umbral y el cooldown (para evitar múltiples llamadas)
            long currentTime = System.currentTimeMillis();
            if (accel > SHAKE_THRESHOLD_GRAVITY && (currentTime - lastShakeTime) > SHAKE_COOLDOWN_MS) {
                lastShakeTime = currentTime;

                // ¡SACUDIDA DETECTADA!
                Log.d("ShakeDetector", "¡SACUDIDA FÍSICA DETECTADA! Notificando al ViewModel...");

                // 5. Notificar al ViewModel (¡La conexión MVVM!)
                vm.onShakeDetectado();
            }
        }
    }
    private void pedirPermisoYLlamar() {
        // Comprobamos si el permiso (del Manifest) ya fue concedido
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED) {

            // --- Caso 1: YA TENEMOS PERMISO ---
            Log.d("InicioFragment", "Permiso ya concedido. Llamando directamente.");
            realizarLlamada();

        } else {

            // --- Caso 2: NO TENEMOS PERMISO ---
            Log.d("InicioFragment", "Permiso no concedido. Solicitando...");
            // Lanzamos el pop-up de solicitud de permiso.
            // El resultado será manejado por el 'requestPermissionLauncher' que definimos en onCreate.
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }

    /**
     * Este método solo se debe llamar DESPUÉS de confirmar que tenemos permiso.
     * Crea el Intent y realiza la llamada al 9999.
     */
    private void realizarLlamada() {
        // Creamos el Intent con la acción de LLAMAR (ACTION_CALL)
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // Establecemos el número. "tel:" es el prefijo obligatorio.
        callIntent.setData(Uri.parse("tel:9999"));

        try {
            // ¡Iniciamos la llamada!
            startActivity(callIntent);
        } catch (SecurityException se) {
            // Esto *no debería* pasar si 'checkSelfPermission' funcionó,
            // pero es una buena práctica de seguridad.
            Log.e("InicioFragment", "Error de seguridad al intentar llamar.", se);
            Toast.makeText(getContext(), "Error de seguridad. Revisa los permisos.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Captura genérica (ej. ActivityNotFoundException si no hay app de teléfono)
            Log.e("InicioFragment", "No se pudo iniciar la llamada.", e);
            Toast.makeText(getContext(), "No se encontró una aplicación para llamar.", Toast.LENGTH_SHORT).show();
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