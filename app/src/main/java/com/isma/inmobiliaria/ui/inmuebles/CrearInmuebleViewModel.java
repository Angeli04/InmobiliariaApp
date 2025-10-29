package com.isma.inmobiliaria.ui.inmuebles;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.isma.inmobiliaria.model.Inmueble;
import com.isma.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class CrearInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> mUri = new MutableLiveData<>();

    public LiveData<Uri> getUri() {
        return mUri;
    }


    public CrearInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Uri uri = data.getData();
            mUri.postValue(uri);
        }
    }

    public void cargarInmueble(String direccion, String precio, String ambientes, String superficie, String tipoInmueble, String latitud, String longitud, boolean habilitado){
        try {
            int superficieInt = Integer.parseInt(superficie);
            double latitudDouble = Double.parseDouble(latitud);
            double longitudDouble = Double.parseDouble(longitud);
            double precioDouble = Double.parseDouble(precio);
            int ambientesInt = Integer.parseInt(ambientes);



            //validamos todos los campos
            if (direccion.isEmpty() || precio.isEmpty() || ambientes.isEmpty() || superficie.isEmpty() || tipoInmueble.isEmpty() || latitud.isEmpty() || longitud.isEmpty()) {
                Toast.makeText(getApplication(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            //validamos que haya foto
            if (mUri.getValue() == null) {
                Toast.makeText(getApplication(), "No hay una foto seleccionada", Toast.LENGTH_SHORT).show();
                return;
            }

            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(direccion);
            inmueble.setPrecio(precioDouble);
            inmueble.setAmbientes(ambientesInt);
            inmueble.setSuperficie(superficieInt);
            inmueble.setTipoInmueble(tipoInmueble);
            inmueble.setLatitud(latitudDouble);
            inmueble.setLongitud(longitudDouble);
            byte[] imagen = transformarImagen();

            String inmuebleJson = new Gson().toJson(inmueble);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imagen);

            // armar multipart
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);


            ApiClient.ApiService api = ApiClient.getApiService();
            Call<Inmueble> call = api.crearInmueble("Bearer " + ApiClient.leerToken(getApplication()), imagenPart, requestBody);
            call.enqueue(new Callback<Inmueble>(){

                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Inmueble creado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplication(), "Error al cargar inmueble, isSuccessful", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    Toast.makeText(getApplication(), "Error al cargar inmueble, onFailure", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (NumberFormatException nfe){

            Toast.makeText(getApplication(), "Error en los campos", Toast.LENGTH_SHORT).show();
            return;
        }catch (Exception e){

            Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_SHORT).show();
            return;
        }


    };

    private byte[] transformarImagen() {
        try {
            Uri uri = mUri.getValue();  //lo puedo usar porque estoy en viewmodel
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (
                FileNotFoundException er) {
            Toast.makeText(getApplication(), "No ha seleccinado una foto", Toast.LENGTH_SHORT).show();
            return new byte[]{};
        }
    }
}

