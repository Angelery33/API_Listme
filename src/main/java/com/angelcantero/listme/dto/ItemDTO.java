package com.angelcantero.listme.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>ItemDTO</strong></p>
 * <p>DTO para representar un ítem dentro de una biblioteca.</p>
 * <p>Contiene todos los atributos posibles de un ítem para transferencia de datos.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    /**
     * Identificador único del ítem.
     */
    private Long idItem;

    /**
     * Identificador de la biblioteca a la que pertenece el ítem.
     */
    @NotNull(message = "El ID de la biblioteca es obligatorio")
    private Long idLibrary;

    /**
     * Nombre del ítem.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 1, max = 200, message = "El nombre debe tener entre 1 y 200 caracteres")
    private String name;

    /**
     * Descripción del ítem.
     */
    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    private String description;

    /**
     * Fecha asociada al ítem.
     */
    private Long date;

    /**
     * Estado del ítem.
     */
    private String status;

    /**
     * Ruta de la imagen local.
     */
    private String imagePath;

    /**
     * Puntuación personal (0-10).
     */
    @DecimalMin(value = "0.0", message = "La puntuación debe ser >= 0")
    @DecimalMax(value = "10.0", message = "La puntuación debe ser <= 10")
    private Double score;

    /**
     * Género del ítem.
     */
    private String genre;

    /**
     * Fecha de inicio.
     */
    private Long startDate;

    /**
     * Fecha de completado.
     */
    private Long completionDate;

    /**
     * Indica si está en lista de deseos.
     */
    private boolean isWishlist;

    /**
     * Fecha de adquisición.
     */
    private Long acquisitionDate;

    /**
     * Precio pagado (debe ser positivo o cero).
     */
    @DecimalMin(value = "0.0", message = "El precio debe ser >= 0")
    private Double price;

    /**
     * Indica si es el ítem actual.
     */
    private boolean isCurrent;

    /**
     * URL de imagen remota.
     */
    private String remoteImageUrl;

    /**
     * Unidad de progreso.
     */
    private String progressUnit;

    /**
     * Progreso actual (debe ser >= 0).
     */
    @PositiveOrZero(message = "El progreso actual debe ser >= 0")
    private Integer currentProgress;

    /**
     * Progreso total (debe ser > 0).
     */
    @PositiveOrZero(message = "El progreso total debe ser >= 0")
    private Integer totalProgress;

    /**
     * Temporada actual (debe ser >= 0).
     */
    @PositiveOrZero(message = "La temporada debe ser >= 0")
    private Integer season;

    /**
     * Capítulo actual (debe ser >= 0).
     */
    @PositiveOrZero(message = "El capítulo debe ser >= 0")
    private Integer chapter;

    /**
     * Página actual (debe ser >= 0).
     */
    @PositiveOrZero(message = "La página debe ser >= 0")
    private Integer page;

    /**
     * Volumen actual (debe ser >= 0).
     */
    @PositiveOrZero(message = "El volumen debe ser >= 0")
    private Integer volume;

    /**
     * Total de temporadas (debe ser >= 0).
     */
    @PositiveOrZero(message = "El total de temporadas debe ser >= 0")
    private Integer totalSeason;

    /**
     * Total de capítulos (debe ser >= 0).
     */
    @PositiveOrZero(message = "El total de capítulos debe ser >= 0")
    private Integer totalChapter;

    /**
     * Total de páginas (debe ser >= 0).
     */
    @PositiveOrZero(message = "El total de páginas debe ser >= 0")
    private Integer totalPage;

    /**
     * Total de volúmenes (debe ser >= 0).
     */
    @PositiveOrZero(message = "El total de volúmenes debe ser >= 0")
    private Integer totalVolume;

    /**
     * Indica si es colección.
     */
    private boolean isCollection;

    /**
     * ID del ítem padre.
     */
    private Long parentId;

    /**
     * Puntuación externa (0-10).
     */
    @DecimalMin(value = "0.0", message = "La calificación externa debe ser >= 0")
    @DecimalMax(value = "10.0", message = "La calificación externa debe ser <= 10")
    private Double externalRating;

    /**
     * Alineación X de imagen.
     */
    private Double imageAlignmentX;

    /**
     * Alineación Y de imagen.
     */
    private Double imageAlignmentY;

    /**
     * Número de ítem.
     */
    private String itemNumber;

    /**
     * Tipo de producto.
     */
    private String productType;

    /**
     * Edición del producto.
     */
    private String edition;
}
