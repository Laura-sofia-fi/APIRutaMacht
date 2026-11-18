package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.client.PasajeroClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Conexión", description = "Verifica la comunicación con otros microservicios")
@RestController
@RequestMapping("/api/conexion")
@RequiredArgsConstructor
public class ConexionController {
    private final PasajeroClient pasajeroClient;

    @Operation(summary = "Verificar conexión con el microservicio de pasajeros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Microservicio disponible"),
            @ApiResponse(responseCode = "503", description = "Microservicio no disponible")
    })
    @GetMapping("/pasajero")
    public ResponseEntity<String> conexionPasajeros() {
        try {
            String respuesta = pasajeroClient.verificarConexion();
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("ApiPasajero no disponible");
        }
    }
}

