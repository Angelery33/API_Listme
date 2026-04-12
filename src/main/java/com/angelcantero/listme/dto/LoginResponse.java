package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>LoginResponse</strong></p>
 * <p>DTO que contiene los tokens de autenticación después de un login exitoso.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    /**
     * Token de acceso JWT para autenticar solicitudes.
     */
    private String accessToken;

    /**
     * Token de refresco para obtener nuevos access tokens.
     */
    private String refreshToken;
}
