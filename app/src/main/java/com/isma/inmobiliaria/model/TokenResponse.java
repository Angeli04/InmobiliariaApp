package com.isma.inmobiliaria.model;

// Puedes añadir la anotación de Gson si quieres ser más explícito,
// aunque no es estrictamente necesario si el nombre coincide.
import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}