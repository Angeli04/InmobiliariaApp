package com.isma.inmobiliaria.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isma.inmobiliaria.R;



//TODO, cambiar a viewModel
// 1. Implementamos OnMapReadyCallback para saber cuándo está listo el mapa.
public class InicioFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap miMapa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos el layout que nos pasaste, que ya contiene el mapa.
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 2. Obtenemos el SupportMapFragment. Es importante usar getChildFragmentManager()
        //    cuando estamos dentro de otro Fragment. [cite: 59, 70]
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        // 3. Solicitamos el mapa de forma asíncrona. Cuando esté listo, se llamará a onMapReady.
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Este metodo se ejecuta cuando el mapa está completamente cargado y listo para ser usado.
     * Aquí es donde realizamos todas las operaciones como añadir marcadores, mover la cámara, etc.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        miMapa = googleMap;

        // 4. Definimos las coordenadas del punto que queremos mostrar.
        //    Usamos las mismas coordenadas para SANLUIS que en tu presentación. [cite: 63]
        LatLng ulp = new LatLng(-33.148953, -66.3078767);

        // 5. Añadimos un marcador en la posición definida.
        //    El método addMarker recibe un MarkerOptions con la posición y el título. [cite: 78, 102]
        miMapa.addMarker(new MarkerOptions()
                .position(ulp)
                .title("Marcador en San Luis"));

        // 6. Movemos la cámara al punto que hemos marcado.
        //    Usamos CameraUpdateFactory para crear la "instrucción" de movimiento
        //    y luego la ejecutamos con moveCamera o animateCamera. [cite: 85, 95]
        //    Un zoom de 15 es un buen nivel para ver una ciudad.
        miMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(ulp, 15));

        // Opcional: Si quieres una animación como en la presentación, usa animateCamera: [cite: 63, 95]
        // miMapa.animateCamera(CameraUpdateFactory.newLatLngZoom(sanLuis, 15));
    }
}