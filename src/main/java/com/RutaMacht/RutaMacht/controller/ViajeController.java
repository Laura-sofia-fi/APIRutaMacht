package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.dto.CrearViajeRequest;
import com.RutaMacht.RutaMacht.model.Viaje;
import com.RutaMacht.RutaMacht.services.ViajeServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Viajes", description = "Publicación, búsqueda, modificación y cancelación de viajes")
@RestController
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
public class ViajeController {

    private final ViajeServicio viajeServicio;

    @Operation(summary = "Publicar viaje",
            description = "Crea un viaje programado. Valida conductor activo, vehículo, cupos y fecha futura.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Viaje publicado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Conductor o vehículo no encontrado")
    })
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

    @Operation(summary = "Buscar viajes programados",
            description = "Lista los viajes PROGRAMADOS con salida futura, con filtros opcionales por origen, destino y rango de fechas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viajes encontrados (puede estar vacío)"),
            @ApiResponse(responseCode = "400", description = "Rango de fechas inválido")
    })
    @GetMapping
    public ResponseEntity<?> buscar(
            @Parameter(description = "Origen (búsqueda parcial)", example = "Ibagué")
            @RequestParam(required = false) String origen,
            @Parameter(description = "Destino (búsqueda parcial)", example = "Bogotá")
            @RequestParam(required = false) String destino,
            @Parameter(description = "Salida desde (ISO-8601)", example = "2027-01-01T00:00:00Z")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant desde,
            @Parameter(description = "Salida hasta (ISO-8601)", example = "2027-12-31T23:59:59Z")
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

    @Operation(summary = "Listar todos los viajes",
            description = "Devuelve todos los viajes almacenados en MongoDB, sin importar su estado o fecha.")
    @ApiResponse(responseCode = "200", description = "Lista de viajes (puede estar vacía)")
    @GetMapping("/todos")
    public ResponseEntity<?> listarTodos() {
        List<Viaje> viajes = viajeServicio.listarTodos();
        String mensaje = viajes.isEmpty()
                ? "No hay viajes registrados."
                : "Viajes consultados correctamente.";
        return ResponseEntity.ok(Map.of("mensaje", mensaje, "viajes", viajes));
    }

    @Operation(summary = "Consultar viaje por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un viaje con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Id del viaje") @PathVariable String id) {
        try {
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Viaje encontrado correctamente.",
                    "viaje", viajeServicio.buscarPorId(id)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar viaje",
            description = "Modifica un viaje PROGRAMADO. No permite dejar menos cupos que los ya reservados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o viaje no modificable"),
            @ApiResponse(responseCode = "404", description = "Viaje, conductor o vehículo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "Id del viaje") @PathVariable String id,
            @Valid @RequestBody CrearViajeRequest request) {
        try {
            Viaje viaje = viajeServicio.actualizar(id, request);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Viaje actualizado correctamente.",
                    "viaje", viaje));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @Operation(summary = "Cancelar viaje",
            description = "Cambia el estado a CANCELADO y cancela sus reservas confirmadas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje cancelado"),
            @ApiResponse(responseCode = "400", description = "Solo se pueden cancelar viajes programados"),
            @ApiResponse(responseCode = "404", description = "No existe un viaje con ese id")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(
            @Parameter(description = "Id del viaje") @PathVariable String id) {
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

    @Operation(summary = "Eliminar viaje",
            description = "Elimina el viaje de MongoDB. No se permite si tiene reservas confirmadas (cancélalo primero).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un viaje con ese id"),
            @ApiResponse(responseCode = "409", description = "El viaje tiene reservas confirmadas")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "Id del viaje") @PathVariable String id) {
        try {
            viajeServicio.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Viaje eliminado correctamente."));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        }
    }
}
