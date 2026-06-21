package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.CitaRequest;
import com.clipbarber.clipbarberbackend.dto.CitaResponse;
import com.clipbarber.clipbarberbackend.model.*;
import com.clipbarber.clipbarberbackend.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaControllerTest {

    @Mock
    private CitaService citaService;

    @InjectMocks
    private CitaController citaController;

    private Cita cita;
    private CitaRequest citaRequest;

    @BeforeEach
    void setUp() {
        User cliente = new User();
        cliente.setId(1L);
        cliente.setName("Juan Perez");

        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop");

        User barberoUser = new User();
        barberoUser.setId(2L);
        barberoUser.setName("Pedro Barbero");

        Barbero barbero = new Barbero();
        barbero.setId(1L);
        barbero.setUsuario(barberoUser);
        barbero.setBarberia(barberia);

        Servicio servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Corte");

        cita = new Cita();
        cita.setId(1L);
        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);
        cita.setFechaHora(LocalDateTime.now().plusDays(1));
        cita.setEstado(Cita.EstadoCita.PENDIENTE);
        cita.setPrecio(15000.0);

        citaRequest = new CitaRequest(1L, 1L, LocalDateTime.now().plusDays(1), null, 15000.0);
    }

    @Test
    void createCita_ShouldReturnCita() {
        when(citaService.createCita(1L, citaRequest)).thenReturn(cita);

        ResponseEntity<?> response = citaController.createCita(1L, citaRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(CitaResponse.class, response.getBody());
        assertEquals(1L, ((CitaResponse) response.getBody()).getClienteId());
    }

    @Test
    void createCita_ShouldReturnBadRequest_WhenSlotOccupied() {
        when(citaService.createCita(1L, citaRequest))
                .thenThrow(new RuntimeException("Error: El barbero no esta disponible en ese horario"));

        ResponseEntity<?> response = citaController.createCita(1L, citaRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: El barbero no esta disponible en ese horario", response.getBody());
    }

    @Test
    void getCitasByCliente_ShouldReturnList() {
        when(citaService.getCitasByCliente(1L)).thenReturn(List.of(cita));

        ResponseEntity<List<CitaResponse>> response = citaController.getCitasByCliente(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCitasByBarbero_ShouldReturnList() {
        when(citaService.getCitasByBarbero(1L)).thenReturn(List.of(cita));

        ResponseEntity<List<CitaResponse>> response = citaController.getCitasByBarbero(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCitaById_ShouldReturnCita_WhenExists() {
        when(citaService.getCitaById(1L)).thenReturn(cita);

        ResponseEntity<?> response = citaController.getCitaById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(CitaResponse.class, response.getBody());
    }

    @Test
    void getCitaById_ShouldReturnBadRequest_WhenNotExists() {
        when(citaService.getCitaById(99L)).thenThrow(new RuntimeException("Error: Cita no encontrada"));

        ResponseEntity<?> response = citaController.getCitaById(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Cita no encontrada", response.getBody());
    }

    @Test
    void confirmarCita_ShouldReturnConfirmedCita() {
        when(citaService.confirmarCita(1L)).thenReturn(cita);

        ResponseEntity<?> response = citaController.confirmarCita(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(CitaResponse.class, response.getBody());
    }

    @Test
    void cancelarCita_ShouldReturnCancelledCita() {
        when(citaService.cancelarCita(1L)).thenReturn(cita);

        ResponseEntity<?> response = citaController.cancelarCita(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(CitaResponse.class, response.getBody());
    }

    @Test
    void deleteCita_ShouldReturnNoContent() {
        doNothing().when(citaService).deleteCita(1L);

        ResponseEntity<?> response = citaController.deleteCita(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteCita_ShouldReturnBadRequest_WhenNotExists() {
        doThrow(new RuntimeException("Error: Cita no encontrada")).when(citaService).deleteCita(99L);

        ResponseEntity<?> response = citaController.deleteCita(99L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Cita no encontrada", response.getBody());
    }
}
