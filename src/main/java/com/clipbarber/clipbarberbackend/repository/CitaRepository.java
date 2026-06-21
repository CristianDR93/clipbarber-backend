package com.clipbarber.clipbarberbackend.repository;

import com.clipbarber.clipbarberbackend.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByClienteId(Long clienteId);

    List<Cita> findByBarberoId(Long barberoId);

    List<Cita> findByBarberoIdAndFechaHoraBetween(Long barberoId, LocalDateTime inicio, LocalDateTime fin);

    List<Cita> findByClienteIdAndEstado(Long clienteId, Cita.EstadoCita estado);

    List<Cita> findByBarberoIdAndEstado(Long barberoId, Cita.EstadoCita estado);
}
