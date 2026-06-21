package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.ServicioRequest;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.model.Servicio;
import com.clipbarber.clipbarberbackend.repository.BarberiaRepository;
import com.clipbarber.clipbarberbackend.repository.ServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private BarberiaRepository barberiaRepository;

    @InjectMocks
    private ServicioService servicioService;

    private Servicio servicio;
    private Barberia barberia;
    private ServicioRequest request;

    @BeforeEach
    void setUp() {
        barberia = new Barberia();
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
    void createServicio_ShouldSaveServicio() {
        when(barberiaRepository.findById(1L)).thenReturn(Optional.of(barberia));
        when(servicioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Servicio result = servicioService.createServicio(request);

        assertNotNull(result);
        assertEquals("Corte de Cabello", result.getNombre());
        assertEquals(15000.0, result.getPrecio());
        assertTrue(result.getActivo());
        assertEquals(barberia, result.getBarberia());
        verify(servicioRepository).save(any());
    }

    @Test
    void createServicio_ShouldThrowException_WhenBarberiaNotFound() {
        when(barberiaRepository.findById(99L)).thenReturn(Optional.empty());
        ServicioRequest badRequest = new ServicioRequest("Corte", "desc", 10000.0, 30, 99L);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> servicioService.createServicio(badRequest));

        assertEquals("Error: Barberia no encontrada", exception.getMessage());
    }

    @Test
    void getAllServicios_ShouldReturnAll() {
        when(servicioRepository.findAll()).thenReturn(List.of(servicio));

        List<Servicio> result = servicioService.getAllServicios();

        assertEquals(1, result.size());
        verify(servicioRepository).findAll();
    }

    @Test
    void getServiciosByBarberia_ShouldReturnActiveServices() {
        when(servicioRepository.findByBarberiaIdAndActivoTrue(1L)).thenReturn(List.of(servicio));

        List<Servicio> result = servicioService.getServiciosByBarberia(1L);

        assertEquals(1, result.size());
        verify(servicioRepository).findByBarberiaIdAndActivoTrue(1L);
    }

    @Test
    void getServicioById_ShouldReturnServicio_WhenExists() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));

        Servicio result = servicioService.getServicioById(1L);

        assertNotNull(result);
        assertEquals("Corte de Cabello", result.getNombre());
    }

    @Test
    void getServicioById_ShouldThrowException_WhenNotExists() {
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> servicioService.getServicioById(99L));

        assertEquals("Error: Servicio no encontrado", exception.getMessage());
    }

    @Test
    void updateServicio_ShouldUpdateFields() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(servicioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ServicioRequest updateRequest = new ServicioRequest("Corte Actualizado", "Nueva desc", 20000.0, 45, 1L);
        Servicio result = servicioService.updateServicio(1L, updateRequest);

        assertEquals("Corte Actualizado", result.getNombre());
        assertEquals(20000.0, result.getPrecio());
        assertEquals(45, result.getDuracionMinutos());
        verify(servicioRepository).save(any());
    }

    @Test
    void deleteServicio_ShouldSoftDelete() {
        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(servicioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        servicioService.deleteServicio(1L);

        assertFalse(servicio.getActivo());
        verify(servicioRepository).save(any());
    }
}
