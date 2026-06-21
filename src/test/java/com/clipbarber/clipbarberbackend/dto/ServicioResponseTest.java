package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.model.Servicio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicioResponseTest {

    @Test
    void fromServicio_ShouldMapAllFields() {
        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop");

        Servicio servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Corte");
        servicio.setDescripcion("Corte moderno");
        servicio.setPrecio(15000.0);
        servicio.setDuracionMinutos(30);
        servicio.setActivo(true);
        servicio.setBarberia(barberia);

        ServicioResponse response = ServicioResponse.fromServicio(servicio);

        assertEquals(1L, response.getId());
        assertEquals("Corte", response.getNombre());
        assertEquals("Corte moderno", response.getDescripcion());
        assertEquals(15000.0, response.getPrecio());
        assertEquals(30, response.getDuracionMinutos());
        assertTrue(response.getActivo());
        assertEquals(1L, response.getBarberiaId());
        assertEquals("BarberShop", response.getBarberiaNombre());
    }

    @Test
    void fromServicio_ShouldNotExposeInternalState() {
        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop");

        Servicio servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Corte");
        servicio.setPrecio(15000.0);
        servicio.setDuracionMinutos(30);
        servicio.setActivo(true);
        servicio.setBarberia(barberia);

        ServicioResponse response = ServicioResponse.fromServicio(servicio);

        assertNotNull(response);
        assertEquals("Corte", response.getNombre());
    }

    @Test
    void defaultConstructor_ShouldWork() {
        ServicioResponse response = new ServicioResponse();

        assertNull(response.getId());
        assertNull(response.getNombre());
    }

    @Test
    void setters_ShouldWork() {
        ServicioResponse response = new ServicioResponse();
        response.setId(1L);
        response.setNombre("Corte");
        response.setDescripcion("Desc");
        response.setPrecio(15000.0);
        response.setDuracionMinutos(30);
        response.setActivo(true);
        response.setBarberiaId(1L);
        response.setBarberiaNombre("BarberShop");

        assertEquals(1L, response.getId());
        assertEquals("Corte", response.getNombre());
        assertEquals("Desc", response.getDescripcion());
        assertEquals(15000.0, response.getPrecio());
        assertEquals(30, response.getDuracionMinutos());
        assertTrue(response.getActivo());
        assertEquals(1L, response.getBarberiaId());
        assertEquals("BarberShop", response.getBarberiaNombre());
    }
}
