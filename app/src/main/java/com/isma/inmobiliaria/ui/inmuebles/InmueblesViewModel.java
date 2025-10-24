
package com.isma.inmobiliaria.ui.inmuebles;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isma.inmobiliaria.model.Inmueble;
import com.isma.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mText = new MutableLiveData<>();
    private final MutableLiveData<List<Inmueble>> mInmuebles = new MutableLiveData<>();
    public InmueblesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Inmueble>> getInmuebles() {
        return mInmuebles;
    }


    public void leerInmuebles() {

        String token  = ApiClient.leerToken(getApplication());
        ApiClient.ApiService api = ApiClient.getApiService();
        Call<List<Inmueble>> llamada = api.listarInmuebles("Bearer " + token);


        llamada.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()) {
                    mInmuebles.postValue(response.body());
                }else{
                    Toast.makeText(getApplication(), "No hay inmuebles disponibles", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

}