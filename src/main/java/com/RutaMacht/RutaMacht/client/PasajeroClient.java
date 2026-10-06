package com.RutaMacht.RutaMacht.client;

import com.RutaMacht.RutaMacht.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasajeroClient {

    private final RestClient restClient;

    public String verificarConexion() {
        return restClient.get()
                .uri("/api/pasajeros/conexion")
                .retrieve()
                .body(String.class);
    }

    public List<Pasajero> listar() {
        return restClient.get()
                .uri("/api/pasajeros")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Pasajero>>() {});
    }

    public Pasajero buscarPorId(String id) {
        try {
            return restClient.get()
                    .uri("/api/pasajeros/{id}", id)
                    .retrieve()
                    .body(Pasajero.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    public Pasajero crear(Pasajero pasajero) {
        return restClient.post()
                .uri("/api/pasajeros")
                .body(pasajero)
                .retrieve()
                .body(Pasajero.class);
    }

    public void modificar(String id, Pasajero pasajero) {
        restClient.put()
                .uri("/api/pasajeros/{id}", id)
                .body(pasajero)
                .retrieve()
                .toBodilessEntity();
    }

    public void eliminar(String id) {
        restClient.delete()
                .uri("/api/pasajeros/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}