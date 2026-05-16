package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un colaborador de una biblioteca.
 *
 * <p>Incluye el identificador y nombre de usuario del colaborador, y su rol dentro
 * de la biblioteca ({@code "editor"} o {@code "viewer"}).</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorDTO {

    /** Identificador único del usuario colaborador. */
    private Long userId;

    /** Nombre de usuario legible del colaborador. */
    private String username;

    /**
     * Rol del colaborador en la biblioteca.
     * Puede ser {@code "editor"} (lectura/escritura) o {@code "viewer"} (solo lectura).
     */
    private String role;
}
