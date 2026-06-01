package com.israelmerlyn.SistemadeMesadeAyuda.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Mesa de Ayuda API")
                        .description(
                                "API REST para gestionar usuarios, tickets, soluciones y dashboard del sistema Help Desk")
                        .version("1.0.0")
                        .contact(new Contact().name("Equipo Backend").email("backend@academico.local"))
                        .license(new License().name("Uso academico")));
    }
}