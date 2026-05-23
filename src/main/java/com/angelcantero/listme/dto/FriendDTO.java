package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO que representa un amigo del usuario autenticado junto con sus estadísticas de uso.
 *
 * <p>Se devuelve en la lista de amigos de la pantalla social para mostrar una
 * tarjeta de resumen por cada amigo.</p>
 *
 * @author Angel Cantero
 * @since 1.5.0
 */
@Data
@AllArgsConstructor
public class FriendDTO {

    /** Identificador único del amigo. */
    private Long id;

    /** Nombre de usuario del amigo. */
    private String username;

    /** URL de la foto de perfil del amigo almacenada en Firebase Storage, o {@code null}. */
    private String photoUrl;

    /** Número total de bibliotecas (listas) que posee el amigo. */
    private long totalLibraries;

    /** Número total de ítems registrados en todas las bibliotecas del amigo. */
    private long totalItems;
}
