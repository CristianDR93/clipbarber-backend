package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.BarberiaRequest;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.repository.BarberiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberiaService {

    private final BarberiaRepository barberiaRepository;

    public BarberiaService(BarberiaRepository barberiaRepository) {
        this.barberiaRepository = barberiaRepository;
    }

    public Barberia createBarberia(BarberiaRequest request) {
        Barberia barberia = new Barberia();
        barberia.setNombre(request.getNombre());
        barberia.setDireccion(request.getDireccion());
        barberia.setTelefono(request.getTelefono());
        barberia.setEmail(request.getEmail());
        barberia.setImagenUrl(request.getImagenUrl());
        barberia.setLatitud(request.getLatitud());
        barberia.setLongitud(request.getLongitud());
        barberia.setActiva(true);

        return barberiaRepository.save(barberia);
    }

    public List<Barberia> getAllBarberias() {
        return barberiaRepository.findAll();
    }

    public List<Barberia> getBarberiasActivas() {
        return barberiaRepository.findByActivaTrue();
    }

    public Barberia getBarberiaById(Long id) {
        return barberiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Barberia no encontrada"));
    }

    public List<Barberia> searchBarberias(String nombre) {
        return barberiaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Barberia updateBarberia(Long id, BarberiaRequest request) {
        Barberia barberia = getBarberiaById(id);

        barberia.setNombre(request.getNombre());
        barberia.setDireccion(request.getDireccion());
        barberia.setTelefono(request.getTelefono());
        barberia.setEmail(request.getEmail());
        barberia.setImagenUrl(request.getImagenUrl());
        barberia.setLatitud(request.getLatitud());
        barberia.setLongitud(request.getLongitud());

        return barberiaRepository.save(barberia);
    }

    public void deleteBarberia(Long id) {
        Barberia barberia = getBarberiaById(id);
        barberia.setActiva(false);
        barberiaRepository.save(barberia);
    }
}
