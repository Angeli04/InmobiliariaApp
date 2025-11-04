package com.isma.inmobiliaria.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InicioViewModel extends AndroidViewModel {
    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    // 1. EL "ESTADO" INTERNO (Mutable)
    //    Es privado (MutableLiveData) porque solo el ViewModel debe poder modificarlo.
    //    Empieza en 'false' (no llamar).
    private final MutableLiveData<Boolean> _iniciarLlamada = new MutableLiveData<>(false);


    // 2. LA "VENTANA" PÚBLICA (Inmutable)
    //    El Fragment "observará" esto (LiveData).
    //    No puede modificarlo, solo leerlo. Es una buena práctica.
    public LiveData<Boolean> getIniciarLlamada() {
        return _iniciarLlamada;
    }


    // 3. LA ACCIÓN: El Fragment nos avisa que se sacudió
    //    Este es el método que llamaremos desde el Fragment.
    public void onShakeDetectado() {
        // "Disparamos" el evento poniendo el LiveData en 'true'.
        // Usamos postValue() que es seguro para ejecutar desde cualquier hilo.
        _iniciarLlamada.postValue(true);
    }


    // 4. EL RESET: El Fragment nos avisa que ya manejó el evento
    //    Esto es CRUCIAL para que el evento no se dispare múltiples veces.
    public void onEventoDeLlamadaManejado() {
        // Reseteamos el LiveData a 'false'.
        _iniciarLlamada.postValue(false);
    }
}