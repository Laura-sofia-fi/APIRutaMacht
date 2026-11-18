package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.dto.CrearReservaRequest;
import com.RutaMacht.RutaMacht.dto.ReservaResponse;
import com.RutaMacht.RutaMacht.services.GoogleIdentityService;
import com.RutaMacht.RutaMacht.services.ReservaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Reservas", description = "Reservas de cupos por pasajeros autenticados con Google (token Bearer)")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaServicio reservaServicio;
    private final GoogleIdentityService googleIdentityService;

    @Operation(summary = "Reservar un cupo en un viaje",
            description = "Requiere el encabezado Authorization con un ID token de Google. Descuenta un cupo del viaje.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cupo reservado"),
            @ApiResponse(responseCode = "401", description = "Sesión de Google ausente o inválida"),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado"),
            @ApiResponse(responseCode = "409", description = "Sin cupos, viaje no disponible o reserva duplicada")
    })
    @PostMapping("/viajes/{viajeId}/reservas")
    public ResponseEntity<?> reservar(@PathVariable String viajeId,
                                      @Parameter(description = "Token de Google: Bearer <ID token>") @RequestHeader(value = "Authorization", required = false) String authorization,
                                      @Valid @RequestBody CrearReservaRequest request) {
        try {
            GoogleIdentityService.IdentidadGoogle pasajero = googleIdentityService.autenticar(authorization);
            ReservaResponse reserva = reservaServicio.reservar(viajeId, pasajero, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("mensaje", "Cupo reservado correctamente.", "reserva", reserva));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @Operation(summary = "Listar mis reservas",
            description = "Devuelve las reservas del pasajero autenticado con Google.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas del pasajero"),
            @ApiResponse(responseCode = "401", description = "Sesión de Google ausente o inválida")
    })
    @GetMapping("/reservas/mis")
    public ResponseEntity<?> misReservas(@Parameter(description = "Token de Google: Bearer <ID token>") @RequestHeader(value = "Authorization", required = false) String authorization) {
        GoogleIdentityService.IdentidadGoogle pasajero = googleIdentityService.autenticar(authorization);
        List<ReservaResponse> reservas = reservaServicio.listarPropias(pasajero);
        return ResponseEntity.ok(Map.of("reservas", reservas));
    }

    @Operation(summary = "Cancelar una reserva",
            description = "Cancela la reserva propia y devuelve el cupo al viaje.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada"),
            @ApiResponse(responseCode = "401", description = "Sesión de Google ausente o inválida"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "409", description = "Reserva ya cancelada o viaje ya salió")
    })
    @DeleteMapping("/reservas/{reservaId}")
    public ResponseEntity<?> cancelar(@PathVariable String reservaId,
                                      @Parameter(description = "Token de Google: Bearer <ID token>") @RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            GoogleIdentityService.IdentidadGoogle pasajero = googleIdentityService.autenticar(authorization);
            ReservaResponse reserva = reservaServicio.cancelar(reservaId, pasajero);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada.", "reserva", reserva));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        }
    }
}
