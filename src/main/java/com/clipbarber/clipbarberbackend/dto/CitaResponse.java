package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.Cita;
import java.time.LocalDateTime;

public class CitaResponse {

    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private Long barberoId;
    private String barberoNombre;
    private Long servicioId;
    private String servicioNombre;
    private LocalDateTime fechaHora;
    private String estado;
    private String observaciones;
    private Double precio;

    public CitaResponse() {}

    public CitaResponse(Long id, Long clienteId, String clienteNombre, Long barberoId, String barberoNombre, Long servicioId, String servicioNombre, LocalDateTime fechaHora, String estado, String observaciones, Double precio) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.barberoId = barberoId;
        this.barberoNombre = barberoNombre;
        this.servicioId = servicioId;
        this.servicioNombre = servicioNombre;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.observaciones = observaciones;
        this.precio = precio;
    }

    public static CitaResponse fromCita(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getCliente().getId(),
                cita.getCliente().getName(),
                cita.getBarbero().getId(),
                cita.getBarbero().getUsuario().getName(),
                cita.getServicio().getId(),
                cita.getServicio().getNombre(),
                cita.getFechaHora(),
                cita.getEstado().name(),
                cita.getObservaciones(),
                cita.getPrecio()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public Long getBarberoId() {
        return barberoId;
    }

    public void setBarberoId(Long barberoId) {
        this.barberoId = barberoId;
    }

    public String getBarberoNombre() {
        return barberoNombre;
    }

    public void setBarberoNombre(String barberoNombre) {
        this.barberoNombre = barberoNombre;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public String getServicioNombre() {
        return servicioNombre;
    }

    public void setServicioNombre(String servicioNombre) {
        this.servicioNombre = servicioNombre;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
