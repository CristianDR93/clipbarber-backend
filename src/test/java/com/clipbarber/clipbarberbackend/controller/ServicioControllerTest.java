package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.ServicioRequest;
import com.clipbarber.clipbarberbackend.dto.ServicioResponse;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.model.Servicio;
import com.clipbarber.clipbarberbackend.service.ServicioService;
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
class ServicioControllerTest {

    @Mock
    private ServicioService servicioService;

    @InjectMocks
    private ServicioController servicioController;

    private Servicio servicio;
    private ServicioRequest request;

    @BeforeEach
    void setUp() {
        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop Premium");

        servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Corte de Cabello");
        servicio.setDescripcion("Corte moderno");
        servicio.setPrecio(15000.0);
        servicio.setDuracionMinutos(30);
        servicio.setActivo(true);
        servicio.setBarberia(barberia);

        request = new ServicioRequest("Corte de Cabello", "Corte moderno", 15000.0, 30, 1L);
    }

    @Test
    void createServicio_ShouldReturnServicio() {
        when(servicioService.createServicio(request)).thenReturn(servicio);

        ResponseEntity<?> response = servicioController.createServicio(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(ServicioResponse.class, response.getBody());
        assertEquals("Corte de Cabello", ((ServicioResponse) response.getBody()).getNombre());
    }

    @Test
    void createServicio_ShouldReturnBadRequest_WhenServiceThrows() {
        when(servicioService.createServicio(request)).thenThrow(new RuntimeException("Error: Barberia no encontrada"));

        ResponseEntity<?> response = servicioController.createServicio(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Barberia no encontrada", response.getBody());
    }

    @Test
    void getAllServicios_ShouldReturnList() {
        when(servicioService.getAllServicios()).thenReturn(List.of(servicio));

        ResponseEntity<List<ServicioResponse>> response = servicioController.getAllServicios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getServiciosByBarberia_ShouldReturnList() {
        when(servicioService.getServiciosByBarberia(1L)).thenReturn(List.of(servicio));

        ResponseEntity<List<ServicioResponse>> response = servicioController.getServiciosByBarberia(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getServicioById_ShouldReturnServicio_WhenExists() {
        when(servicioService.getServicioById(1L)).thenReturn(servicio);

        ResponseEntity<?> response = servicioController.getServicioById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(ServicioResponse.class, response.getBody());
    }

    @Test
    void getServicioById_ShouldReturnBadRequest_WhenNotExists() {
        when(servicioService.getServicioById(99L)).thenThrow(new RuntimeException("Error: Servicio no encontrado"));

        ResponseEntity<?> response = servicioController.getServicioById(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Servicio no encontrado", response.getBody());
    }

    @Test
    void updateServicio_ShouldReturnUpdatedServicio() {
        when(servicioService.updateServicio(1L, request)).thenReturn(servicio);

        ResponseEntity<?> response = servicioController.updateServicio(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(ServicioResponse.class, response.getBody());
    }

    @Test
    void deleteServicio_ShouldReturnNoContent() {
        doNothing().when(servicioService).deleteServicio(1L);

        ResponseEntity<?> response = servicioController.deleteServicio(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteServicio_ShouldReturnBadRequest_WhenNotExists() {
        doThrow(new RuntimeException("Error: Servicio no encontrado")).when(servicioService).deleteServicio(99L);

        ResponseEntity<?> response = servicioController.deleteServicio(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Servicio no encontrado", response.getBody());
    }
}
