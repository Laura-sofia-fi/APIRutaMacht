package com.RutaMacht.RutaMacht.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration

public class RestPasajeroConfig {

    @Value("${api.libros.url}")
    private String apipasajeroUrl;


    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl(apipasajeroUrl)
                .build();
    }
}
