package com.isma.inmobiliaria.ui.inquilinos;
import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.isma.inmobiliaria.model.InquilinoAlquilerDto;
import com.isma.inmobiliaria.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<InquilinoAlquilerDto>> mInquilinos;

    private final MutableLiveData<String> mError;

    public InquilinosViewModel(@NonNull Application application) {
        super(application);
        mInquilinos = new MutableLiveData<>();
        mError = new MutableLiveData<>();
    }

    public LiveData<List<InquilinoAlquilerDto>> getInquilinos() {
        return mInquilinos;
    }

    public LiveData<String> getError() {
        return mError;
    }

    public void cargarInquilinos() {

        String token = "Bearer " + ApiClient.leerToken(getApplication());
        Log.d("TOKEN_CHECK", "Token que se va a usar para 'cargarInquilinos': " + token);

        ApiClient.ApiService api = ApiClient.getApiService();

        Call<List<InquilinoAlquilerDto>> llamada = api.misInquilinos(token);

        llamada.enqueue(new Callback<List<InquilinoAlquilerDto>>() {

            @Override
            public void onResponse(Call<List<InquilinoAlquilerDto>> call, Response<List<InquilinoAlquilerDto>> response) {
                if (response.isSuccessful()) {
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