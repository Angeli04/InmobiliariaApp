package com.isma.inmobiliaria.ui.inquilinos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isma.inmobiliaria.model.InquilinoAlquilerDto;
import com.isma.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {

    // LiveData para la lista de inquilinos (DTOs)
    private final MutableLiveData<List<InquilinoAlquilerDto>> mInquilinos;

    // LiveData para manejar errores
    private final MutableLiveData<String> mError;

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
        mInquilinos = new MutableLiveData<>();
        mError = new MutableLiveData<>();
    }

    // Getters para que el Fragment (Vista) observe
    public LiveData<List<InquilinoAlquilerDto>> getInquilinos() {
        return mInquilinos;
    }

    public LiveData<String> getError() {
        return mError;
    }

    /**
     * Llama a la API (endpoint: /api/Inquilino/mis-inquilinos)
     * para obtener la lista de inquilinos del propietario logueado.
     */
    public void cargarInquilinos() {
        // 1. Obtener el token guardado
        String token = "Bearer " + ApiClient.leerToken(getApplication());
        Log.d("TOKEN_CHECK", "Token que se va a usar para 'cargarInquilinos': " + token);


        // 2. Obtener el servicio de la API (usando el ApiClient.java actualizado)
        ApiClient.ApiService api = ApiClient.getApiService();

        // 3. Llamar al método que definimos en ApiClient.java
        Call<List<InquilinoAlquilerDto>> llamada = api.misInquilinos(token);

        // 4. Ejecutar la llamada de forma asíncrona
        llamada.enqueue(new Callback<List<InquilinoAlquilerDto>>() {

            @Override
            public void onResponse(Call<List<InquilinoAlquilerDto>> call, Response<List<InquilinoAlquilerDto>> response) {
                if (response.isSuccessful()) {
                    // 5. Éxito: Postear la lista al LiveData
                    mInquilinos.postValue(response.body());
                } else {
                    Log.d("InquilinosViewModel", "Error al cargar inquilinos: " + response.code());
                    mError.postValue("No se pudieron cargar los inquilinos (Error " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<InquilinoAlquilerDto>> call, @NonNull Throwable t) {
                Log.d("InquilinosViewModel", "Fallo al cargar inquilinos: " + t.getMessage());
                mError.postValue("Fallo de conexión: " + t.getMessage());
            }
        });
    }
}