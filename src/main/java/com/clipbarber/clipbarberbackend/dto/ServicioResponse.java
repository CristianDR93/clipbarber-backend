package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.Servicio;

public class ServicioResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracionMinutos;
    private Boolean activo;
    private Long barberiaId;
    private String barberiaNombre;

    public ServicioResponse() {}

    public ServicioResponse(Long id, String nombre, String descripcion, Double precio, Integer duracionMinutos, Boolean activo, Long barberiaId, String barberiaNombre) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
        this.activo = activo;
        this.barberiaId = barberiaId;
        this.barberiaNombre = barberiaNombre;
    }

    public static ServicioResponse fromServicio(Servicio servicio) {
        return new ServicioResponse(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio(),
                servicio.getDuracionMinutos(),
                servicio.getActivo(),
                servicio.getBarberia().getId(),
                servicio.getBarberia().getNombre()
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Long getBarberiaId() {
        return barberiaId;
    }

    public void setBarberiaId(Long barberiaId) {
        this.barberiaId = barberiaId;
    }

    public String getBarberiaNombre() {
        return barberiaNombre;
    }

    public void setBarberiaNombre(String barberiaNombre) {
        this.barberiaNombre = barberiaNombre;
    }
}
