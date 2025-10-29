package com.isma.inmobiliaria.model;

public class HabilitacionRequestDto {
    public int IdInmuebles;
    public boolean Habilitado;


    public HabilitacionRequestDto(int idInmuebles, boolean habilitado) {
        IdInmuebles = idInmuebles;
        Habilitado = habilitado;

    }

    public HabilitacionRequestDto() {
    }

    public int getIdInmuebles() {
        return IdInmuebles;
    }

    public void setIdInmuebles(int idInmuebles) {
        IdInmuebles = idInmuebles;
    }

    public boolean isHabilitado() {
        return Habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        Habilitado = habilitado;
    }
}



