package com.clipbarber.clipbarberbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "barbero")
@Getter
@Setter
public class Barbero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barberia_id", nullable = false)
    private Barberia barberia;

    @Column(nullable = false)
    private Boolean activo = true;

    private String especialidad;

    @ElementCollection
    @CollectionTable(name = "barbero_horarios", joinColumns = @JoinColumn(name = "barbero_id"))
    @Column(name = "horario")
    private List<String> horarios;

    @ManyToMany
    @JoinTable(
        name = "barbero_servicios",
        joinColumns = @JoinColumn(name = "barbero_id"),
        inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private List<Servicio> servicios;
}
