package com.miaprendizaje.servicio;

public class Usuario {
    private  String nombre;

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    private  String apellido;
    private  String correo;

    // Constructor vacío (obligatorio para que Spring pueda crearlo)
    public Usuario() {}



}
