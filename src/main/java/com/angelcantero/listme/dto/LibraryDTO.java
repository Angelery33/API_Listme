package com.angelcantero.listme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
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
    @JsonProperty("gradeable")
    private boolean isGradeable;

    /**
     * Indica si es temática (por géneros).
     */
    @JsonProperty("thematic")
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
    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    private String description;

    /**
     * Modo de diseño de géneros (debe ser >= 0).
     */
    @Min(value = 0, message = "El modo de diseño debe ser >= 0")
    private int genreLayoutMode;

    /**
     * Orden y visibilidad de las secciones de estado (comma-separated).
     */
    private String statusOrder;

    /**
     * Indica si la vista es compacta.
     */
    @JsonProperty("compact")
    private boolean isCompact;

    /**
     * Posición en el orden del usuario (debe ser >= 0).
     */
    @PositiveOrZero(message = "La posición debe ser >= 0")
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
     * Escala de calificación (1-10).
     */
    @Min(value = 1, message = "La escala de calificación debe ser >= 1")
    @Max(value = 10, message = "La escala de calificación debe ser <= 10")
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
