package com.angelcantero.listme.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p><strong>RootController</strong></p>
 * <p>Controlador raíz para comprobar el estado de la API y enlaces rápidos.</p>
 * Created on: 2026-03-20
 *
 * @author Angel Cantero
 * @version 1.0.0
 */
@RestController
public class RootController {

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

