package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.dto.CrearReservaRequest;
import com.RutaMacht.RutaMacht.dto.ReservaResponse;
import com.RutaMacht.RutaMacht.services.GoogleIdentityService;
import com.RutaMacht.RutaMacht.services.ReservaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaServicio reservaServicio;
    private final GoogleIdentityService googleIdentityService;

    @PostMapping("/viajes/{viajeId}/reservas")
    public ResponseEntity<?> reservar(@PathVariable String viajeId,
                                      @RequestHeader(value = "Authorization", required = false) String authorization,
                                      @Valid @RequestBody CrearReservaRequest request) {
        try {
            GoogleIdentityService.IdentidadGoogle pasajero = googleIdentityService.autenticar(authorization);
            ReservaResponse reserva = reservaServicio.reservar(viajeId, pasajero, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Cupo reservado correctamente.", "reserva", reserva));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/reservas/mis")
    public ResponseEntity<?> misReservas(@RequestHeader(value = "Authorization", required = false) String authorization) {
        GoogleIdentityService.IdentidadGoogle pasajero = googleIdentityService.autenticar(authorization);
        List<ReservaResponse> reservas = reservaServicio.listarPropias(pasajero);
        return ResponseEntity.ok(Map.of("reservas", reservas));
    }

    @DeleteMapping("/reservas/{reservaId}")
    public ResponseEntity<?> cancelar(@PathVariable String reservaId,
                                      @RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            GoogleIdentityService.IdentidadGoogle pasajero = googleIdentityService.autenticar(authorization);
            ReservaResponse reserva = reservaServicio.cancelar(reservaId, pasajero);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada.", "reserva", reserva));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        }
    }
}
