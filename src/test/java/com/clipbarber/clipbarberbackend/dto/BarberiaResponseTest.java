package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.Barberia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BarberiaResponseTest {

    @Test
    void fromBarberia_ShouldMapAllFields() {
        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop Premium");
        barberia.setDireccion("Av. Principal 123");
        barberia.setTelefono("+56912345678");
        barberia.setEmail("shop@example.com");
        barberia.setImagenUrl("url.jpg");
        barberia.setLatitud(-33.4);
        barberia.setLongitud(-70.6);
        barberia.setActiva(true);

        BarberiaResponse response = BarberiaResponse.fromBarberia(barberia);

        assertEquals(1L, response.getId());
        assertEquals("BarberShop Premium", response.getNombre());
        assertEquals("Av. Principal 123", response.getDireccion());
        assertEquals("+56912345678", response.getTelefono());
        assertEquals("shop@example.com", response.getEmail());
        assertEquals("url.jpg", response.getImagenUrl());
        assertEquals(-33.4, response.getLatitud());
        assertEquals(-70.6, response.getLongitud());
        assertTrue(response.getActiva());
    }

    @Test
    void fromBarberia_ShouldHandleNullOptionalFields() {
        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop");
        barberia.setDireccion("Av. Principal");
        barberia.setLatitud(-33.4);
        barberia.setLongitud(-70.6);

        BarberiaResponse response = BarberiaResponse.fromBarberia(barberia);

        assertNull(response.getTelefono());
        assertNull(response.getEmail());
        assertNull(response.getImagenUrl());
    }

    @Test
    void defaultConstructor_ShouldWork() {
        BarberiaResponse response = new BarberiaResponse();

        assertNull(response.getId());
        assertNull(response.getNombre());
    }

    @Test
    void setters_ShouldWork() {
        BarberiaResponse response = new BarberiaResponse();
        response.setId(1L);
        response.setNombre("BarberShop");
        response.setDireccion("Av. Principal");
        response.setTelefono("+56912345678");
        response.setEmail("shop@example.com");
        response.setImagenUrl("url.jpg");
        response.setLatitud(-33.4);
        response.setLongitud(-70.6);
        response.setActiva(true);

        assertEquals(1L, response.getId());
        assertEquals("BarberShop", response.getNombre());
        assertTrue(response.getActiva());
    }
}
