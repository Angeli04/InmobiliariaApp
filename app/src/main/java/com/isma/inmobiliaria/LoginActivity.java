package com.isma.inmobiliaria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.isma.inmobiliaria.databinding.ActivityLoginBinding;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel vm;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private ProgressDialog progressDialog; // Para el 'mLoading'
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupBiometricTools();

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuario = binding.etUsuario.getText().toString();
                String contraseña = binding.etContrasenia.getText().toString();
                vm.iniciarSesion(usuario, contraseña);
                vm.getMensaje().observe(LoginActivity.this, mensaje -> {

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                });
                vm.getError().observe(LoginActivity.this, error -> {

                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();

                    Log.e("ErrorLogin", error);
                });

            }
        });

        binding.ibBiometric.setOnClickListener(v-> {
           vm.onBiometricLoginClicked();
        });

        setupObservers();

        vm.checkBiometricSupport();

    }

    /**
     * Prepara las herramientas de biometría (el Executor, el Callback y el Diálogo).
     * Esto es configuración de la Vista, no lógica de negocio.
     */
    // usar clase biometria para almacenar y desalojar un poco la activity
    private void setupBiometricTools() {
        executor = ContextCompat.getMainExecutor(this);

        // Prepara el diálogo de "Cargando..."
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);

        // El Callback (Qué hacer cuando el diálogo biométrico termina)
        // Solo reporta el resultado al ViewModel.
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Reporta el error al VM
                vm.onBiometricAuthenticationError(errString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // Reporta el fallo al VM
                vm.onBiometricAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Reporta el éxito al VM
                vm.onBiometricAuthenticationSucceeded();
            }
        });

        // El Diálogo (Qué mostrar)
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de sesión biométrico")
                .setSubtitle("Inicia sesión usando tu huella o rostro")
                .setNegativeButtonText("Cancelar")
                .build();
    }

    /**
     * Configura todos los Observers para reaccionar a los cambios del ViewModel.
     * Esta Activity es "tonta", solo obedece.
     */
    private void setupObservers() {

        // Observador para NAVEGAR
        // (Obedece la orden de 'mNavigateToMain' de tu VM fusionado)
        vm.getNavigateToMain().observe(this, event -> {
            // 'getContentIfNotHandled' asegura que la navegación solo ocurra una vez
            if (event.getContentIfNotHandled() != null) {
                navigateToMainActivity();
            }
        });

        // Observador para ERRORES (Toasts)
        // (Obedece la orden de 'mError' de tu VM)
        vm.getError().observe(this, error -> {
            // if redundante
            if(error != null && !error.isEmpty()){
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Observador para LOADING
        // (Obedece la orden de 'mLoading' de tu VM)
        vm.getmLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                if (isLoading) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });

        // Observador para MOSTRAR/OCULTAR BOTÓN BIOMÉTRICO
        // (Obedece la orden de 'mShowBiometricButton' de tu VM)
        vm.getShowBiometricButton().observe(this, event -> {
            Boolean show = event.getContentIfNotHandled();
            if (show != null) {
                binding.ibBiometric.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        // Observador para DISPARAR EL DIÁLOGO BIOMÉTRICO
        // (Obedece la orden de 'mTriggerBiometricPrompt' de tu VM)
        vm.getTriggerBiometricPrompt().observe(this, event -> {
            if (event.getContentIfNotHandled() != null) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    /**
     * Navega a la MainActivity y cierra la LoginActivity.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra el Login para que no se pueda volver
    }
}