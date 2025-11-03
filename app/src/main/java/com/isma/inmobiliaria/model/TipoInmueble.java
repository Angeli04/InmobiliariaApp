package com.isma.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TipoInmueble implements Serializable {

    @SerializedName("idTipoInmueble")
    private int idTipoInmueble;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("existe")
    private int existe;


    public TipoInmueble(int idTipoInmueble, String nombre, int existe) {
        this.idTipoInmueble = idTipoInmueble;
        this.nombre = nombre;
        this.existe = existe;
    }

    public TipoInmueble() { }


    public int getIdTipoInmueble() {
        return idTipoInmueble;
    }

    public void setIdTipoInmueble(int idTipoInmueble) {
        this.idTipoInmueble = idTipoInmueble;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExiste() {
        return existe;
    }

    public void setExiste(int existe) {
        this.existe = existe;
    }



    @Override
    public String toString() {
        return nombre;
    }
}