package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * <p><strong>Item</strong></p>
 * <p>Entidad que representa un ítem individual dentro de una biblioteca.</p>
 * <p>Un ítem puede ser cualquier elemento que el usuario quiera rastrear, como:
 * libros, series de TV, películas, anime, videojuegos, etc.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    /**
     * Identificador único del ítem (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_item")
    private Long idItem;

    /**
     * Biblioteca a la que pertenece el ítem.
     * Relación muchos-a-uno: muchos ítems pueden pertenecer a una biblioteca.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_library", nullable = false)
    @ToString.Exclude
    private Library library;

    /**
     * Nombre del ítem.
     * Campo obligatorio que identifica al ítem.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Descripción detallada del ítem.
     * Puede contener información adicional hasta 2000 caracteres.
     */
    @Column(length = 2000)
    private String description;

    /**
     * Fecha asociada al ítem (fecha de lanzamiento, publicación, etc.).
     * Almacenada como timestamp Unix.
     */
    private Long itemDate;

    /**
     * Estado actual del ítem en la lista del usuario.
     * Valores típicos: PENDING, IN_PROGRESS, COMPLETED, DROPPED, ON_HOLD.
     */
    @Column(nullable = false)
    private String status = "PENDING";

    /**
     * Ruta local de la imagen del ítem.
     */
    private String imagePath;

    /**
     * Puntuación personal asignada por el usuario (0-10).
     */
    private Double score;

    /**
     * Género o categoría del ítem.
     */
    private String genre;

    /**
     * Fecha de inicio de consumo del ítem.
     * Timestamp Unix.
     */
    private Long startDate;

    /**
     * Fecha de finalización/completado del ítem.
     * Timestamp Unix.
     */
    private Long completionDate;

    /**
     * Indica si el ítem está en la lista de deseos.
     */
    private boolean isWishlist;

    /**
     * Fecha de adquisición del ítem.
     * Timestamp Unix.
     */
    private Long acquisitionDate;

    /**
     * Precio pagado por el ítem.
     */
    private Double price;

    /**
     * Indica si el ítem es el actualmente en consumo.
     */
    private boolean isCurrent;

    /**
     * URL de imagen remota del ítem.
     */
    private String remoteImageUrl;

    /**
     * Unidad de progreso personalizada.
     * Ejemplos: "capítulos", "páginas", "horas".
     */
    private String progressUnit;

    /**
     * Progreso actual del ítem.
     */
    private Integer currentProgress;

    /**
     * Progreso total necesario para completar el ítem.
     */
    private Integer totalProgress;

    /**
     * Temporada actual (para series de TV/anime).
     */
    private Integer season;

    /**
     * Capítulo actual.
     */
    private Integer chapter;

    /**
     * Página actual (para libros/manga).
     */
    private Integer page;

    /**
     * Volumen actual (para series de tomos).
     */
    private Integer volume;

    /**
     * Total de temporadas de la serie.
     */
    private Integer totalSeason;

    /**
     * Total de capítulos de la temporada/serie.
     */
    private Integer totalChapter;

    /**
     * Total de páginas del libro/obra.
     */
    private Integer totalPage;

    /**
     * Total de volúmenes de la serie.
     */
    private Integer totalVolume;

    /**
     * Indica si el ítem es parte de una colección/serie.
     */
    private boolean isCollection;

    /**
     * Ítem padre o principal al que pertenece este ítem.
     * Usado para relacionar capítulos de una serie con su serie principal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Item parentItem;

    /**
     * Puntuación externa del ítem (ej: puntuación de IMDb, MyAnimeList).
     */
    private Double externalRating;

    /**
     * Alineación X de la imagen del ítem.
     */
    private Double imageAlignmentX;

    /**
     * Alineación Y de la imagen del ítem.
     */
    private Double imageAlignmentY;

    /**
     * Número identificador del ítem dentro de una serie.
     * Ej: número de episodio, volumen, temporada.
     */
    private String itemNumber;

    /**
     * Tipo de producto del ítem.
     */
    private String productType;

    /**
     * Edición específica del producto.
     */
    private String edition;

    /**
     * Imágenes asociadas al ítem.
     * Relación uno-a-muchos: un ítem puede tener múltiples imágenes.
     */
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<ItemImage> images;
}
