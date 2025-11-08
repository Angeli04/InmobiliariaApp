package com.isma.inmobiliaria.ui.contratos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isma.inmobiliaria.model.ContratoDetalleDto;
import com.isma.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {

    private MutableLiveData<List<ContratoDetalleDto>> mContratos;

    private MutableLiveData<String> merror;

    public LiveData<List<ContratoDetalleDto>> getContratos() {
        return mContratos;
    }

    public LiveData<String> getError() {
         return merror;
    }

    public ContratosViewModel(@NonNull Application application) {
        super(application);
        mContratos = new MutableLiveData<>();
        merror = new MutableLiveData<>();
    }

    public void cargarContratos(){
        String token= ApiClient.leerToken(getApplication());
        if (token == null || token.isEmpty()){
            merror.postValue("No hay token");
            return;
        }
        ApiClient.ApiService api = ApiClient.getApiService();
        Call<List<ContratoDetalleDto>> call = api.contratosVigentes("Bearer "+token);

        call.enqueue(new Callback<List<ContratoDetalleDto>>() {
            @Override
            public void onResponse(Call<List<ContratoDetalleDto>> call, Response<List<ContratoDetalleDto>> response) {
                if(response.isSuccessful()){
                    mContratos.postValue(response.body());
                }
                else{
                    Log.d("ContratosViewModel", "Error al cargar contratos: " + response.code());
                    merror.postValue("Error al cargar contratos: " + response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<List<ContratoDetalleDto>> call, Throwable t) {
                Log.d("ContratosViewModel", "Error al cargar contratos: " + t.getMessage());
                merror.postValue("Error al cargar contratos: " + t.getMessage());
            }
        });

    }


}