package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.client.PasajeroClient;
import com.RutaMacht.RutaMacht.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

// Este controlador NO guarda pasajeros aquí: reenvía todo hacia la API
// externa de pasajeros a través de PasajeroClient. Es el "puente" entre
// el panel de Angular (que solo conoce esta API) y esa API externa.

@RestController
@RequestMapping("/api/pasajero")
@RequiredArgsConstructor
public class PasajeroController {

    private final PasajeroClient pasajeroClient;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pasajero pasajero) {

        try {

            Pasajero creado = pasajeroClient.crear(pasajero);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensaje", "Pasajero creado correctamente.",
                            "pasajero", creado
                    ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "mensaje", "No se pudo crear el pasajero en la API externa."
                    ));
        }
    }


    @GetMapping
    public ResponseEntity<?> listar() {

        try {

            List<Pasajero> pasajeros = pasajeroClient.listar();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", pasajeros.isEmpty()
                                    ? "No hay pasajeros registrados."
                                    : "Pasajeros consultados correctamente.",
                            "pasajeros", pasajeros
                    ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "mensaje", "La API de pasajeros no está disponible.",
                            "pasajeros", List.of()
                    ));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable String id) {

        Pasajero pasajero = pasajeroClient.buscarPorId(id);

        if (pasajero == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", "No se encontró un pasajero con el ID " + id
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "mensaje", "Pasajero encontrado correctamente.",
                        "pasajero", pasajero
                ));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable String id,
            @RequestBody Pasajero pasajero) {

        try {

            // Mantener el ID de la URL
            pasajero.setId(id);

            pasajeroClient.modificar(id, pasajero);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "Pasajero actualizado correctamente.",
                            "pasajero", pasajero
                    ));

        } catch (HttpClientErrorException.NotFound e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", "No se encontró un pasajero con el ID " + id
                    ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "mensaje", "No se pudo actualizar el pasajero en la API externa."
                    ));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable String id) {

        try {

            pasajeroClient.eliminar(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "Pasajero eliminado correctamente."
                    ));

        } catch (HttpClientErrorException.NotFound e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "mensaje", "No se encontró un pasajero con el ID " + id
                    ));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "mensaje", "No se pudo eliminar el pasajero en la API externa."
                    ));
        }
    }
}