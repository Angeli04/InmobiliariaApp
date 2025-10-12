package com.isma.inmobiliaria.model;
public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String clave;
    private String avatar;
    private int rol;
    private int existe; // Campo adicional según tu backend

    // Constructor vacío (obligatorio para Gson)
    public Usuario() {
    }

    // Constructor opcional
    public Usuario(String email, String clave) {
        this.email = email;
        this.clave = clave;
    }

    // Getters y Setters
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
