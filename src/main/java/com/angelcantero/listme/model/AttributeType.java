package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attribute_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttributeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "attribute_type_id")
    private Long attributeTypeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String dataType;
}
