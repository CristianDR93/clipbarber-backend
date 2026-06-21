package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.CitaRequest;
import com.clipbarber.clipbarberbackend.model.Barbero;
import com.clipbarber.clipbarberbackend.model.Cita;
import com.clipbarber.clipbarberbackend.model.Servicio;
import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.repository.BarberoRepository;
import com.clipbarber.clipbarberbackend.repository.CitaRepository;
import com.clipbarber.clipbarberbackend.repository.ServicioRepository;
import com.clipbarber.clipbarberbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final UserRepository userRepository;
    private final BarberoRepository barberoRepository;
    private final ServicioRepository servicioRepository;

    public CitaService(CitaRepository citaRepository, UserRepository userRepository,
                       BarberoRepository barberoRepository, ServicioRepository servicioRepository) {
        this.citaRepository = citaRepository;
        this.userRepository = userRepository;
        this.barberoRepository = barberoRepository;
        this.servicioRepository = servicioRepository;
    }

    public Cita createCita(Long clienteId, CitaRequest request) {
        User cliente = userRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Error: Cliente no encontrado"));

        Barbero barbero = barberoRepository.findById(request.getBarberoId())
                .orElseThrow(() -> new RuntimeException("Error: Barbero no encontrado"));

        Servicio servicio = servicioRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Error: Servicio no encontrado"));

        List<Cita> citasExistentes = citaRepository.findByBarberoIdAndFechaHoraBetween(
                request.getBarberoId(),
                request.getFechaHora().minusMinutes(servicio.getDuracionMinutos()),
                request.getFechaHora().plusMinutes(servicio.getDuracionMinutos())
        );

        boolean horarioOcupado = citasExistentes.stream()
                .anyMatch(cita -> cita.getEstado() != Cita.EstadoCita.CANCELADA);

        if (horarioOcupado) {
            throw new RuntimeException("Error: El barbero no esta disponible en ese horario");
        }

        Cita cita = new Cita();
        cita.setCliente(cliente);
        cita.setBarbero(barbero);
        cita.setServicio(servicio);
        cita.setFechaHora(request.getFechaHora());
        cita.setObservaciones(request.getObservaciones());
        cita.setPrecio(request.getPrecio());
        cita.setEstado(Cita.EstadoCita.PENDIENTE);

        return citaRepository.save(cita);
    }

    public List<Cita> getCitasByCliente(Long clienteId) {
        return citaRepository.findByClienteId(clienteId);
    }

    public List<Cita> getCitasByBarbero(Long barberoId) {
        return citaRepository.findByBarberoId(barberoId);
    }

    public Cita getCitaById(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Cita no encontrada"));
    }

    public Cita updateEstadoCita(Long id, Cita.EstadoCita nuevoEstado) {
        Cita cita = getCitaById(id);
        cita.setEstado(nuevoEstado);
        return citaRepository.save(cita);
    }

    public Cita cancelarCita(Long id) {
        return updateEstadoCita(id, Cita.EstadoCita.CANCELADA);
    }

    public Cita confirmarCita(Long id) {
        return updateEstadoCita(id, Cita.EstadoCita.CONFIRMADA);
    }

    public void deleteCita(Long id) {
        Cita cita = getCitaById(id);
        cita.setEstado(Cita.EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }
}
