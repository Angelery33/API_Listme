package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

/**
 * <p><strong>Library</strong></p>
 * <p>Entidad que representa una biblioteca o colección de ítems del usuario.</p>
 * <p>Una biblioteca es un contenedor lógico que agrupa ítems relacionados
 * (ej: "Mis libros de fantasía", "Anime veraniego", "Videojuegos RPG").</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "library")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Library {

    /**
     * Identificador único de la biblioteca (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_library")
    private Long idLibrary;

    /**
     * Usuario propietario de la biblioteca.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Usuario usuario;

    /**
     * Conjunto de usuarios que pueden editar la biblioteca.
     * Compartidos con permisos de escritura.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "library_editors",
            joinColumns = @JoinColumn(name = "library_id", referencedColumnName = "id_library"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<Usuario> editors = new HashSet<>();

    /**
     * Conjunto de usuarios que pueden visualizar la biblioteca.
     * Compartidos con permisos de solo lectura.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "library_viewers",
            joinColumns = @JoinColumn(name = "library_id", referencedColumnName = "id_library"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<Usuario> viewers = new HashSet<>();

    /**
     * Lista de ítems contenidos en la biblioteca.
     */
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private java.util.List<Item> items = new java.util.ArrayList<>();

    /**
     * Lista de géneros definidos para la biblioteca.
     */
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private java.util.List<LibraryGenre> genres = new java.util.ArrayList<>();

    /**
     * Nombre de la biblioteca.
     * Campo obligatorio.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Tipo de biblioteca.
     * Ejemplos: "Libros", "Anime", "Videojuegos", "Películas".
     */
    private String type;

    /**
     * Indica si la biblioteca soporta estados de completado.
     */
    private boolean supportsCompletion;

    /**
     * Indica si se puede asignar puntuación a los ítems.
     */
    private boolean isGradeable;

    /**
     * Indica si la biblioteca es temática (agrupada por géneros).
     */
    private boolean isThematic;

    /**
     * Indica si la biblioteca soporta lista de deseos.
     */
    private boolean supportsWishlist;

    /**
     * Indica si la biblioteca rastrea fechas de inicio y finalización.
     */
    private boolean tracksDates;

    /**
     * Indica si la biblioteca rastrea precios.
     */
    private boolean supportsPrice;

    /**
     * Descripción de la biblioteca.
     * Puede contener hasta 2000 caracteres.
     */
    @Column(length = 2000)
    private String description;

    /**
     * Modo de diseño de géneros (0, 1, 2, etc.).
     */
    private int genreLayoutMode;

    /**
     * Indica si la vista es compacta.
     */
    private boolean isCompact;

    /**
     * Posición de la biblioteca en el orden del usuario.
     */
    @Column(name = "position_index")
    private int position;

    /**
     * Indica si la biblioteca soporta progreso.
     */
    private boolean supportsProgress;

    /**
     * Tipo de progreso de la biblioteca.
     * Ej: "CHAPTERS", "PAGES", "HOURS".
     */
    private String progressType;

    /**
     * Unidad de progreso personalizada.
     */
    private String customProgressUnit;

    /**
     * Categoría por defecto para nuevos ítems.
     */
    private String defaultCategory;

    /**
     * Escala de puntuación (típicamente 10 o 5).
     */
    private int ratingScale = 10;
}
