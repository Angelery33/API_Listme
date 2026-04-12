package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>LibraryGenreDTO</strong></p>
 * <p>DTO para representar un género de biblioteca.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryGenreDTO {
    /**
     * Identificador del género.
     */
    private Long id;

    /**
     * Identificador de la biblioteca.
     */
    @NotNull(message = "El ID de la biblioteca es obligatorio")
    private Long libraryId;

    /**
     * Nombre del género.
     */
    @NotBlank(message = "El nombre del género es obligatorio")
    private String name;
}
