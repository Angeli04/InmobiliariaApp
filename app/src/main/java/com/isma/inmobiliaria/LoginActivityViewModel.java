package com.isma.inmobiliaria;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.isma.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private String usuario = "isma";
    private String contraseña = "1234";

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    MutableLiveData<String> mMensaje = new MutableLiveData<>();
    MutableLiveData<String> mError = new MutableLiveData<>();

    public MutableLiveData<String> getError() {
        if (mError == null) {
            mError = new MutableLiveData<>();
        }
        return mError;
    }

    public MutableLiveData<String> getMensaje() {
        if (mMensaje == null) {
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

/*    public void iniciarSesion(String usuario, String contraseña) {
        ApiClient.ApiService api = ApiClient.getApiService();
        Call<String> llamada = api.login(usuario, contraseña);
        llamada.enqueue(new Callback<String>(){

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    String token = response.body();
                    ApiClient.guardarToken(getApplication(), token);
                    mMensaje.setValue("Bienvenido");
                }else{
                    String errorMsg = "Error: " + response.code() + " - " + response.message();
                    mError.setValue(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String errorMsg = "Error: " + t.getMessage();
                mError.setValue(errorMsg);
            }
        });


    }*/

    // iniciar sesion comparando con el usuario y contraseña de la clase
    public void iniciarSesion(String usuario, String contraseña) {
        if (this.usuario.equals(usuario) && this.contraseña.equals(contraseña)) {
            mMensaje.setValue("Bienvenido");
        } else {
            mError.setValue("Usuario o contraseña incorrectos");
        }

    }

}
