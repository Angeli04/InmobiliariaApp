package com.isma.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ContratoDetalleDto implements Serializable {


    @SerializedName("idContrato")
    private int idContrato;

    @SerializedName("monto")
    private double monto; // decimal (C#) o 560000 (JSON) -> double (Java)

    @SerializedName("fechaDesde")
    private String fechaDesde; // "2025-12-01T00:00:00" -> String

    @SerializedName("fechaHasta")
    private String fechaHasta; // "2026-02-01T00:00:00" -> String

    @SerializedName("vigente")
    private int vigente; // 1 -> int

    @SerializedName("cantidadCuotas")
    private int cantidadCuotas;

    @SerializedName("nombreInquilino")
    private String nombreInquilino;

    @SerializedName("direccionInmueble")
    private String direccionInmueble;

    // --- Getters y Setters ---
    // (Necesarios para que el Adapter y el código de la UI los lean)


    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public int getVigente() {
        return vigente;
    }

    public void setVigente(int vigente) {
        this.vigente = vigente;
    }

    public int getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(int cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public String getNombreInquilino() {
        return nombreInquilino;
    }

    public void setNombreInquilino(String nombreInquilino) {
        this.nombreInquilino = nombreInquilino;
    }

    public String getDireccionInmueble() {
        return direccionInmueble;
    }

    public void setDireccionInmueble(String direccionInmueble) {
        this.direccionInmueble = direccionInmueble;
    }
}