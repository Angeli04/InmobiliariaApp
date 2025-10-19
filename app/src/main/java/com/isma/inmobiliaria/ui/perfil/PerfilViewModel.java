package com.isma.inmobiliaria.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.isma.inmobiliaria.model.Propietario;
import com.isma.inmobiliaria.model.Usuario;
import com.isma.inmobiliaria.request.ApiClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData <Usuario> mUsuario;
    private MutableLiveData<Boolean> mModoEditar;

    private MutableLiveData<String> mMensaje;

    private MutableLiveData<Boolean> mNavigateToInicio ;

    public LiveData<Boolean> getNavigateToInicio(){
        if (mNavigateToInicio == null){
            mNavigateToInicio = new MutableLiveData<>(false);
        }
        return mNavigateToInicio;
    }

    public LiveData<Boolean> getModoEditar(){
        if (mModoEditar == null){
            mModoEditar = new MutableLiveData<>(false);
        }
        return mModoEditar;
    }

    public LiveData<String> getMensaje(){
        if (mMensaje == null){
            mMensaje = new MutableLiveData<>();
        }
        return mMensaje;
    }

    public void setmModoEditar(boolean value){
        mModoEditar.setValue(value);
    }


    public LiveData<Usuario> getUsuario(){
        if (mUsuario == null){
            mUsuario = new MutableLiveData<>();
        }
        return mUsuario;
    }


    public void activarModoEditar(){
        // Cambiar el valor de mModoEditar a true
        mModoEditar.setValue(true);
    }

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public void cargarUsuario() {

        Context context = getApplication().getApplicationContext();
        String token = ApiClient.leerToken(context);

        if (token.isEmpty()) {
            return;
        }

        Call<Usuario> callUsuario = ApiClient.getApiService().getUsuario("Bearer " + token);
        callUsuario.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    mUsuario.postValue(response.body());
                }else{
                   Log.d("PerfilViewModel", "Error al cargar el usuario: " + response.code());
                    Log.d("PerfilViewModel", "TOKEN: " + token);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("PerfilViewModel", "Error al cargar el usuario", t);
            }
        });


    }

    public void actualizarUsuario(Usuario usuarioActualizado) {
        // si el modo editar esta descactivado, no hace nada
        if (!mModoEditar.getValue()) {
            mMensaje.postValue("Modo editar Activado");
            mModoEditar.setValue(true);
            return;
        }else {
            // si el modo editar esta activado, actualiza el usuario
            mMensaje.postValue("Guardando cambios...");


            Context context = getApplication().getApplicationContext();
            String token = ApiClient.leerToken(context);

            if (token.isEmpty()) {
                return;
            }

            //validacion de campos
            if (usuarioActualizado.getNombre().isEmpty() || usuarioActualizado.getApellido().isEmpty() ||
                    usuarioActualizado.getDni().isEmpty() || usuarioActualizado.getTelefono().isEmpty() ||
                    usuarioActualizado.getEmail().isEmpty()) {
                mMensaje.postValue("Por favor, complete todos los campos");
                return;
            }


            Gson gson = new Gson();
            String usuarioJson = gson.toJson(usuarioActualizado);
            Log.d("PerfilViewModel", "Usuario JSON: " + usuarioJson);

            Call<Usuario> callUsuario = ApiClient.getApiService().actualizarUsuario("Bearer " + token, usuarioActualizado);
            callUsuario.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.isSuccessful()) {
                        mMensaje.postValue("Usuario actualizado exitosamente");
                        mUsuario.postValue(response.body());
                        mNavigateToInicio.postValue(true);
                        mModoEditar.postValue(false);
                    } else {
                        Log.d("PerfilViewModel", "Error al actualizar el usuario: " + response.code());
                        Log.e("PerfilViewModel", "TOKEN: " + token);
                        Log.e("PerfilViewModel", "USUARIO:" + usuarioActualizado);
                        try {
                            Log.e("PerfilViewModel", "BODY: " + response.errorBody().string() );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("API_URL_CHECK", "Intentando llamar a la URL: " + callUsuario.request().url());

                        mMensaje.postValue("Error al actualizar el usuario");
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Log.e("PerfilViewModel", "Error al actualizar el usuario", t);
                    Log.e("PerfilViewModel", "TOKEN: " + token);
                    Log.e("PerfilViewModel", "USUARIO:" + usuarioActualizado.toString());
                    mMensaje.postValue("Error al actualizar el usuario");
                }

            });
        }
    }

    public void onNavigationComplete() {
        mNavigateToInicio.setValue(false);
    }
}