package com.isma.inmobiliaria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.isma.inmobiliaria.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel vm;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuario = binding.etUsuario.getText().toString();
                String contraseña = binding.etContrasenia.getText().toString();
                vm.iniciarSesion(usuario, contraseña);
                vm.getMensaje().observe(LoginActivity.this, mensaje -> {
                    // Iniciamos el fragment inicio a traves de un intent

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                });
                vm.getError().observe(LoginActivity.this, error -> {
                    // Mostramos el error en un toast
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    // mostramos el error por consola con un log
                    Log.e("ErrorLogin", error);
                });

            }
        });

    }
}