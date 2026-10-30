package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.model.Vehiculo;
import com.RutaMacht.RutaMacht.services.VehiculoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculo")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoServicio servicio;

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


    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {

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


    @GetMapping("/placa/{placa}")
    public ResponseEntity<?> buscarPorPlaca(@PathVariable String placa) {

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


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable String id,
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


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {

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
    }
}