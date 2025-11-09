package com.isma.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Inmueble implements Serializable {


    @SerializedName("idInmuebles")
    private int idInmuebles;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("ambientes")
    private int ambientes;

    @SerializedName("superficie")
    private int superficie;

    @SerializedName("latitud")
    private double latitud;

    @SerializedName("longitud")
    private double longitud;

    @SerializedName("idUsuario")
    private int idUsuario;


    @SerializedName("duenio")
    private Usuario duenio;

    @SerializedName("idTipoInmueble")
    private int idTipoInmueble;

    @SerializedName("tipoInmueble")
    private String tipoInmueble;

    @SerializedName("precio")
    private double precio;

    @SerializedName("habilitado")
    private boolean habilitado;

    @SerializedName("existe")
    private boolean existe;

    @SerializedName("imagenUrl")
    private String imagenUrl;

    public Inmueble(String direccion, double precio, int ambientes, int superficie, String tipoInmueble, double latitud, double longitud, boolean habilitado) {
        this.direccion = direccion;
        this.precio = precio;
        this.ambientes = ambientes;
        this.superficie = superficie;
        this.tipoInmueble = tipoInmueble;
        this.latitud = latitud;
        this.longitud = longitud;
        this.habilitado = habilitado;
    }


    public Inmueble() {
    }


    public int getIdInmuebles() {
        return idInmuebles;
    }

    public void setIdInmuebles(int idInmuebles) {
        this.idInmuebles = idInmuebles;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getAmbientes() {
        return ambientes;
    }

    public void setAmbientes(int ambientes) {
        this.ambientes = ambientes;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario getDuenio() {
        return duenio;
    }

    public void setDuenio(Usuario duenio) {
        this.duenio = duenio;
    }

    public int getIdTipoInmueble() {
        return idTipoInmueble;
    }

    public void setIdTipoInmueble(int idTipoInmueble) {
        this.idTipoInmueble = idTipoInmueble;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}

