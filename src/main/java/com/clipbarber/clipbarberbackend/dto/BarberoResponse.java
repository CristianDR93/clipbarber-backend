package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.Barbero;
import java.util.List;
import java.util.stream.Collectors;

public class BarberoResponse {

    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    private Long barberiaId;
    private String barberiaNombre;
    private Boolean activo;
    private String especialidad;
    private List<String> horarios;
    private List<ServicioResponse> servicios;

    public BarberoResponse() {}

    public BarberoResponse(Long id, Long usuarioId, String usuarioNombre, String usuarioEmail, Long barberiaId, String barberiaNombre, Boolean activo, String especialidad, List<String> horarios, List<ServicioResponse> servicios) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.usuarioEmail = usuarioEmail;
        this.barberiaId = barberiaId;
        this.barberiaNombre = barberiaNombre;
        this.activo = activo;
        this.especialidad = especialidad;
        this.horarios = horarios;
        this.servicios = servicios;
    }

    public static BarberoResponse fromBarbero(Barbero barbero) {
        List<ServicioResponse> serviciosResponse = barbero.getServicios() != null
                ? barbero.getServicios().stream()
                    .map(ServicioResponse::fromServicio)
                    .collect(Collectors.toList())
                : List.of();

        return new BarberoResponse(
                barbero.getId(),
                barbero.getUsuario().getId(),
                barbero.getUsuario().getName(),
                barbero.getUsuario().getEmail(),
                barbero.getBarberia().getId(),
                barbero.getBarberia().getNombre(),
                barbero.getActivo(),
                barbero.getEspecialidad(),
                barbero.getHorarios(),
                serviciosResponse
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioEmail() {
        return usuarioEmail;
    }

    public void setUsuarioEmail(String usuarioEmail) {
        this.usuarioEmail = usuarioEmail;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public List<ServicioResponse> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioResponse> servicios) {
        this.servicios = servicios;
    }
}
