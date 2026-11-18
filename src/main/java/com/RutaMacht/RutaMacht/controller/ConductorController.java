package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.model.Conductor;
import com.RutaMacht.RutaMacht.services.ConductorServicie;
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

@Tag(name = "Conductores", description = "CRUD de conductores (persistidos en MongoDB)")
@RestController
@RequestMapping("/api/conductor")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorServicie conductorServicie;

    @Operation(summary = "Crear conductor",
            description = "Registra un conductor. El campo id es opcional: si no se envía, MongoDB lo genera.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conductor creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o conductor duplicado")
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody Conductor conductor) {

        try {
            Conductor creado = conductorServicie.crearConductor(conductor);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensaje", "Conductor creado correctamente.",
                            "conductor", creado
                    ));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "mensaje", e.getMessage()
                    ));
        }
    }


    @Operation(summary = "Listar conductores",
            description = "Devuelve todos los conductores almacenados en MongoDB.")
    @ApiResponse(responseCode = "200", description = "Lista de conductores (puede estar vacía)")
    @GetMapping
    public ResponseEntity<?> listar() {

        List<Conductor> conductores =
                conductorServicie.listarConductores();

        if (conductores.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "No hay conductores registrados.",
                            "conductores", conductores
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "mensaje", "Conductores consultados correctamente.",
                        "conductores", conductores
                ));
    }


    @Operation(summary = "Consultar conductor por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conductor encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un conductor con ese id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "Id del conductor") @PathVariable String id) {

        Conductor conductor =
                conductorServicie.buscarConductor(id);

        if (conductor == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", "No se encontró un conductor con el ID " + id
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "mensaje", "Conductor encontrado correctamente.",
                        "conductor", conductor
                ));
    }


    @Operation(summary = "Actualizar conductor",
            description = "Reemplaza los datos del conductor indicado. Todos los campos obligatorios deben enviarse.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conductor actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "No existe un conductor con ese id")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "Id del conductor") @PathVariable String id,
            @RequestBody Conductor conductor) {

        // Verificar que exista
        Conductor existente =
                conductorServicie.buscarConductor(id);

        if (existente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", "No se encontró un conductor con el ID " + id
                    ));
        }

        try {

            // Nos aseguramos de que el objeto conserve el ID
            conductor.setId(id);

            conductorServicie.modificarConductor(conductor);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "Conductor actualizado correctamente.",
                            "conductor", conductor
                    ));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "mensaje", e.getMessage()
                    ));
        }
    }


    @Operation(summary = "Eliminar conductor",
            description = "Elimina el conductor de MongoDB. No se permite si tiene viajes programados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conductor eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un conductor con ese id"),
            @ApiResponse(responseCode = "409", description = "El conductor tiene viajes programados")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @Parameter(description = "Id del conductor") @PathVariable String id) {

        try {

            conductorServicie.eliminarConductor(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "Conductor eliminado correctamente."
                    ));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "mensaje", e.getMessage()
                    ));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", e.getMessage()
                    ));
        }
    }
}
