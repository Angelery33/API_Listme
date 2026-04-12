package com.angelcantero.listme.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>RegisterRequest</strong></p>
 * <p>DTO para registrar un nuevo usuario en el sistema.</p>
 * <p>Contiene los datos necesarios para crear una nueva cuenta de usuario.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /**
     * Nombre de usuario deseado.
     * Debe ser único en el sistema.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    /**
     * Contraseña del usuario.
     * Debe tener al menos 8 caracteres.
     */
    @NotBlank(message = "La contraseña es obligatorio")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    /**
     * Correo electrónico del usuario.
     * Debe ser único y válido.
     */
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;
}
