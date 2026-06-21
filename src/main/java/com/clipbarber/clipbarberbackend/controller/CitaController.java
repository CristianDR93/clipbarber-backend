package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.CitaRequest;
import com.clipbarber.clipbarberbackend.dto.CitaResponse;
import com.clipbarber.clipbarberbackend.model.Cita;
import com.clipbarber.clipbarberbackend.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<?> createCita(@PathVariable Long clienteId, @Valid @RequestBody CitaRequest request) {
        try {
            Cita cita = citaService.createCita(clienteId, request);
            return ResponseEntity.ok(CitaResponse.fromCita(cita));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CitaResponse>> getCitasByCliente(@PathVariable Long clienteId) {
        List<CitaResponse> citas = citaService.getCitasByCliente(clienteId).stream()
                .map(CitaResponse::fromCita)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/barbero/{barberoId}")
    public ResponseEntity<List<CitaResponse>> getCitasByBarbero(@PathVariable Long barberoId) {
        List<CitaResponse> citas = citaService.getCitasByBarbero(barberoId).stream()
                .map(CitaResponse::fromCita)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCitaById(@PathVariable Long id) {
        try {
            Cita cita = citaService.getCitaById(id);
            return ResponseEntity.ok(CitaResponse.fromCita(cita));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarCita(@PathVariable Long id) {
        try {
            Cita cita = citaService.confirmarCita(id);
            return ResponseEntity.ok(CitaResponse.fromCita(cita));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarCita(@PathVariable Long id) {
        try {
            Cita cita = citaService.cancelarCita(id);
            return ResponseEntity.ok(CitaResponse.fromCita(cita));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCita(@PathVariable Long id) {
        try {
            citaService.deleteCita(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
