package com.isma.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isma.inmobiliaria.model.TokenResponse;
import com.isma.inmobiliaria.model.Usuario;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class ApiClient {

    // ANTES:
    public static final String URLBASE = "http://192.168.0.7:5164/";


    public static ApiService getApiService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ApiService.class);
    }



    public interface ApiService {

        @FormUrlEncoded
        @POST("api/Auth/login")
        Call<TokenResponse> login(@Field("email") String email, @Field("password") String password);


        @GET("api/Auth/perfil") // OJO: Asegúrate que la ruta sea la correcta ("api/Auth/perfil")
        Call<Usuario> getUsuario(@Header("Authorization") String token);

        @POST("api/Auth/actualizar")
        Call<Usuario> actualizarUsuario(@Header("Authorization") String token, @Body Usuario usuarioActualizado);


    }

    // token en shared preferences
    public static void guardarToken(Context context, String token) {
        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String leerToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        return preferences.getString("token", ""); // Devuelve "" si no se encuentra
    }

}