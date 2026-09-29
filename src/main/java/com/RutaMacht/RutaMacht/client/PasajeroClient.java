package com.RutaMacht.RutaMacht.client;

import com.RutaMacht.RutaMacht.model.Pasajero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Configuration

public class PasajeroClient {

    @Value("${api.pasjeros.url}")
    private String apipasajerosUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().
                baseUrl(apipasajerosUrl).
                build();
    }
    private final RestClient restClient;
}
