package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.dto.CrearViajeRequest;
import com.RutaMacht.RutaMacht.model.Viaje;
import com.RutaMacht.RutaMacht.services.ViajeServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
public class ViajeController {

    private final ViajeServicio viajeServicio;

    @PostMapping
    public ResponseEntity<?> publicar(@Valid @RequestBody CrearViajeRequest request) {
        try {
            Viaje viaje = viajeServicio.publicar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Viaje publicado correctamente.",
                    "viaje", viaje));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> buscar(
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant hasta) {
        try {
            List<Viaje> viajes = viajeServicio.buscarProgramados(origen, destino, desde, hasta);
            String mensaje = viajes.isEmpty()
                    ? "No hay viajes programados que coincidan con la búsqueda."
                    : "Viajes consultados correctamente.";
            return ResponseEntity.ok(Map.of("mensaje", mensaje, "viajes", viajes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Viaje encontrado correctamente.",
                    "viaje", viajeServicio.buscarPorId(id)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable String id) {
        try {
            Viaje viaje = viajeServicio.cancelar(id);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Viaje cancelado correctamente.",
                    "viaje", viaje));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }
}
