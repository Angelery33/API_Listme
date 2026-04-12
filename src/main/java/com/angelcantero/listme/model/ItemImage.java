package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p><strong>ItemImage</strong></p>
 * <p>Entidad que representa una imagen asociada a un ítem.</p>
 * <p>Un ítem puede tener múltiples imágenes, incluyendo una imagen favorita
 * que se muestra como portada principal.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "item_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemImage {

    /**
     * Identificador único de la imagen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_image")
    private Long idImage;

    /**
     * Ítem al que pertenece la imagen.
     */
    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_item", nullable = false)
    @ToString.Exclude
    private Item item;

    /**
     * URI local de la imagen.
     */
    @Column(nullable = false)
    private String imageUri;

    /**
     * URL remota de la imagen.
     */
    private String remoteImageUrl;

    /**
     * Indica si esta imagen es la favoritos del ítem.
     */
    private Boolean isFavorite = false;
}
