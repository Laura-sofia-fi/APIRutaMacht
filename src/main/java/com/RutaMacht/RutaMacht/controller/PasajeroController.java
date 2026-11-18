package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.client.PasajeroClient;
import com.RutaMacht.RutaMacht.dto.PasajeroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Map;

@Tag(name = "Pasajeros", description = "Comunicación con el microservicio de pasajeros (puerto 8082) mediante RestClient")
@RestController
@RequestMapping("/api/pasajero")
@RequiredArgsConstructor
public class PasajeroController {

    private final PasajeroClient pasajeroClient;

    @Operation(summary = "Crear pasajero",
            description = "Reenvía la creación al microservicio de pasajeros.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pasajero creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (respuesta del microservicio)"),
            @ApiResponse(responseCode = "503", description = "Microservicio de pasajeros no disponible")
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PasajeroDTO pasajero) {
        try {
            PasajeroDTO creado = pasajeroClient.crear(pasajero);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "mensaje", "Pasajero creado correctamente.",
                    "pasajero", creado));
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException | HttpServerErrorException e) {
            return servicioCaido();
        }
    }

    @Operation(summary = "Listar pasajeros",
            description = "Consulta la lista de pasajeros en el microservicio de pasajeros.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pasajeros (puede estar vacía)"),
            @ApiResponse(responseCode = "503", description = "Microservicio de pasajeros no disponible")
    })
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
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException | HttpServerErrorException e) {
            return servicioCaido();
        }
    }

    @Operation(summary = "Consultar pasajero por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pasajero encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un pasajero con ese id"),
            @ApiResponse(responseCode = "503", description = "Microservicio de pasajeros no disponible")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Id del pasajero") @PathVariable String id) {
        try {
            PasajeroDTO pasajero = pasajeroClient.buscarPorId(id);
            if (pasajero == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "mensaje", "No se encontró un pasajero con el ID " + id));
            }
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Pasajero encontrado correctamente.",
                    "pasajero", pasajero));
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException | HttpServerErrorException e) {
            return servicioCaido();
        }
    }

    @Operation(summary = "Actualizar pasajero",
            description = "Reenvía la actualización al microservicio de pasajeros.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pasajero actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (respuesta del microservicio)"),
            @ApiResponse(responseCode = "404", description = "No existe un pasajero con ese id"),
            @ApiResponse(responseCode = "503", description = "Microservicio de pasajeros no disponible")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "Id del pasajero") @PathVariable String id,
            @RequestBody PasajeroDTO pasajero) {
        try {
            pasajero.setId(id);
            pasajeroClient.modificar(id, pasajero);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Pasajero actualizado correctamente.",
                    "pasajero", pasajero));
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException | HttpServerErrorException e) {
            return servicioCaido();
        }
    }

    @Operation(summary = "Eliminar pasajero",
            description = "Reenvía la eliminación al microservicio de pasajeros.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pasajero eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un pasajero con ese id"),
            @ApiResponse(responseCode = "503", description = "Microservicio de pasajeros no disponible")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "Id del pasajero") @PathVariable String id) {
        try {
            pasajeroClient.eliminar(id);
            return ResponseEntity.ok(Map.of("mensaje", "Pasajero eliminado correctamente."));
        } catch (HttpClientErrorException e) {
            return errorDelServicio(e);
        } catch (ResourceAccessException | HttpServerErrorException e) {
            return servicioCaido();
        }
    }

    // Reenvía tal cual el error que devolvió el servicio de pasajeros (400, 404...)
    private ResponseEntity<?> errorDelServicio(HttpClientErrorException e) {
        return ResponseEntity.status(e.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsString());
    }

    // El 8082 no está corriendo, no responde o falló internamente (5xx)
    private ResponseEntity<?> servicioCaido() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                "mensaje", "El servicio de pasajeros no está disponible."));
    }
}
