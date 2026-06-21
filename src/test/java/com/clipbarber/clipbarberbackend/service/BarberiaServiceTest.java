package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.BarberiaRequest;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.repository.BarberiaRepository;
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
class BarberiaServiceTest {

    @Mock
    private BarberiaRepository barberiaRepository;

    @InjectMocks
    private BarberiaService barberiaService;

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
    void createBarberia_ShouldSaveBarberia() {
        when(barberiaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Barberia result = barberiaService.createBarberia(request);

        assertNotNull(result);
        assertEquals("BarberShop Premium", result.getNombre());
        assertEquals("Av. Principal 123", result.getDireccion());
        assertTrue(result.getActiva());
        verify(barberiaRepository).save(any());
    }

    @Test
    void getAllBarberias_ShouldReturnAllBarberias() {
        when(barberiaRepository.findAll()).thenReturn(List.of(barberia));

        List<Barberia> result = barberiaService.getAllBarberias();

        assertEquals(1, result.size());
        verify(barberiaRepository).findAll();
    }

    @Test
    void getBarberiasActivas_ShouldReturnActiveBarberias() {
        when(barberiaRepository.findByActivaTrue()).thenReturn(List.of(barberia));

        List<Barberia> result = barberiaService.getBarberiasActivas();

        assertEquals(1, result.size());
        verify(barberiaRepository).findByActivaTrue();
    }

    @Test
    void getBarberiaById_ShouldReturnBarberia_WhenExists() {
        when(barberiaRepository.findById(1L)).thenReturn(Optional.of(barberia));

        Barberia result = barberiaService.getBarberiaById(1L);

        assertNotNull(result);
        assertEquals("BarberShop Premium", result.getNombre());
        verify(barberiaRepository).findById(1L);
    }

    @Test
    void getBarberiaById_ShouldThrowException_WhenNotExists() {
        when(barberiaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> barberiaService.getBarberiaById(99L));

        assertEquals("Error: Barberia no encontrada", exception.getMessage());
    }

    @Test
    void updateBarberia_ShouldUpdateBarberia() {
        when(barberiaRepository.findById(1L)).thenReturn(Optional.of(barberia));
        when(barberiaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BarberiaRequest updateRequest = new BarberiaRequest("BarberShop Actualizado", "NuevaDireccion 456", "+56999999999", "nuevo@email.com", null, -33.5, -70.7);
        Barberia result = barberiaService.updateBarberia(1L, updateRequest);

        assertEquals("BarberShop Actualizado", result.getNombre());
        assertEquals("NuevaDireccion 456", result.getDireccion());
        verify(barberiaRepository).save(any());
    }

    @Test
    void deleteBarberia_ShouldSoftDelete() {
        when(barberiaRepository.findById(1L)).thenReturn(Optional.of(barberia));
        when(barberiaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        barberiaService.deleteBarberia(1L);

        assertFalse(barberia.getActiva());
        verify(barberiaRepository).save(any());
    }
}
