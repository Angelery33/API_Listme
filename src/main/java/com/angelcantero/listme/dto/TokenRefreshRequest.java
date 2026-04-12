package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p><strong>TokenRefreshRequest</strong></p>
 * <p>DTO para solicitar un nuevo token de acceso usando refresh token.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
public class TokenRefreshRequest {
    /**
     * Token de refresco válido.
     */
    @NotBlank(message = "El token de refresco es obligatorio")
    private String refreshToken;
}
