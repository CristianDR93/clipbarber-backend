package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.CitaRequest;
import com.clipbarber.clipbarberbackend.model.*;
import com.clipbarber.clipbarberbackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BarberoRepository barberoRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private CitaService citaService;

    private User cliente;
    private Barbero barbero;
    private Servicio servicio;
    private Cita cita;
    private CitaRequest citaRequest;

    @BeforeEach
    void setUp() {
        cliente = new User();
        cliente.setId(1L);
        cliente.setName("Juan Perez");
        cliente.setEmail("juan@example.com");

        Barberia barberia = new Barberia();
        barberia.setId(1L);
        barberia.setNombre("BarberShop");

        barbero = new Barbero();
        barbero.setId(1L);
        barbero.setUsuario(cliente);
        barbero.setBarberia(barberia);

        servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Corte");
        servicio.setDuracionMinutos(30);
        servicio.setPrecio(15000.0);

        cita = new Cita();
        cita.setId(1L);
        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);
        cita.setFechaHora(LocalDateTime.now().plusDays(1));
        cita.setEstado(Cita.EstadoCita.PENDIENTE);
        cita.setPrecio(15000.0);

        citaRequest = new CitaRequest(1L, 1L, LocalDateTime.now().plusDays(1), "Sin observaciones", 15000.0);
    }

    @Test
    void createCita_ShouldSaveCita_WhenSlotAvailable() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(citaRepository.findByBarberoIdAndFechaHoraBetween(anyLong(), any(), any())).thenReturn(Collections.emptyList());
        when(citaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Cita result = citaService.createCita(1L, citaRequest);

        assertNotNull(result);
        assertEquals(Cita.EstadoCita.PENDIENTE, result.getEstado());
        assertEquals(15000.0, result.getPrecio());
        verify(citaRepository).save(any());
    }

    @Test
    void createCita_ShouldThrowException_WhenSlotOccupied() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(citaRepository.findByBarberoIdAndFechaHoraBetween(anyLong(), any(), any())).thenReturn(List.of(cita));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> citaService.createCita(1L, citaRequest));

        assertEquals("Error: El barbero no esta disponible en ese horario", exception.getMessage());
    }

    @Test
    void createCita_ShouldThrowException_WhenClienteNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> citaService.createCita(99L, citaRequest));

        assertEquals("Error: Cliente no encontrado", exception.getMessage());
    }

    @Test
    void createCita_ShouldThrowException_WhenBarberoNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(barberoRepository.findById(99L)).thenReturn(Optional.empty());
        CitaRequest badRequest = new CitaRequest(99L, 1L, LocalDateTime.now().plusDays(1), null, 15000.0);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> citaService.createCita(1L, badRequest));

        assertEquals("Error: Barbero no encontrado", exception.getMessage());
    }

    @Test
    void getCitasByCliente_ShouldReturnCitas() {
        when(citaRepository.findByClienteId(1L)).thenReturn(List.of(cita));

        List<Cita> result = citaService.getCitasByCliente(1L);

        assertEquals(1, result.size());
        verify(citaRepository).findByClienteId(1L);
    }

    @Test
    void getCitasByBarbero_ShouldReturnCitas() {
        when(citaRepository.findByBarberoId(1L)).thenReturn(List.of(cita));

        List<Cita> result = citaService.getCitasByBarbero(1L);

        assertEquals(1, result.size());
        verify(citaRepository).findByBarberoId(1L);
    }

    @Test
    void getCitaById_ShouldReturnCita_WhenExists() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        Cita result = citaService.getCitaById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCitaById_ShouldThrowException_WhenNotExists() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> citaService.getCitaById(99L));

        assertEquals("Error: Cita no encontrada", exception.getMessage());
    }

    @Test
    void confirmarCita_ShouldChangeStateToConfirmada() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Cita result = citaService.confirmarCita(1L);

        assertEquals(Cita.EstadoCita.CONFIRMADA, result.getEstado());
        verify(citaRepository).save(any());
    }

    @Test
    void cancelarCita_ShouldChangeStateToCancelada() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Cita result = citaService.cancelarCita(1L);

        assertEquals(Cita.EstadoCita.CANCELADA, result.getEstado());
        verify(citaRepository).save(any());
    }

    @Test
    void deleteCita_ShouldSoftDelete() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        citaService.deleteCita(1L);

        assertEquals(Cita.EstadoCita.CANCELADA, cita.getEstado());
        verify(citaRepository).save(any());
    }
}
