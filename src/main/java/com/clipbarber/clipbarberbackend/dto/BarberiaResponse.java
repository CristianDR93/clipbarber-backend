package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.Barberia;

public class BarberiaResponse {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String imagenUrl;
    private Double latitud;
    private Double longitud;
    private Boolean activa;

    public BarberiaResponse() {}

    public BarberiaResponse(Long id, String nombre, String direccion, String telefono, String email, String imagenUrl, Double latitud, Double longitud, Boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.imagenUrl = imagenUrl;
        this.latitud = latitud;
        this.longitud = longitud;
        this.activa = activa;
    }

    public static BarberiaResponse fromBarberia(Barberia barberia) {
        return new BarberiaResponse(
                barberia.getId(),
                barberia.getNombre(),
                barberia.getDireccion(),
                barberia.getTelefono(),
                barberia.getEmail(),
                barberia.getImagenUrl(),
                barberia.getLatitud(),
                barberia.getLongitud(),
                barberia.getActiva()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}
