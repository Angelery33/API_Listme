package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p><strong>AttributeType</strong></p>
 * <p>Entidad que representa un tipo de atributo personalizado.</p>
 * <p>Los tipos de atributos definen qué campos adicionales pueden tener los ítems
 * (ej: "Autor", "Editorial", "Director", "Plataforma").</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "attribute_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttributeType {

    /**
     * Identificador único del tipo de atributo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "attribute_type_id")
    private Long attributeTypeId;

    /**
     * Nombre del tipo de atributo.
     * Ej: "Autor", "Director", "Plataforma".
     */
    @Column(nullable = false)
    private String name;

    /**
     * Tipo de dato del atributo.
     * Ej: "STRING", "NUMBER", "DATE".
     */
    @Column(nullable = false)
    private String dataType;
}
