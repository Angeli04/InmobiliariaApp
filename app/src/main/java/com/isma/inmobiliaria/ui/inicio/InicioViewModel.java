package com.isma.inmobiliaria.ui.inicio;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.isma.inmobiliaria.LoginActivityViewModel;

public class InicioViewModel extends AndroidViewModel {

    private final MutableLiveData<LoginActivityViewModel.Event<Boolean>> mIniciarLlamada = new MutableLiveData<>();

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<LoginActivityViewModel.Event<Boolean>> getIniciarLlamada() {
        return mIniciarLlamada;
    }


    public void onShakeDetectado() {

        Log.d("InicioViewModel", "Shake detectado. Ordenando a la Vista que llame.");
        mIniciarLlamada.postValue(new LoginActivityViewModel.Event<>(true));
    }


    public void onEventoDeLlamadaManejado() {

        Log.d("InicioViewModel", "La Vista manejó el evento de llamada.");
    }
}