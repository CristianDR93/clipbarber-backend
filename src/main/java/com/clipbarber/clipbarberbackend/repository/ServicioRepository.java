package com.clipbarber.clipbarberbackend.repository;

import com.clipbarber.clipbarberbackend.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    List<Servicio> findByBarberiaIdAndActivoTrue(Long barberiaId);

    List<Servicio> findByActivoTrue();
}
