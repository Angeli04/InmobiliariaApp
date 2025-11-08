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
import com.isma.inmobiliaria.utils.BiometricHelper;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    // --- ViewModel y Binding ---
    private LoginActivityViewModel viewModel;
    // Binding: Acceso a las vistas (reemplaza a findViewById)
    private ActivityLoginBinding binding;

    // --- Clases Helper ---
    private BiometricHelper biometricHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Configuración de View Binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Configuración del ViewModel
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        // 3. Configuración del Helper Biométrico
        //    (Le pasamos la Activity y el ViewModel, como pide su constructor)
        biometricHelper = new BiometricHelper(this, viewModel);

        // 4. Configurar Listeners (Acciones del Usuario -> ViewModel)
        setupListeners();

        // 5. Configurar Observadores (Eventos del ViewModel -> UI)
        setupObservers();

        // 6. Comprobar soporte biométrico al iniciar
        viewModel.checkBiometricSupport();
    }

    /**
     * Informa al ViewModel sobre las acciones del usuario.
     * CERO lógica aquí.
     */
    private void setupListeners() {
        // Clic en "Iniciar Sesión"
        binding.button.setOnClickListener(v -> {
            String email = binding.etUsuario.getText().toString();
            String pass = binding.etContrasenia.getText().toString();

            viewModel.iniciarSesion(email, pass);
        });

        binding.ibBiometric.setOnClickListener(v -> {
            viewModel.onBiometricLoginClicked();
        });
    }


    private void setupObservers() {


        viewModel.getmError().observe(this, error -> {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });

        viewModel.getNavigateToMain().observe(this, event -> {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
        });


        viewModel.getTriggerBiometricPrompt().observe(this, event -> {
                biometricHelper.authenticate();
        });

        viewModel.getShowLoading().observe(this, event -> {
                binding.button.setEnabled(false);
        });

        viewModel.getHideLoading().observe(this, event -> {
                binding.button.setEnabled(true);
        });



        viewModel.getShowBiometricButton().observe(this, event -> {
                binding.ibBiometric.setVisibility(View.VISIBLE);
        });

        viewModel.getHideBiometricButton().observe(this, event -> {
                binding.ibBiometric.setVisibility(View.GONE);
        });
    }
}