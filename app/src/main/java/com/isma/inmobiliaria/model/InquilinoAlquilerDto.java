package com.isma.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;


public class InquilinoAlquilerDto implements Serializable {

    // (Anotaciones @SerializedName para camelCase)

    @SerializedName("idInquilino")
    private int idInquilino;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("dni")
    private String dni;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("email")
    private String email;

    @SerializedName("direccionInmueble")
    private String direccionInmueble;

    // --- Getters y Setters ---
    // (Generados automáticamente)

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccionInmueble() {
        return direccionInmueble;
    }

    public void setDireccionInmueble(String direccionInmueble) {
        this.direccionInmueble = direccionInmueble;
    }
}

