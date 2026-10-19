package com.RutaMacht.RutaMacht.client;

import com.RutaMacht.RutaMacht.dto.PasajeroDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasajeroClient {

    private final RestClient restClient;

    // El 8082 envuelve sus respuestas en {"mensaje": ..., "pasajero(s)": ...}
    private record ListaRespuesta(String mensaje, List<PasajeroDTO> pasajeros) {}
    private record PasajeroRespuesta(String mensaje, PasajeroDTO pasajero) {}

    public String verificarConexion() {
        return restClient.get()
                .uri("/api/pasajeros/conexion")
                .retrieve()
                .body(String.class);
    }

    public List<PasajeroDTO> listar() {
        ListaRespuesta r = restClient.get()
                .uri("/api/pasajeros")
                .retrieve()
                .body(ListaRespuesta.class);
        return r == null ? List.of() : r.pasajeros();
    }

    public PasajeroDTO buscarPorId(String id) {
        try {
            PasajeroRespuesta r = restClient.get()
                    .uri("/api/pasajeros/{id}", id)
                    .retrieve()
                    .body(PasajeroRespuesta.class);
            return r == null ? null : r.pasajero();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    public PasajeroDTO crear(PasajeroDTO pasajero) {
        PasajeroRespuesta r = restClient.post()
                .uri("/api/pasajeros")
                .body(pasajero)
                .retrieve()
                .body(PasajeroRespuesta.class);
        return r == null ? null : r.pasajero();
    }

    public void modificar(String id, PasajeroDTO pasajero) {
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