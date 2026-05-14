package com.nazca.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nazcaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nazca API")
                        .description("Sistema de POPs, treinamentos e conformidade")
                        .version("1.0.0"));
    }
}