package com.isma.inmobiliaria;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.isma.inmobiliaria.model.TokenResponse;
import com.isma.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    // Los campos de LiveData deben ser privados
    private MutableLiveData<String> mMensaje = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // Getters simplificados
    public MutableLiveData<String> getError() {
        return mError;
    }

    public MutableLiveData<String> getMensaje() {
        return mMensaje;
    }

    public void iniciarSesion(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            mError.postValue("Por favor, complete todos los campos.");
            return;
        }

        ApiClient.ApiService api = ApiClient.getApiService();
        Call<TokenResponse> call = ApiClient.getApiService().login(email, password);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String tokenLimpio = response.body().getToken();
                    ApiClient.guardarToken(getApplication(), tokenLimpio);
                    Log.d("LOGIN_VM", "Token guardado exitosamente.");

                    mMensaje.postValue("Login exitoso");

                } else {
                    Log.d("LOGIN_VM", "Login no exitoso. Código: " + response.code());
                    mError.postValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                // Manejar fallo de conexión
                Log.e("LOGIN_VM", "Fallo en la llamada de login: " + t.getMessage());
            }
        });
    }
}
