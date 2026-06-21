package com.clipbarber.clipbarberbackend.repository;

import com.clipbarber.clipbarberbackend.model.Barbero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarberoRepository extends JpaRepository<Barbero, Long> {

    List<Barbero> findByBarberiaIdAndActivoTrue(Long barberiaId);

    Optional<Barbero> findByUsuarioIdAndBarberiaId(Long usuarioId, Long barberiaId);

    List<Barbero> findByUsuarioId(Long usuarioId);
}
