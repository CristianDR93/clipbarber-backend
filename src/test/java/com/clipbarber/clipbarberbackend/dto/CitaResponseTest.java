package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CitaResponseTest {

    private Cita cita;
    private User cliente;
    private User barberoUser;
    private Barbero barbero;
    private Servicio servicio;
    private Barberia barberia;

    @BeforeEach
    void setUp() {
        barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop");

        cliente = new User();
        cliente.setId(1L);
        cliente.setName("Juan Perez");

        barberoUser = new User();
        barberoUser.setId(2L);
        barberoUser.setName("Pedro Barbero");

        barbero = new Barbero();
        barbero.setId(1L);
        barbero.setUsuario(barberoUser);
        barbero.setBarberia(barberia);

        servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Corte");

        cita = new Cita();
        cita.setId(1L);
        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);
        cita.setFechaHora(LocalDateTime.of(2025, 6, 15, 10, 0));
        cita.setEstado(Cita.EstadoCita.PENDIENTE);
        cita.setObservaciones("Sin observaciones");
        cita.setPrecio(15000.0);
    }

    @Test
    void fromCita_ShouldMapAllFields() {
        CitaResponse response = CitaResponse.fromCita(cita);

        assertEquals(1L, response.getId());
        assertEquals(1L, response.getClienteId());
        assertEquals("Juan Perez", response.getClienteNombre());
        assertEquals(1L, response.getBarberoId());
        assertEquals("Pedro Barbero", response.getBarberoNombre());
        assertEquals(1L, response.getServicioId());
        assertEquals("Corte", response.getServicioNombre());
        assertEquals(LocalDateTime.of(2025, 6, 15, 10, 0), response.getFechaHora());
        assertEquals("PENDIENTE", response.getEstado());
        assertEquals("Sin observaciones", response.getObservaciones());
        assertEquals(15000.0, response.getPrecio());
    }

    @Test
    void fromCita_ShouldMapEstadoAsName() {
        cita.setEstado(Cita.EstadoCita.CONFIRMADA);

        CitaResponse response = CitaResponse.fromCita(cita);

        assertEquals("CONFIRMADA", response.getEstado());
    }

    @Test
    void defaultConstructor_ShouldWork() {
        CitaResponse response = new CitaResponse();

        assertNull(response.getId());
        assertNull(response.getEstado());
    }

    @Test
    void setters_ShouldWork() {
        CitaResponse response = new CitaResponse();
        response.setId(1L);
        response.setClienteId(1L);
        response.setClienteNombre("Juan");
        response.setBarberoId(2L);
        response.setBarberoNombre("Pedro");
        response.setServicioId(1L);
        response.setServicioNombre("Corte");
        response.setFechaHora(LocalDateTime.now());
        response.setEstado("PENDIENTE");
        response.setObservaciones("obs");
        response.setPrecio(15000.0);

        assertEquals(1L, response.getId());
        assertEquals("PENDIENTE", response.getEstado());
    }
}
