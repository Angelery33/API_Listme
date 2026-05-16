package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO que encapsula las estadísticas de uso agregadas de un usuario.
 *
 * <p>Se utiliza para devolver al cliente un resumen numérico de los recursos
 * que posee el usuario autenticado, sin exponer detalles de las entidades internas.</p>
 */
@Data
@AllArgsConstructor
public class UserStatsDTO {

    /** Número total de bibliotecas que pertenecen al usuario. */
    private long totalLibraries;

    /** Número total de ítems registrados en todas las bibliotecas del usuario. */
    private long totalItems;
}
