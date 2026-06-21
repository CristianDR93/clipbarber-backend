package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.BarberoRequest;
import com.clipbarber.clipbarberbackend.dto.BarberoResponse;
import com.clipbarber.clipbarberbackend.model.Barbero;
import com.clipbarber.clipbarberbackend.service.BarberoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/barberos")
public class BarberoController {

    private final BarberoService barberoService;

    public BarberoController(BarberoService barberoService) {
        this.barberoService = barberoService;
    }

    @PostMapping
    public ResponseEntity<?> createBarbero(@Valid @RequestBody BarberoRequest request) {
        try {
            Barbero barbero = barberoService.createBarbero(request);
            return ResponseEntity.ok(BarberoResponse.fromBarbero(barbero));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/barberia/{barberiaId}")
    public ResponseEntity<List<BarberoResponse>> getBarberosByBarberia(@PathVariable Long barberiaId) {
        List<BarberoResponse> barberos = barberoService.getBarberosByBarberia(barberiaId).stream()
                .map(BarberoResponse::fromBarbero)
                .collect(Collectors.toList());
        return ResponseEntity.ok(barberos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<BarberoResponse>> getBarberosByUsuario(@PathVariable Long usuarioId) {
        List<BarberoResponse> barberos = barberoService.getBarberosByUsuario(usuarioId).stream()
                .map(BarberoResponse::fromBarbero)
                .collect(Collectors.toList());
        return ResponseEntity.ok(barberos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBarberoById(@PathVariable Long id) {
        try {
            Barbero barbero = barberoService.getBarberoById(id);
            return ResponseEntity.ok(BarberoResponse.fromBarbero(barbero));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBarbero(@PathVariable Long id, @Valid @RequestBody BarberoRequest request) {
        try {
            Barbero barbero = barberoService.updateBarbero(id, request);
            return ResponseEntity.ok(BarberoResponse.fromBarbero(barbero));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBarbero(@PathVariable Long id) {
        try {
            barberoService.deleteBarbero(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
