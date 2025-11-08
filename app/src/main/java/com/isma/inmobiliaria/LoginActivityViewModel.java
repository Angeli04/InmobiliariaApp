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

    // --- LiveData para Órdenes/Eventos ---
    private final MutableLiveData<String> mError = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> mNavigateToMain = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> mTriggerBiometricPrompt = new MutableLiveData<>();

    // --- 1. CAMBIO: Separamos Loading en dos órdenes ---
    private final MutableLiveData<Event<Void>> mShowLoading = new MutableLiveData<>();
    private final MutableLiveData<Event<Void>> mHideLoading = new MutableLiveData<>();

    // --- 2. CAMBIO: Separamos el botón en dos órdenes ---
    private final MutableLiveData<Event<Void>> mShowBiometricButton = new MutableLiveData<>();
    private final MutableLiveData<Event<Void>> mHideBiometricButton = new MutableLiveData<>();


    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // --- Getters para que la Activity (Vista) observe ---
    public LiveData<String> getmError() { return mError; }
    public LiveData<Event<Boolean>> getNavigateToMain() { return mNavigateToMain; }
    public LiveData<Event<Boolean>> getTriggerBiometricPrompt() { return mTriggerBiometricPrompt; }

    // --- 3. CAMBIO: Nuevos Getters ---
    public LiveData<Event<Void>> getShowLoading() { return mShowLoading; }
    public LiveData<Event<Void>> getHideLoading() { return mHideLoading; }
    public LiveData<Event<Void>> getShowBiometricButton() { return mShowBiometricButton; }
    public LiveData<Event<Void>> getHideBiometricButton() { return mHideBiometricButton; }


    // --- LÓGICA DE LOGIN NORMAL ---
    public void iniciarSesion(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mError.postValue("Por favor, complete todos los campos.");
            return;
        }

        mShowLoading.postValue(new Event<>(null)); // 4. CAMBIO: Orden de "Mostrar"

        ApiClient.ApiService api = ApiClient.getApiService();
        //
        Call<TokenResponse> call = api.login(email, password);


        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                mHideLoading.postValue(new Event<>(null)); // 5. CAMBIO: Orden de "Ocultar"
                if (response.isSuccessful() && response.body() != null) {
                    String tokenLimpio = response.body().getToken();
                    ApiClient.guardarToken(getApplication(), tokenLimpio);
                    Log.d("LOGIN_VM", "Token guardado exitosamente.");
                    mNavigateToMain.postValue(new Event<>(true));
                } else {
                    Log.d("LOGIN_VM", "Login no exitoso. Código: " + response.code());
                    mError.postValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                mHideLoading.postValue(new Event<>(null)); // 6. CAMBIO: Orden de "Ocultar"
                Log.e("LOGIN_VM", "Fallo en la llamada de login: " + t.getMessage());
                mError.postValue("Fallo de conexión: " + t.getMessage());
            }
        });
    }

    // --- LÓGICA DE BIOMETRÍA ---
    public void checkBiometricSupport() {
        if (!ApiClient.hayTokenGuardado(getApplication())) {
            mHideBiometricButton.postValue(new Event<>(null)); // 7. CAMBIO
            return;
        }

        BiometricManager biometricManager = BiometricManager.from(getApplication());
        int canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG);

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            mShowBiometricButton.postValue(new Event<>(null)); // 8. CAMBIO
        } else {
            mHideBiometricButton.postValue(new Event<>(null)); // 9. CAMBIO
        }
    }

    public void onBiometricLoginClicked() {
        mTriggerBiometricPrompt.postValue(new Event<>(true));
    }

    public void onBiometricAuthenticationSucceeded() {
        mNavigateToMain.postValue(new Event<>(true));
    }

    public void onBiometricAuthenticationFailed() {
        mError.postValue("Huella no reconocida");
    }

    public void onBiometricAuthenticationError(CharSequence errString) {
        mError.postValue("Autenticación cancelada: " + errString.toString());
    }

    // --- Clase Auxiliar para Eventos ---
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
