package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "La contraseña actual es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String currentPassword;

    /**
     * Nueva contraseña deseada.
     * Debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales.
     */
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "La contraseña debe contener mayúsculas, minúsculas, números y caracteres especiales (@$!%*?&)")
    private String newPassword;
}