package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "image_id")
    private Long idImage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    private Item item;

    @Column(nullable = false)
    private String imageUri;

    private String remoteImageUrl;
}
