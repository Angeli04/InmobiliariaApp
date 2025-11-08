package com.isma.inmobiliaria.ui.contratos;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isma.inmobiliaria.model.ContratoDetalleDto;
import com.isma.inmobiliaria.model.Pago;
import com.isma.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> mPagos;

    private MutableLiveData<String> merror;


    public PagosViewModel(@NonNull Application application) {
        super(application);
        mPagos = new MutableLiveData<>();
        merror = new MutableLiveData<>();
    }

    public LiveData<List<Pago>> getPagos() {
        return mPagos;
    }



    public LiveData<String> getError() {
        return merror;
    }

    public void cargarDatosIniciales(Bundle bundle){
        if(bundle == null){
            Log.d("PagosViewModel", "No hay datos en el bundle");
            merror.postValue("No hay datos");
            return;
        }

        int idContrato = bundle.getInt("idContrato");
        if(idContrato>0){
            cargarPagos(idContrato);
        }else{
            merror.postValue("No hay ID de contrato");
        }
    }

    private void cargarPagos(int idContrato) {

        String token = ApiClient.leerToken(getApplication());
        if(token.isEmpty()){
            merror.postValue("No hay token");
            return;
        }
        ApiClient.ApiService api = ApiClient.getApiService();
        Call<List<Pago>> call = api.pagosPorContrato("Bearer "+token, idContrato);

        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if(response.isSuccessful() && response.body() != null){
                    mPagos.postValue(response.body());
                }else {
                    merror.postValue("Error al cargar pagos");
                }
            };

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                Log.d("PagosViewModel", "Fallo al cargar pagos: " + t.getMessage());
                merror.postValue("Fallo de conexión: " + t.getMessage());
            }
        });
   }
}