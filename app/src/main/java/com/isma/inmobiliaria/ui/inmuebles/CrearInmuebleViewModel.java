package com.isma.inmobiliaria.ui.inmuebles;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.isma.inmobiliaria.model.Inmueble;
import com.isma.inmobiliaria.model.TipoInmueble;
import com.isma.inmobiliaria.request.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class CrearInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> mUri = new MutableLiveData<>();
    private MutableLiveData<List<TipoInmueble>> mTipoInmueble = new MutableLiveData<>();

    private final MutableLiveData<String> mError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> mGuardadoExitoso = new MutableLiveData<>(false);

    private final MutableLiveData<String> mToast = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mNavegacion = new MutableLiveData<>(false);

    public LiveData<String> getToast() {
        return mToast;
    }

    public LiveData<Boolean> getNavegacion() {
        return mNavegacion;
    }


    public LiveData<String> getError() {
        return mError;
    }
    public LiveData<Boolean> getLoading() {
        return mLoading;
    }
    public LiveData<Boolean> getGuardadoExitoso() {
        return mGuardadoExitoso;
    }
    public LiveData<List<TipoInmueble>> getTipoInmueble() {
        return mTipoInmueble;
    }
    public LiveData<Uri> getUri() {
        return mUri;
    }
    public CrearInmuebleViewModel(@NonNull Application application) {
        super(application);
    }
    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null){
                mUri.postValue(data.getData());
            }
        }
    }

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

    //funcion para traer a traves de la api los tipos de inmueble
    public void traerTipos(){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.ApiService api = ApiClient.getApiService();
        Call<List<TipoInmueble>> llamada = api.listarTipos( "Bearer " + token);

        llamada.enqueue(new Callback<List<TipoInmueble>>(){
            @Override
            public void onResponse(@NonNull Call<List<TipoInmueble>> call, @NonNull Response<List<TipoInmueble>> response) {
                if(response.isSuccessful()){
                    List<TipoInmueble> listaDeTipos = response.body();
                    mTipoInmueble.postValue(listaDeTipos);
                } else {
                    mError.postValue("Error al traer los tipos: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<TipoInmueble>> call, @NonNull Throwable t) {
                mError.postValue("Error al traer los tipos: " + t.getMessage());
            }
        });
    }



    public void cargarInmueble(String direccion, String precioStr, String ambientesStr, String superficieStr,
                               TipoInmueble tipoSeleccionado, String latitudStr,
                               String longitudStr, boolean habilitado) {

        mLoading.postValue(true);


        if (tipoSeleccionado == null) {
            mError.postValue("Debe seleccionar un tipo de inmueble.");
            mLoading.postValue(false);
            return;
        }
        if (mUri.getValue() == null) {
            mError.postValue("Debe seleccionar una imagen.");
            mLoading.postValue(false);
            return;
        }
        if (direccion.isEmpty() || precioStr.isEmpty() || ambientesStr.isEmpty() || superficieStr.isEmpty() || latitudStr.isEmpty() || longitudStr.isEmpty()) {
            mError.postValue("Todos los campos son obligatorios.");
            mLoading.postValue(false);
            return;
        }

        try {

            int idDelTipo = tipoSeleccionado.getIdTipoInmueble();

            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(direccion);
            inmueble.setPrecio(Double.parseDouble(precioStr));
            inmueble.setAmbientes(Integer.parseInt(ambientesStr));
            inmueble.setSuperficie(Integer.parseInt(superficieStr));
            inmueble.setIdTipoInmueble(idDelTipo); // Asignamos el ID
            inmueble.setLatitud(Double.parseDouble(latitudStr));
            inmueble.setLongitud(Double.parseDouble(longitudStr));
            inmueble.setHabilitado(habilitado);

            // preparar multipart body para la imagen
            MultipartBody.Part imagenPart = prepararImagenPart(mUri.getValue());
            if (imagenPart == null) {
                mLoading.postValue(false);
                return;
            }


            Gson gson = new Gson();
            String inmuebleJsonString = gson.toJson(inmueble);
            RequestBody inmuebleJsonBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    inmuebleJsonString
            );


            String token = ApiClient.leerToken(getApplication());
            ApiClient.ApiService api = ApiClient.getApiService();
            Call<Inmueble> call = api.altaInmueble("Bearer " + token, imagenPart, inmuebleJsonBody);

            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(@NonNull Call<Inmueble> call, @NonNull Response<Inmueble> response) {
                    mLoading.postValue(false);
                    if (response.isSuccessful()) {

                        mToast.postValue("Inmueble guardado con éxito");
                        mNavegacion.postValue(true); // ¡Ordena al Fragment navegar!
                    } else {
                        mError.postValue("Error al guardar: " + response.code() + " " + response.message());
                        Log.e("ViewModelll", "Error al guardar: " + token);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Inmueble> call, @NonNull Throwable t) {
                    mLoading.postValue(false);
                    mError.postValue("Fallo de conexión: " + t.getMessage());
                    Log.e("ViewModelll", "Error al guardar: " + t.getMessage());
                }
            });

        } catch (NumberFormatException e) {
            mError.postValue("Error: Verifique los campos numéricos.");
            mLoading.postValue(false);
        }
    }


    private MultipartBody.Part prepararImagenPart(Uri uri) {
        try {
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);

            byte[] fileBytes = toByteArray(inputStream);
            inputStream.close();

            String mimeType = getApplication().getContentResolver().getType(uri);

            RequestBody requestFileBody = RequestBody.create(MediaType.parse(mimeType), fileBytes);

            return MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFileBody);

        } catch (FileNotFoundException e) {
            Log.e("ViewModel", "Archivo no encontrado", e);
            mError.postValue("Error: No se encontró el archivo de imagen.");
            return null;
        } catch (IOException e) {
            Log.e("ViewModel", "Error de IO", e);
            mError.postValue("Error al leer la imagen.");
            return null;
        }
    }

    private byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }
}

