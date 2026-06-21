package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.ServicioRequest;
import com.clipbarber.clipbarberbackend.dto.ServicioResponse;
import com.clipbarber.clipbarberbackend.model.Servicio;
import com.clipbarber.clipbarberbackend.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @PostMapping
    public ResponseEntity<?> createServicio(@Valid @RequestBody ServicioRequest request) {
        try {
            Servicio servicio = servicioService.createServicio(request);
            return ResponseEntity.ok(ServicioResponse.fromServicio(servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> getAllServicios() {
        List<ServicioResponse> servicios = servicioService.getAllServicios().stream()
                .map(ServicioResponse::fromServicio)
                .collect(Collectors.toList());
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/barberia/{barberiaId}")
    public ResponseEntity<List<ServicioResponse>> getServiciosByBarberia(@PathVariable Long barberiaId) {
        List<ServicioResponse> servicios = servicioService.getServiciosByBarberia(barberiaId).stream()
                .map(ServicioResponse::fromServicio)
                .collect(Collectors.toList());
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServicioById(@PathVariable Long id) {
        try {
            Servicio servicio = servicioService.getServicioById(id);
            return ResponseEntity.ok(ServicioResponse.fromServicio(servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateServicio(@PathVariable Long id, @Valid @RequestBody ServicioRequest request) {
        try {
            Servicio servicio = servicioService.updateServicio(id, request);
            return ResponseEntity.ok(ServicioResponse.fromServicio(servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteServicio(@PathVariable Long id) {
        try {
            servicioService.deleteServicio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
