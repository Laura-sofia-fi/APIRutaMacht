package com.RutaMacht.RutaMacht.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rutaMachtOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API RutaMacht")
                        .version("1.0.0")
                        .description("API REST de RutaMacht: gestión de conductores, vehículos, viajes y "
                                + "reservas (MongoDB). Los pasajeros se consumen desde el microservicio "
                                + "de pasajeros mediante RestClient."))
                .servers(List.of(new Server().url("http://localhost:8081").description("Local")));
    }
}
