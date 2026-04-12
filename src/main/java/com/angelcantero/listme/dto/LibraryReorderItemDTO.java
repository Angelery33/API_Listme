package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>LibraryReorderItemDTO</strong></p>
 * <p>DTO para reordenar bibliotecas.</p>
 * <p>Usado en operaciones batch de reordenamiento.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryReorderItemDTO {
    /**
     * Identificador de la biblioteca.
     */
    private Long id;

    /**
     * Nueva posición en el orden.
     */
    private int position;
}
