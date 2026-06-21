package com.clipbarber.clipbarberbackend.repository;

import com.clipbarber.clipbarberbackend.model.Barberia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarberiaRepository extends JpaRepository<Barberia, Long> {

    List<Barberia> findByActivaTrue();

    List<Barberia> findByNombreContainingIgnoreCase(String nombre);
}
