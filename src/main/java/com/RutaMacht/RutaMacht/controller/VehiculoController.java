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

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody Vehiculo vehiculo) {

        try {

            VehiculoServicio.crearVehiculo(vehiculo);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensaje", "Vehículo creado correctamente.",
                            "vehiculo", vehiculo
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

        List<Vehiculo> vehiculos =
                VehiculoServicio.listarVehiculos();

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


    @GetMapping("/{placa}")
    public ResponseEntity<?> buscarPorPlaca(
            @PathVariable String placa) {

        Vehiculo vehiculo =
                VehiculoServicio.buscarVehiculo(placa);

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
                        "mensaje",
                        "Vehículo encontrado correctamente.",
                        "vehiculo",
                        vehiculo
                ));
    }


    @PutMapping("/{placa}")
    public ResponseEntity<?> actualizar(
            @PathVariable String placa,
            @RequestBody Vehiculo vehiculo) {

        Vehiculo existente =
                VehiculoServicio.buscarVehiculo(placa);

        if (existente == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje",
                            "No se encontró un vehículo con la placa " + placa
                    ));
        }

        try {

            // Mantener la placa de la URL
            vehiculo.setPlaca(placa);

            VehiculoServicio.modificarVehiculo(vehiculo);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje",
                            "Vehículo actualizado correctamente.",
                            "vehiculo",
                            vehiculo
                    ));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "mensaje", e.getMessage()
                    ));
        }
    }


    @DeleteMapping("/{placa}")
    public ResponseEntity<?> eliminar(
            @PathVariable String placa) {

        try {

            VehiculoServicio.eliminarVehiculo(placa);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje",
                            "Vehículo eliminado correctamente."
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