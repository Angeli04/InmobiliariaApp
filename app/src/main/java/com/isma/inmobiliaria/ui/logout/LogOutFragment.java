package com.isma.inmobiliaria.ui.logout;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isma.inmobiliaria.LoginActivity;
import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentLogOutBinding;

public class LogOutFragment extends Fragment {

    private LogOutViewModel mViewModel;
    private FragmentLogOutBinding binding;


    public static LogOutFragment newInstance() {
        return new LogOutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLogOutBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(LogOutViewModel.class);

        mostrarDialogoSalir();

        mViewModel.getCerrarSesion().observe(getViewLifecycleOwner(), cerrarSesion -> {
            if (cerrarSesion != null && cerrarSesion) {
               // navegamos a la activity del login
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogOutViewModel.class);
        // TODO: Use the ViewModel
    }

    private void mostrarDialogoSalir() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Salir")
                .setMessage("¿Seguro que desea salir de la aplicación?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    mViewModel.cerrarSesion();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    // Opcional: volver al fragment anterior
                    requireActivity().onBackPressed();
                })
                .setCancelable(false)
                .show();
    }

}