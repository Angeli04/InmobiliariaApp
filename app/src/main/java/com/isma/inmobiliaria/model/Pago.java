package com.isma.inmobiliaria.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pago implements Serializable {

    // Asumo que la tabla 'pagos' tiene una Clave Primaria 'idPago'
    @SerializedName("idPago")
    private int idPago;

    @SerializedName("idContrato")
    private int idContrato;

    @SerializedName("fechaPago")
    private String fechaPago; // "2025-12-01T00:00:00"

    @SerializedName("importe")
    private double importe;

    @SerializedName("concepto")
    private String concepto;

    @SerializedName("numeroCuota")
    private int numeroCuota;

    @SerializedName("mesPago")
    private int mesPago;

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public int getMesPago() {
        return mesPago;
    }

    public void setMesPago(int mesPago) {
        this.mesPago = mesPago;
    }
}
