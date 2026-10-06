package com.RutaMacht.RutaMacht.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {


    @Value("${api.pasajeros.url}")
    private String pasajerosUrl;

    @Bean
    public RestClient restClient(){
        return RestClient.builder()
                .baseUrl(pasajerosUrl)
                .build();
    }
}