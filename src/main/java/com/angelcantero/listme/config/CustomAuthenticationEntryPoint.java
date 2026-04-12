package com.angelcantero.listme.config;

import com.angelcantero.listme.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p><strong>CustomAuthenticationEntryPoint</strong></p>
 * <p>Manejo personalizado de autenticación fallida.</p>
 * <p>Retorna JSON con error 401 cuando falla la autenticación.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "No autorizado: Fallo de autenticación o token inválido/ausente.",
                System.currentTimeMillis(),
                null
        );

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
