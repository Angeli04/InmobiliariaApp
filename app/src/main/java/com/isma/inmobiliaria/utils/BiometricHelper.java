package com.isma.inmobiliaria.utils; // O el paquete que prefieras para tus "utilidades"

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity; // Importa AppCompatActivity
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

// 1. Importa tu ViewModel
import com.isma.inmobiliaria.LoginActivityViewModel;

import java.util.concurrent.Executor;

public class BiometricHelper {

    private final Executor executor;
    private final BiometricPrompt biometricPrompt;
    private final BiometricPrompt.PromptInfo promptInfo;


    public BiometricHelper(@NonNull AppCompatActivity activity, @NonNull LoginActivityViewModel viewModel) {

        //  Obtiene el Executor
        this.executor = ContextCompat.getMainExecutor(activity);
        this.biometricPrompt = new BiometricPrompt(activity, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                viewModel.onBiometricAuthenticationError(errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                viewModel.onBiometricAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                viewModel.onBiometricAuthenticationSucceeded();
            }
        });
        this.promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de sesión biométrico")
                .setSubtitle("Inicia sesión usando tu huella o rostro")
                .setNegativeButtonText("Cancelar")
                .build();
    }

    public void authenticate() {
        biometricPrompt.authenticate(promptInfo);
    }

}
