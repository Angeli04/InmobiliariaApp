package com.isma.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isma.inmobiliaria.model.Usuario;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

    public class ApiClient {

        public static final String URLBASE = "http://10.0.2.2:5164/";

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
            @POST("api/Propietarios/login")
            Call<String> login(@Field("email") String email, @Field("password") String password);




        }

        // vamos a guardar el token en shared preferences
        public static void guardarToken(Context context, String token) {
            SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("token", token);
            editor.apply();
        }

    }

