package com.angelcantero.listme.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bibliomakerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Bibliomaker API")
                        .description("API para la gestión de bibliotecas personalizadas y coleccionables.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
