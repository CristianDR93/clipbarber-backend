package com.clipbarber.clipbarberbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "barberia")
@Getter
@Setter
public class Barberia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    private String telefono;

    private String email;

    private String imagenUrl;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(nullable = false)
    private Boolean activa = true;

    @OneToMany(mappedBy = "barberia", cascade = CascadeType.ALL)
    private List<Barbero> barberos;

    @OneToMany(mappedBy = "barberia", cascade = CascadeType.ALL)
    private List<Servicio> servicios;
}
