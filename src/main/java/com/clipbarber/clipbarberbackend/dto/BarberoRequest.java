package com.clipbarber.clipbarberbackend.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class BarberoRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La barberia es obligatoria")
    private Long barberiaId;

    private String especialidad;

    private List<String> horarios;

    private List<Long> serviciosIds;

    public BarberoRequest() {}

    public BarberoRequest(Long usuarioId, Long barberiaId, String especialidad, List<String> horarios, List<Long> serviciosIds) {
        this.usuarioId = usuarioId;
        this.barberiaId = barberiaId;
        this.especialidad = especialidad;
        this.horarios = horarios;
        this.serviciosIds = serviciosIds;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getBarberiaId() {
        return barberiaId;
    }

    public void setBarberiaId(Long barberiaId) {
        this.barberiaId = barberiaId;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<String> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<String> horarios) {
        this.horarios = horarios;
    }

    public List<Long> getServiciosIds() {
        return serviciosIds;
    }

    public void setServiciosIds(List<Long> serviciosIds) {
        this.serviciosIds = serviciosIds;
    }
}
