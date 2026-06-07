package com.angelcantero.listme.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO para solicitar el cambio de rol de un colaborador de una biblioteca.
 */
@Data
public class UpdateCollaboratorRoleRequest {

    /**
     * Nuevo rol del colaborador: {@code "editor"} (lectura/escritura) o
     * {@code "viewer"} (solo lectura).
     */
    @Pattern(regexp = "editor|viewer", message = "El rol debe ser 'editor' o 'viewer'")
    private String role;
}
