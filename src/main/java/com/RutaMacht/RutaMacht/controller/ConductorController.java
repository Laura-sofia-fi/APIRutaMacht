package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.model.Conductor;
import com.RutaMacht.RutaMacht.services.ConductorServicie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/conductor")
@RequiredArgsConstructor
public class ConductorController {

    private final ConductorServicie conductorServicie;

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


    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @PathVariable String id) {

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


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable String id,
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


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable String id) {

        try {

            conductorServicie.eliminarConductor(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "Conductor eliminado correctamente."
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