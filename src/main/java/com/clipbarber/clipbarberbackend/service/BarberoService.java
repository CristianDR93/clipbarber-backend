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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarberoService {

    private final BarberoRepository barberoRepository;
    private final UserRepository userRepository;
    private final BarberiaRepository barberiaRepository;
    private final ServicioRepository servicioRepository;

    public BarberoService(BarberoRepository barberoRepository, UserRepository userRepository,
                          BarberiaRepository barberiaRepository, ServicioRepository servicioRepository) {
        this.barberoRepository = barberoRepository;
        this.userRepository = userRepository;
        this.barberiaRepository = barberiaRepository;
        this.servicioRepository = servicioRepository;
    }

    public Barbero createBarbero(BarberoRequest request) {
        User usuario = userRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado"));

        Barberia barberia = barberiaRepository.findById(request.getBarberiaId())
                .orElseThrow(() -> new RuntimeException("Error: Barberia no encontrada"));

        barberoRepository.findByUsuarioIdAndBarberiaId(request.getUsuarioId(), request.getBarberiaId())
                .ifPresent(b -> { throw new RuntimeException("Error: El usuario ya es barbero en esta barberia"); });

        Barbero barbero = new Barbero();
        barbero.setUsuario(usuario);
        barbero.setBarberia(barberia);
        barbero.setEspecialidad(request.getEspecialidad());
        barbero.setHorarios(request.getHorarios() != null ? request.getHorarios() : new ArrayList<>());
        barbero.setActivo(true);

        if (request.getServiciosIds() != null && !request.getServiciosIds().isEmpty()) {
            List<Servicio> servicios = servicioRepository.findAllById(request.getServiciosIds());
            barbero.setServicios(servicios);
        } else {
            barbero.setServicios(new ArrayList<>());
        }

        return barberoRepository.save(barbero);
    }

    public List<Barbero> getBarberosByBarberia(Long barberiaId) {
        return barberoRepository.findByBarberiaIdAndActivoTrue(barberiaId);
    }

    public Barbero getBarberoById(Long id) {
        return barberoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Barbero no encontrado"));
    }

    public List<Barbero> getBarberosByUsuario(Long usuarioId) {
        return barberoRepository.findByUsuarioId(usuarioId);
    }

    public Barbero updateBarbero(Long id, BarberoRequest request) {
        Barbero barbero = getBarberoById(id);

        if (request.getEspecialidad() != null) {
            barbero.setEspecialidad(request.getEspecialidad());
        }

        if (request.getHorarios() != null) {
            barbero.setHorarios(request.getHorarios());
        }

        if (request.getServiciosIds() != null) {
            List<Servicio> servicios = servicioRepository.findAllById(request.getServiciosIds());
            barbero.setServicios(servicios);
        }

        return barberoRepository.save(barbero);
    }

    public void deleteBarbero(Long id) {
        Barbero barbero = getBarberoById(id);
        barbero.setActivo(false);
        barberoRepository.save(barbero);
    }
}
