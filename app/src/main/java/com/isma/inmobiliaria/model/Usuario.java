package com.isma.inmobiliaria.model;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String clave;
    private String dni;
    private String telefono;
    private String avatar;
    private int rol;
    private int existe;

    // Constructor vacío (obligatorio para Gson/Firebase)
    public Usuario() {
    }

    // Constructor completo (con los parámetros ordenados y corregidos)
    public Usuario(int idUsuario, String nombre, String apellido, String email, String clave, String dni, String telefono, String avatar, int rol, int existe) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.clave = clave;
        this.dni = dni;
        this.telefono = telefono;
        this.avatar = avatar;
        this.rol = rol;
        this.existe = existe;
    }

    // Getters y Setters (incluyendo los que faltaban)
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getExiste() {
        return existe;
    }

    public void setExiste(int existe) {
        this.existe = existe;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + email + ")";
    }
}