package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>LoginRequest</strong></p>
 * <p>DTO para solicitar autenticación de usuario.</p>
 * <p>Contiene las credenciales necesarias para iniciar sesión en la aplicación.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    /**
     * Nombre de usuario para autenticación.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    /**
     * Contraseña del usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
