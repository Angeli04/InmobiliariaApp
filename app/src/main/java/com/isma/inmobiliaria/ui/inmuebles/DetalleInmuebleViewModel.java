package com.isma.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isma.inmobiliaria.model.Inmueble;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private MutableLiveData<Inmueble> minmueble = new MutableLiveData<>();

    public LiveData<Inmueble> getMinmueble() {
        return minmueble;
    }

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public void traerInmueble(Bundle inmueble){

        Inmueble inm = (Inmueble) inmueble.getSerializable("inmueble");
        if(inm != null){
            minmueble.setValue(inm);
        }
    }
}