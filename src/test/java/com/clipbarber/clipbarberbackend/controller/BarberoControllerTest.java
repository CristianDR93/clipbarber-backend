package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.BarberoRequest;
import com.clipbarber.clipbarberbackend.dto.BarberoResponse;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.model.Barbero;
import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.service.BarberoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberoControllerTest {

    @Mock
    private BarberoService barberoService;

    @InjectMocks
    private BarberoController barberoController;

    private Barbero barbero;
    private BarberoRequest request;

    @BeforeEach
    void setUp() {
        User usuario = new User();
        usuario.setId(1L);
        usuario.setName("Pedro Barbero");
        usuario.setEmail("pedro@example.com");

        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop Premium");

        barbero = new Barbero();
        barbero.setId(1L);
        barbero.setUsuario(usuario);
        barbero.setBarberia(barberia);
        barbero.setEspecialidad("Corte moderno");
        barbero.setActivo(true);
        barbero.setHorarios(new ArrayList<>());
        barbero.setServicios(new ArrayList<>());

        request = new BarberoRequest(1L, 1L, "Corte moderno", List.of("09:00-18:00"), null);
    }

    @Test
    void createBarbero_ShouldReturnBarbero() {
        when(barberoService.createBarbero(request)).thenReturn(barbero);

        ResponseEntity<?> response = barberoController.createBarbero(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(BarberoResponse.class, response.getBody());
        assertEquals("Corte moderno", ((BarberoResponse) response.getBody()).getEspecialidad());
    }

    @Test
    void createBarbero_ShouldReturnBadRequest_WhenDuplicate() {
        when(barberoService.createBarbero(request))
                .thenThrow(new RuntimeException("Error: El usuario ya es barbero en esta barberia"));

        ResponseEntity<?> response = barberoController.createBarbero(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: El usuario ya es barbero en esta barberia", response.getBody());
    }

    @Test
    void getBarberosByBarberia_ShouldReturnList() {
        when(barberoService.getBarberosByBarberia(1L)).thenReturn(List.of(barbero));

        ResponseEntity<List<BarberoResponse>> response = barberoController.getBarberosByBarberia(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBarberosByUsuario_ShouldReturnList() {
        when(barberoService.getBarberosByUsuario(1L)).thenReturn(List.of(barbero));

        ResponseEntity<List<BarberoResponse>> response = barberoController.getBarberosByUsuario(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBarberoById_ShouldReturnBarbero_WhenExists() {
        when(barberoService.getBarberoById(1L)).thenReturn(barbero);

        ResponseEntity<?> response = barberoController.getBarberoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(BarberoResponse.class, response.getBody());
    }

    @Test
    void getBarberoById_ShouldReturnBadRequest_WhenNotExists() {
        when(barberoService.getBarberoById(99L)).thenThrow(new RuntimeException("Error: Barbero no encontrado"));

        ResponseEntity<?> response = barberoController.getBarberoById(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Barbero no encontrado", response.getBody());
    }

    @Test
    void updateBarbero_ShouldReturnUpdatedBarbero() {
        when(barberoService.updateBarbero(1L, request)).thenReturn(barbero);

        ResponseEntity<?> response = barberoController.updateBarbero(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(BarberoResponse.class, response.getBody());
    }

    @Test
    void deleteBarbero_ShouldReturnNoContent() {
        doNothing().when(barberoService).deleteBarbero(1L);

        ResponseEntity<?> response = barberoController.deleteBarbero(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteBarbero_ShouldReturnBadRequest_WhenNotExists() {
        doThrow(new RuntimeException("Error: Barbero no encontrado")).when(barberoService).deleteBarbero(99L);

        ResponseEntity<?> response = barberoController.deleteBarbero(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Barbero no encontrado", response.getBody());
    }
}
