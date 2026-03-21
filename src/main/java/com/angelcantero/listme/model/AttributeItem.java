package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attribute_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttributeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "attribute_item_id")
    private Long attributeItemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attribute_type_id", nullable = false)
    @ToString.Exclude
    private AttributeType attributeType;

    @Column(nullable = false, length = 2000)
    private String value;
}
