package com.isma.inmobiliaria;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.biometric.BiometricManager;
import com.isma.inmobiliaria.model.TokenResponse;
import com.isma.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    // Los campos de LiveData deben ser privados
    private MutableLiveData<String> mMensaje = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> mNavigateToMain = new MutableLiveData<>();

    private final MutableLiveData<Event<Boolean>> mShowBiometricButton = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> mTriggerBiometricPrompt = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>(false);


    public LiveData<Boolean> getmLoading() {
        return mLoading;
    }


    public LiveData<Event<Boolean>> getNavigateToMain() {
        return mNavigateToMain;
    }

    public LiveData<Event<Boolean>> getShowBiometricButton() {
        return mShowBiometricButton;
    }

    public LiveData<Event<Boolean>> getTriggerBiometricPrompt() {
        return mTriggerBiometricPrompt;
    }




    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // Getters simplificados
    public LiveData<String> getError() {
        return mError;
    }

    public LiveData<String> getMensaje() {
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
                    Log.e("ViewModelll", "Error al guardar: " + tokenLimpio);
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

    public void checkBiometricSupport() {
        if(!ApiClient.hayTokenGuardado(getApplication())){
            mShowBiometricButton.postValue(new Event<>(false));
            return;
        }

        BiometricManager biometricManager = BiometricManager.from(getApplication());
        int canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG);

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            mShowBiometricButton.postValue(new Event<>(true));
        } else {
            mShowBiometricButton.postValue(new Event<>(false));
        }
    }

    //click en el boton de guella, muestra dialogo de biometria
    public void onBiometricLoginClicked() {
        mTriggerBiometricPrompt.postValue(new Event<>(true));
    }

    // exitoso login con huella
    public void onBiometricAuthenticationSucceeded() {
        // El usuario es el dueño del teléfono.
        // Como ya comprobamos que había un token, navegamos a Main.
        mNavigateToMain.postValue(new Event<>(true));
    }

    public void onBiometricAuthenticationFailed() {
        mError.postValue("Huella no reconocida");
    }

    public void onBiometricAuthenticationError(CharSequence errString) {
        mError.postValue("Autenticación cancelada: " + errString.toString());
    }


    public static class Event<T> {
        private T content;
        private boolean hasBeenHandled = false;

        public Event(T content) { this.content = content; }

        public T getContentIfNotHandled() {
            if (hasBeenHandled) {
                return null;
            } else {
                hasBeenHandled = true;
                return content;
            }
        }

        public T peekContent() { return content; }
    }
}
