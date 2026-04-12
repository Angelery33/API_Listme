package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>LibraryDTO</strong></p>
 * <p>DTO para representar una biblioteca.</p>
 * <p>Contiene todos los atributos de configuración de la biblioteca.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO {
    /**
     * Identificador único de la biblioteca.
     */
    private Long idLibrary;

    /**
     * Nombre de la biblioteca.
     */
    @NotBlank(message = "El nombre de la biblioteca es obligatorio")
    private String name;

    /**
     * Tipo de biblioteca.
     */
    private String type;

    /**
     * Indica si soporta estados de completado.
     */
    private boolean supportsCompletion;

    /**
     * Indica si permite calificación.
     */
    private boolean isGradeable;

    /**
     * Indica si es temática (por géneros).
     */
    private boolean isThematic;

    /**
     * Indica si soporta lista de deseos.
     */
    private boolean supportsWishlist;

    /**
     * Indica si rastrea fechas.
     */
    private boolean tracksDates;

    /**
     * Indica si soporta precios.
     */
    private boolean supportsPrice;

    /**
     * Descripción de la biblioteca.
     */
    private String description;

    /**
     * Modo de diseño de géneros.
     */
    private int genreLayoutMode;

    /**
     * Indica si la vista es compacta.
     */
    private boolean isCompact;

    /**
     * Posición en el orden del usuario.
     */
    private int position;

    /**
     * Indica si soporta progreso.
     */
    private boolean supportsProgress;

    /**
     * Tipo de progreso.
     */
    private String progressType;

    /**
     * Unidad de progreso personalizada.
     */
    private String customProgressUnit;

    /**
     * Categoría por defecto.
     */
    private String defaultCategory;

    /**
     * Escala de calificación.
     */
    private int ratingScale;

    /**
     * Indica si el usuario actual es el propietario.
     */
    private boolean isOwner;

    /**
     * Indica si la biblioteca está compartida.
     */
    private boolean isShared;

    /**
     * Indica si el usuario puede editar.
     */
    private boolean canEdit;

    /**
     * Cantidad de ítems en la biblioteca.
     */
    private Long itemCount;
}
