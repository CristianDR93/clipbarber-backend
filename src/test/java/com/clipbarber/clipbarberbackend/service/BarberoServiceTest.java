package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.BarberoRequest;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.model.Barbero;
import com.clipbarber.clipbarberbackend.model.Servicio;
import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.repository.BarberiaRepository;
import com.clipbarber.clipbarberbackend.repository.BarberoRepository;
import com.clipbarber.clipbarberbackend.repository.ServicioRepository;
import com.clipbarber.clipbarberbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberoServiceTest {

    @Mock
    private BarberoRepository barberoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BarberiaRepository barberiaRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private BarberoService barberoService;

    private User usuario;
    private Barberia barberia;
    private Barbero barbero;
    private BarberoRequest request;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setName("Pedro Barbero");
        usuario.setEmail("pedro@example.com");

        barberia = new Barberia();
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
    void createBarbero_ShouldSaveBarbero() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(barberiaRepository.findById(1L)).thenReturn(Optional.of(barberia));
        when(barberoRepository.findByUsuarioIdAndBarberiaId(1L, 1L)).thenReturn(Optional.empty());
        when(barberoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Barbero result = barberoService.createBarbero(request);

        assertNotNull(result);
        assertEquals("Corte moderno", result.getEspecialidad());
        assertTrue(result.getActivo());
        verify(barberoRepository).save(any());
    }

    @Test
    void createBarbero_ShouldThrowException_WhenUserAlreadyBarberInBarberia() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(barberiaRepository.findById(1L)).thenReturn(Optional.of(barberia));
        when(barberoRepository.findByUsuarioIdAndBarberiaId(1L, 1L)).thenReturn(Optional.of(barbero));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> barberoService.createBarbero(request));

        assertEquals("Error: El usuario ya es barbero en esta barberia", exception.getMessage());
    }

    @Test
    void createBarbero_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        BarberoRequest badRequest = new BarberoRequest(99L, 1L, "Espec", null, null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> barberoService.createBarbero(badRequest));

        assertEquals("Error: Usuario no encontrado", exception.getMessage());
    }

    @Test
    void getBarberosByBarberia_ShouldReturnActiveBarberos() {
        when(barberoRepository.findByBarberiaIdAndActivoTrue(1L)).thenReturn(List.of(barbero));

        List<Barbero> result = barberoService.getBarberosByBarberia(1L);

        assertEquals(1, result.size());
        verify(barberoRepository).findByBarberiaIdAndActivoTrue(1L);
    }

    @Test
    void getBarberoById_ShouldReturnBarbero_WhenExists() {
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));

        Barbero result = barberoService.getBarberoById(1L);

        assertNotNull(result);
        assertEquals("Corte moderno", result.getEspecialidad());
    }

    @Test
    void getBarberoById_ShouldThrowException_WhenNotExists() {
        when(barberoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> barberoService.getBarberoById(99L));

        assertEquals("Error: Barbero no encontrado", exception.getMessage());
    }

    @Test
    void updateBarbero_ShouldUpdateFields() {
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));
        when(barberoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BarberoRequest updateRequest = new BarberoRequest(1L, 1L, "Corte y barba", List.of("10:00-20:00"), null);
        Barbero result = barberoService.updateBarbero(1L, updateRequest);

        assertEquals("Corte y barba", result.getEspecialidad());
        verify(barberoRepository).save(any());
    }

    @Test
    void deleteBarbero_ShouldSoftDelete() {
        when(barberoRepository.findById(1L)).thenReturn(Optional.of(barbero));
        when(barberoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        barberoService.deleteBarbero(1L);

        assertFalse(barbero.getActivo());
        verify(barberoRepository).save(any());
    }
}
