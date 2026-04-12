package com.angelcantero.listme.dto;

import java.util.Map;

/**
 * <p><strong>ErrorResponseDTO</strong></p>
 * <p>Record para representar errores HTTP.</p>
 * <p>Usado en respuestas de error para proporcionar información estructurada.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
public record ErrorResponseDTO(
        /**
         * Código de estado HTTP.
         */
        int status,
        /**
         * Mensaje de error principal.
         */
        String message,
        /**
         * Timestamp del error.
         */
        long timestamp,
        /**
         * Mapa de errores de validación.
         */
        Map<String, String> errors
) { }
