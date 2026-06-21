package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.BarberiaRequest;
import com.clipbarber.clipbarberbackend.dto.BarberiaResponse;
import com.clipbarber.clipbarberbackend.model.Barberia;
import com.clipbarber.clipbarberbackend.service.BarberiaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/barberias")
public class BarberiaController {

    private final BarberiaService barberiaService;

    public BarberiaController(BarberiaService barberiaService) {
        this.barberiaService = barberiaService;
    }

    @PostMapping
    public ResponseEntity<?> createBarberia(@Valid @RequestBody BarberiaRequest request) {
        try {
            Barberia barberia = barberiaService.createBarberia(request);
            return ResponseEntity.ok(BarberiaResponse.fromBarberia(barberia));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BarberiaResponse>> getAllBarberias() {
        List<BarberiaResponse> barberias = barberiaService.getAllBarberias().stream()
                .map(BarberiaResponse::fromBarberia)
                .collect(Collectors.toList());
        return ResponseEntity.ok(barberias);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<BarberiaResponse>> getBarberiasActivas() {
        List<BarberiaResponse> barberias = barberiaService.getBarberiasActivas().stream()
                .map(BarberiaResponse::fromBarberia)
                .collect(Collectors.toList());
        return ResponseEntity.ok(barberias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBarberiaById(@PathVariable Long id) {
        try {
            Barberia barberia = barberiaService.getBarberiaById(id);
            return ResponseEntity.ok(BarberiaResponse.fromBarberia(barberia));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<BarberiaResponse>> searchBarberias(@RequestParam String nombre) {
        List<BarberiaResponse> barberias = barberiaService.searchBarberias(nombre).stream()
                .map(BarberiaResponse::fromBarberia)
                .collect(Collectors.toList());
        return ResponseEntity.ok(barberias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBarberia(@PathVariable Long id, @Valid @RequestBody BarberiaRequest request) {
        try {
            Barberia barberia = barberiaService.updateBarberia(id, request);
            return ResponseEntity.ok(BarberiaResponse.fromBarberia(barberia));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBarberia(@PathVariable Long id) {
        try {
            barberiaService.deleteBarberia(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
