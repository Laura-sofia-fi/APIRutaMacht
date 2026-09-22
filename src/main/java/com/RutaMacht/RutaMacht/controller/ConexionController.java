package com.RutaMacht.RutaMacht.controller;

import com.RutaMacht.RutaMacht.client.PasajeroClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conexion")
@RequiredArgsConstructor

public class ConexionController {
    private final PasajeroClient pasajeroClient;

    @GetMapping("/libros")
    public ResponseEntity<String> conexionPasajeros(){
        try{
            String respuesta = pasajeroClient.verificarConexion();
            System.out.println("respuesta" + respuesta);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("ApiPasajero no disponible");
        }
    }
}
