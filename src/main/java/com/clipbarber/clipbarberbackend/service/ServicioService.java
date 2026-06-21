package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.ServicioRequest;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.model.Servicio;
import com.clipbarber.clipbarberbackend.repository.BarberiaRepository;
import com.clipbarber.clipbarberbackend.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final BarberiaRepository barberiaRepository;

    public ServicioService(ServicioRepository servicioRepository, BarberiaRepository barberiaRepository) {
        this.servicioRepository = servicioRepository;
        this.barberiaRepository = barberiaRepository;
    }

    public Servicio createServicio(ServicioRequest request) {
        Barberia barberia = barberiaRepository.findById(request.getBarberiaId())
                .orElseThrow(() -> new RuntimeException("Error: Barberia no encontrada"));

        Servicio servicio = new Servicio();
        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setPrecio(request.getPrecio());
        servicio.setDuracionMinutos(request.getDuracionMinutos());
        servicio.setBarberia(barberia);
        servicio.setActivo(true);

        return servicioRepository.save(servicio);
    }

    public List<Servicio> getAllServicios() {
        return servicioRepository.findAll();
    }

    public List<Servicio> getServiciosByBarberia(Long barberiaId) {
        return servicioRepository.findByBarberiaIdAndActivoTrue(barberiaId);
    }

    public Servicio getServicioById(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Servicio no encontrado"));
    }

    public Servicio updateServicio(Long id, ServicioRequest request) {
        Servicio servicio = getServicioById(id);

        if (request.getBarberiaId() != null && !request.getBarberiaId().equals(servicio.getBarberia().getId())) {
            Barberia barberia = barberiaRepository.findById(request.getBarberiaId())
                    .orElseThrow(() -> new RuntimeException("Error: Barberia no encontrada"));
            servicio.setBarberia(barberia);
        }

        servicio.setNombre(request.getNombre());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setPrecio(request.getPrecio());
        servicio.setDuracionMinutos(request.getDuracionMinutos());

        return servicioRepository.save(servicio);
    }

    public void deleteServicio(Long id) {
        Servicio servicio = getServicioById(id);
        servicio.setActivo(false);
        servicioRepository.save(servicio);
    }
}
