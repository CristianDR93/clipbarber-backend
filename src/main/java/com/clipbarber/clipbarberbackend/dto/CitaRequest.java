package com.clipbarber.clipbarberbackend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CitaRequest {

    @NotNull(message = "El barbero es obligatorio")
    private Long barberoId;

    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    private String observaciones;

    @NotNull(message = "El precio es obligatorio")
    private Double precio;

    public CitaRequest() {}

    public CitaRequest(Long barberoId, Long servicioId, LocalDateTime fechaHora, String observaciones, Double precio) {
        this.barberoId = barberoId;
        this.servicioId = servicioId;
        this.fechaHora = fechaHora;
        this.observaciones = observaciones;
        this.precio = precio;
    }

    public Long getBarberoId() {
        return barberoId;
    }

    public void setBarberoId(Long barberoId) {
        this.barberoId = barberoId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
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
