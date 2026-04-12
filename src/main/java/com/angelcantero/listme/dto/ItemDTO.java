package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String name;

    /**
     * Descripción del ítem.
     */
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
     * Puntuación personal.
     */
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
     * Precio pagado.
     */
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
     * Progreso actual.
     */
    private Integer currentProgress;

    /**
     * Progreso total.
     */
    private Integer totalProgress;

    /**
     * Temporada actual.
     */
    private Integer season;

    /**
     * Capítulo actual.
     */
    private Integer chapter;

    /**
     * Página actual.
     */
    private Integer page;

    /**
     * Volumen actual.
     */
    private Integer volume;

    /**
     * Total de temporadas.
     */
    private Integer totalSeason;

    /**
     * Total de capítulos.
     */
    private Integer totalChapter;

    /**
     * Total de páginas.
     */
    private Integer totalPage;

    /**
     * Total de volúmenes.
     */
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
     * Puntuación externa.
     */
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
