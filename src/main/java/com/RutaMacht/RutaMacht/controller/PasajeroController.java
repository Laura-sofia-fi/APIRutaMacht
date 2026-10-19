package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.client.PasajeroClient;
import com.RutaMacht.RutaMacht.dto.PasajeroDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pasajero")
@RequiredArgsConstructor
public class PasajeroController {

    private final PasajeroClient pasajeroClient;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PasajeroDTO pasajero) {
        try {
            PasajeroDTO creado = pasajeroClient.crear(pasajero);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Pasajero creado correctamente.",
                    "pasajero", creado));
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException e) {
            return servicioCaido();
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<PasajeroDTO> pasajeros = pasajeroClient.listar();
            String mensaje = pasajeros.isEmpty()
                    ? "No hay pasajeros registrados."
                    : "Pasajeros consultados correctamente.";
            return ResponseEntity.ok(Map.of(
                    "mensaje", mensaje,
                    "pasajeros", pasajeros));
        } catch (ResourceAccessException e) {
            return servicioCaido();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {
        try {
            PasajeroDTO pasajero = pasajeroClient.buscarPorId(id);
            if (pasajero == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "mensaje", "No se encontró un pasajero con el ID " + id));
            }
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Pasajero encontrado correctamente.",
                    "pasajero", pasajero));
        } catch (ResourceAccessException e) {
            return servicioCaido();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @RequestBody PasajeroDTO pasajero) {
        try {
            pasajero.setId(id);
            pasajeroClient.modificar(id, pasajero);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Pasajero actualizado correctamente.",
                    "pasajero", pasajero));
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException e) {
            return servicioCaido();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {
        try {
            pasajeroClient.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Pasajero eliminado correctamente."));
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException e) {
            return servicioCaido();
        }
    }

    // Reenvía tal cual el error que devolvió el servicio de pasajeros (400, 404...)
    private ResponseEntity<?> errorDelServicio(HttpClientErrorException e) {
        return ResponseEntity.status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
    }

    // El 8082 no está corriendo o no responde
    private ResponseEntity<?> servicioCaido() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "mensaje", "El servicio de pasajeros no está disponible."));
    }
}