package com.isma.inmobiliaria.ui.perfil;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.isma.inmobiliaria.R;
import com.isma.inmobiliaria.databinding.FragmentPerfilBinding;
import com.isma.inmobiliaria.model.Usuario;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private FragmentPerfilBinding binding;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        mViewModel.getUsuario().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                // Este código se ejecutará cuando los datos lleguen desde la API
                    binding.etId.setText(usuario.getIdUsuario() + "");
                    binding.etNombre.setText(usuario.getNombre());
                    binding.etApellido.setText(usuario.getApellido());
                    binding.etDni.setText(usuario.getDni() + "");
                    binding.etTelefono.setText(usuario.getTelefono());
                    binding.etEmail.setText(usuario.getEmail());
            }
        });



        mViewModel.getModoEditar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean editing) {
                //binding.etId.setEnabled(editing);
                binding.etNombre.setEnabled(editing);
                binding.etApellido.setEnabled(editing);
                binding.etDni.setEnabled(editing);
                binding.etTelefono.setEnabled(editing);
                binding.etEmail.setEnabled(editing);

            }

        });

        binding.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuarioActualizado = new Usuario();
                usuarioActualizado.setIdUsuario(Integer.parseInt(binding.etId.getText().toString()));
                usuarioActualizado.setNombre(binding.etNombre.getText().toString());
                usuarioActualizado.setApellido(binding.etApellido.getText().toString());
                usuarioActualizado.setDni(Integer.parseInt(binding.etDni.getText().toString()));
                usuarioActualizado.setTelefono(binding.etTelefono.getText().toString());
                usuarioActualizado.setEmail(binding.etEmail.getText().toString());

                mViewModel.actualizarUsuario(usuarioActualizado);
                binding.btEditar.setText("GUARDAR");
            }

        });

        mViewModel.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String mensaje) {
                        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    }
        });

        mViewModel.getNavigateToInicio().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean navigate) {
                if (navigate) {
                    Navigation.findNavController(requireView()).navigate(R.id.inicioFragment);
                    mViewModel.onNavigationComplete();
                }
            }
        });

        mViewModel.cargarUsuario();
        return binding.getRoot();
    }



}