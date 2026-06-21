package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.BarberiaRequest;
import com.clipbarber.clipbarberbackend.dto.BarberiaResponse;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.service.BarberiaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberiaControllerTest {

    @Mock
    private BarberiaService barberiaService;

    @InjectMocks
    private BarberiaController barberiaController;

    private Barberia barberia;
    private BarberiaRequest request;

    @BeforeEach
    void setUp() {
        barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop Premium");
        barberia.setDireccion("Av. Principal 123");
        barberia.setTelefono("+56912345678");
        barberia.setEmail("info@barbershop.com");
        barberia.setLatitud(-33.4489);
        barberia.setLongitud(-70.6693);
        barberia.setActiva(true);

        request = new BarberiaRequest("BarberShop Premium", "Av. Principal 123", "+56912345678", "info@barbershop.com", null, -33.4489, -70.6693);
    }

    @Test
    void createBarberia_ShouldReturnCreatedBarberia() {
        when(barberiaService.createBarberia(request)).thenReturn(barberia);

        ResponseEntity<?> response = barberiaController.createBarberia(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(BarberiaResponse.class, response.getBody());
        assertEquals("BarberShop Premium", ((BarberiaResponse) response.getBody()).getNombre());
    }

    @Test
    void createBarberia_ShouldReturnBadRequest_WhenError() {
        when(barberiaService.createBarberia(request)).thenThrow(new RuntimeException("Error: Nombre duplicado"));

        ResponseEntity<?> response = barberiaController.createBarberia(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Nombre duplicado", response.getBody());
    }

    @Test
    void getAllBarberias_ShouldReturnList() {
        when(barberiaService.getAllBarberias()).thenReturn(List.of(barberia));

        ResponseEntity<List<BarberiaResponse>> response = barberiaController.getAllBarberias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getBarberiaById_ShouldReturnBarberia_WhenExists() {
        when(barberiaService.getBarberiaById(1L)).thenReturn(barberia);

        ResponseEntity<?> response = barberiaController.getBarberiaById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(BarberiaResponse.class, response.getBody());
    }

    @Test
    void getBarberiaById_ShouldReturnBadRequest_WhenNotExists() {
        when(barberiaService.getBarberiaById(99L)).thenThrow(new RuntimeException("Error: Barberia no encontrada"));

        ResponseEntity<?> response = barberiaController.getBarberiaById(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Barberia no encontrada", response.getBody());
    }

    @Test
    void updateBarberia_ShouldReturnUpdatedBarberia() {
        when(barberiaService.updateBarberia(1L, request)).thenReturn(barberia);

        ResponseEntity<?> response = barberiaController.updateBarberia(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(BarberiaResponse.class, response.getBody());
    }

    @Test
    void deleteBarberia_ShouldReturnNoContent() {
        doNothing().when(barberiaService).deleteBarberia(1L);

        ResponseEntity<?> response = barberiaController.deleteBarberia(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(barberiaService).deleteBarberia(1L);
    }
}
