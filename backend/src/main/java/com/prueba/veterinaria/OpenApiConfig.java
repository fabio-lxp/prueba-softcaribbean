package com.prueba.veterinaria;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API Veterinaria")
                .version("1.0")
                .description("API para la gestión de pacientes de una veterinaria")
                .termsOfService("http://tusitio.com/terminos")
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .name("Veterinaria Soporte")
                    .email("soporte@veterinaria.com")
                    .url("http://veterinaria.com")
                )
            );
    }
} 