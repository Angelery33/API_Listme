package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p><strong>ChangePasswordRequest</strong></p>
 * <p>DTO para cambiar la contraseña del usuario.</p>
 * <p>Requiere la contraseña actual para verificar identidad.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
public class ChangePasswordRequest {
    /**
     * Contraseña actual del usuario.
     */
    @NotBlank
    private String currentPassword;
    
    /**
     * Nueva contraseña deseada.
     */
    @NotBlank
    private String newPassword;
}