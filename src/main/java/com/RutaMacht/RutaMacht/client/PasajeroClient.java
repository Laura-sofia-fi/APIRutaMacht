package com.RutaMacht.RutaMacht.client;

import com.RutaMacht.RutaMacht.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class PasajeroClient {

    private final RestClient restClient;

    public Pasajero buscarPorId(int id){
        return restClient.get()
                .uri("/api/pasajeros/{id}", id)
                .retrieve()
                .body(Pasajero.class);
    }


    public String verificarConexion(){
        return restClient.get()
                .uri("/api/pasajeros/conexion")
                .retrieve()
                .body(String.class);
    }
}
