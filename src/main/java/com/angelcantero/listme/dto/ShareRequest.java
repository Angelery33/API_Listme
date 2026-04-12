package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>ShareRequest</strong></p>
 * <p>DTO para compartir una biblioteca con otro usuario.</p>
 * <p>Permite especificar el usuario destinatario y el tipo de acceso.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareRequest {
    /**
     * Nombre de usuario con quien compartir la biblioteca.
     */
    @NotBlank(message = "El username es obligatorio")
    private String username;
    
    /**
     * Indica si el acceso es de solo lectura.
     * Si es true, el usuario será viewer (solo lectura).
     * Si es false, el usuario será editor (lectura y escritura).
     */
    private boolean readOnly;
}
