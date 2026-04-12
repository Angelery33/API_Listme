package com.angelcantero.listme.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p><strong>RootController</strong></p>
 * <p>Controlador raíz para comprobar el estado de la API.</p>
 * <p>Endpoints públicos de información sobre la API.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
public class RootController {

    /**
     * Verificación de estado de la API.
     *
     * @return mensaje de estado
     */
    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String root() {
        return String.join(
                "\n",
                "ListMe API is running.",
                "Swagger UI: /swagger-ui/index.html",
                "OpenAPI JSON: /v3/api-docs"
        );
    }
}

