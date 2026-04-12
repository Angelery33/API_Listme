package com.angelcantero.listme.config;

import com.angelcantero.listme.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p><strong>CustomAccessDeniedHandler</strong></p>
 * <p>Manejo personalizado de acceso denegado.</p>
 * <p>Retorna JSON con error 403 cuando el usuario no tiene permisos.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acceso denegado: No tienes los permisos necesarios para acceder a este recurso.",
                System.currentTimeMillis(),
                null
        );

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
