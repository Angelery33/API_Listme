package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p><strong>LibraryGenre</strong></p>
 * <p>Entidad que representa un género o categoría dentro de una biblioteca.</p>
 * <p>Los géneros permiten organizar los ítems dentro de una biblioteca temática
 * (ej: "Fantasía", "Ciencia Ficción" para una biblioteca de libros).</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "library_genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LibraryGenre {

    /**
     * Identificador único del género.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Biblioteca a la que pertenece el género.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "library_id", nullable = false)
    @ToString.Exclude
    private Library library;

    /**
     * Nombre del género.
     * Ej: "Acción", "Romance", "Terror".
     */
    @Column(nullable = false)
    private String name;
}
