package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.model.Vehiculo;
import com.RutaMacht.RutaMacht.services.VehiculoServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Vehículos", description = "CRUD de vehículos (persistidos en MongoDB)")
@RestController
@RequestMapping("/api/vehiculo")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoServicio servicio;

    @Operation(summary = "Crear vehículo",
            description = "Registra un vehículo. La placa debe ser única y el chasis tener 17 caracteres alfanuméricos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehículo creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o placa duplicada")
    })
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Vehiculo vehiculo) {

        try {

            Vehiculo creado = servicio.crear(vehiculo);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensaje", "Vehículo creado correctamente.",
                            "vehiculo", creado
                    ));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }


    @Operation(summary = "Listar vehículos",
            description = "Devuelve todos los vehículos almacenados en MongoDB.")
    @ApiResponse(responseCode = "200", description = "Lista de vehículos (puede estar vacía)")
    @GetMapping
    public ResponseEntity<?> listar() {

        List<Vehiculo> vehiculos = servicio.listar();

        if (vehiculos.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "No hay vehículos registrados.",
                            "vehiculos", vehiculos
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "mensaje", "Vehículos consultados correctamente.",
                        "vehiculos", vehiculos
                ));
    }


    @Operation(summary = "Consultar vehículo por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un vehículo con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Id del vehículo") @PathVariable String id) {

        Vehiculo vehiculo = servicio.buscarPorId(id);

        if (vehiculo == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje",
                            "No se encontró un vehículo con el id " + id
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "mensaje", "Vehículo encontrado correctamente.",
                        "vehiculo", vehiculo
                ));
    }


    @Operation(summary = "Consultar vehículo por placa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un vehículo con esa placa")
    })
    @GetMapping("/placa/{placa}")
    public ResponseEntity<?> buscarPorPlaca(
            @Parameter(description = "Placa del vehículo", example = "ABC123") @PathVariable String placa) {

        Vehiculo vehiculo = servicio.buscarPorPlaca(placa);

        if (vehiculo == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje",
                            "No se encontró un vehículo con la placa " + placa
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "mensaje", "Vehículo encontrado correctamente.",
                        "vehiculo", vehiculo
                ));
    }


    @Operation(summary = "Actualizar vehículo",
            description = "Reemplaza los datos del vehículo indicado, conservando su id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o placa duplicada"),
            @ApiResponse(responseCode = "404", description = "No existe un vehículo con ese id")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "Id del vehículo") @PathVariable String id,
            @RequestBody Vehiculo vehiculo) {

        try {

            Vehiculo actualizado = servicio.actualizar(id, vehiculo);

            if (actualizado == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "mensaje",
                                "No se encontró un vehículo con el id " + id
                        ));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "Vehículo actualizado correctamente.",
                            "vehiculo", actualizado
                    ));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }


    @Operation(summary = "Eliminar vehículo",
            description = "Elimina el vehículo de MongoDB. No se permite si tiene viajes programados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vehículo eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un vehículo con ese id"),
            @ApiResponse(responseCode = "409", description = "El vehículo tiene viajes programados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "Id del vehículo") @PathVariable String id) {

        try {

            boolean eliminado = servicio.eliminar(id);

            if (!eliminado) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "mensaje",
                                "No se encontró un vehículo con el id " + id
                        ));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of("mensaje", "Vehículo eliminado correctamente."));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", e.getMessage()));
        }
    }
}
