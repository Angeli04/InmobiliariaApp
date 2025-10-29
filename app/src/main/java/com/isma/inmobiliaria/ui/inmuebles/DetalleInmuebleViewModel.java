package com.isma.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isma.inmobiliaria.model.Inmueble;
import com.isma.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void cambiarEstado(boolean habilitado){

        Inmueble inmueble = minmueble.getValue();
        inmueble.setHabilitado(habilitado);


        String token = ApiClient.leerToken(getApplication());
        ApiClient.ApiService api = ApiClient.getApiService();
        Call<Inmueble> call = api.actualizarEstado("Bearer " + token, inmueble);
        call.enqueue( new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplication(), "Estado actualizado", Toast.LENGTH_SHORT).show();
                    Log.d("siii", "onResponse: "+ response.body());

                }else{
                    Toast.makeText(getApplication(), "Error al actualizar estado en el Succsessful", Toast.LENGTH_SHORT).show();
                    Log.d("DetalleInmuebleViewModel", response.toString());
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al actualizar estado en el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}