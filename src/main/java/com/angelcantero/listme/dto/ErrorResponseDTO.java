package com.angelcantero.listme.dto;

import java.util.Map;

public record ErrorResponseDTO(
        int status,
        String message,
        long timestamp,
        Map<String, String> errors
) { }
