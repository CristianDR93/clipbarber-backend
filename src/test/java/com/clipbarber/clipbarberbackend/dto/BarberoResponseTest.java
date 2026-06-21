package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.model.Barbero;
import com.clipbarber.clipbarberbackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BarberoResponseTest {

    private Barbero barbero;
    private User usuario;
    private Barberia barberia;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setName("Pedro Barbero");
        usuario.setEmail("pedro@example.com");

        barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop");

        barbero = new Barbero();
        barbero.setId(1L);
        barbero.setUsuario(usuario);
        barbero.setBarberia(barberia);
        barbero.setEspecialidad("Corte moderno");
        barbero.setActivo(true);
        barbero.setHorarios(List.of("09:00-18:00"));
        barbero.setServicios(new ArrayList<>());
    }

    @Test
    void fromBarbero_ShouldMapAllFields() {
        BarberoResponse response = BarberoResponse.fromBarbero(barbero);

        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUsuarioId());
        assertEquals("Pedro Barbero", response.getUsuarioNombre());
        assertEquals("pedro@example.com", response.getUsuarioEmail());
        assertEquals(1L, response.getBarberiaId());
        assertEquals("BarberShop", response.getBarberiaNombre());
        assertTrue(response.getActivo());
        assertEquals("Corte moderno", response.getEspecialidad());
        assertEquals(List.of("09:00-18:00"), response.getHorarios());
        assertNotNull(response.getServicios());
        assertTrue(response.getServicios().isEmpty());
    }

    @Test
    void fromBarbero_ShouldHandleNullServicios() {
        barbero.setServicios(null);

        BarberoResponse response = BarberoResponse.fromBarbero(barbero);

        assertNotNull(response.getServicios());
        assertTrue(response.getServicios().isEmpty());
    }

    @Test
    void defaultConstructor_ShouldWork() {
        BarberoResponse response = new BarberoResponse();

        assertNull(response.getId());
        assertNull(response.getEspecialidad());
    }

    @Test
    void setters_ShouldWork() {
        BarberoResponse response = new BarberoResponse();
        response.setId(1L);
        response.setUsuarioId(1L);
        response.setUsuarioNombre("Pedro");
        response.setUsuarioEmail("pedro@example.com");
        response.setBarberiaId(1L);
        response.setBarberiaNombre("BarberShop");
        response.setActivo(true);
        response.setEspecialidad("Corte");
        response.setHorarios(List.of("09:00-18:00"));
        response.setServicios(List.of());

        assertEquals(1L, response.getId());
        assertEquals("Pedro", response.getUsuarioNombre());
        assertEquals("Corte", response.getEspecialidad());
    }
}
