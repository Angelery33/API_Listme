package com.angelcantero.listme.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p><strong>SwaggerConfig</strong></p>
 * <p>Configuración de Swagger/OpenAPI para documentación de la API.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * Define la información de la API para Swagger.
     *
     * @return configuración de OpenAPI
     */
    @Bean
    public OpenAPI bibliomakerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("ListMe API")
                        .description("API REST para gestión de listas y colecciones personalizables.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
