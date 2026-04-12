package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p><strong>AttributeItem</strong></p>
 * <p>Entidad que representa un valor de atributo personalizado asignado a un ítem.</p>
 * <p>Permite agregar información adicional y personalizada a cada ítem de la biblioteca.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "attribute_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttributeItem {

    /**
     * Identificador único del atributo del ítem.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "attribute_item_id")
    private Long attributeItemId;

    /**
     * Ítem al que pertenece el atributo.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_item", nullable = false)
    @ToString.Exclude
    private Item item;

    /**
     * Tipo de atributo que define el nombre y tipo de dato.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "attribute_type_id", nullable = false)
    @ToString.Exclude
    private AttributeType attributeType;

    /**
     * Valor del atributo.
     * Almacena el contenido textual del atributo.
     */
    @Column(nullable = false, length = 2000)
    private String value;
}
