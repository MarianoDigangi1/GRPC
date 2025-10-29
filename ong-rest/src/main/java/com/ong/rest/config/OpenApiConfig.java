package com.ong.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().description("Documentación de nuestra ONG - Grupo N")
                        .title("ONG REST API - Grupo N")
                        .version("1.0")
                );
    }
}
