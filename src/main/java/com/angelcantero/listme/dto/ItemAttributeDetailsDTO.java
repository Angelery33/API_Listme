package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>ItemAttributeDetailsDTO</strong></p>
 * <p>DTO para detalles de atributos de un ítem.</p>
 * <p>Incluye información del tipo de atributo además del valor.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemAttributeDetailsDTO {
    /**
     * Identificador del atributo del ítem.
     */
    private Long attributeItemId;

    /**
     * Identificador del tipo de atributo.
     */
    private Long attributeTypeId;

    /**
     * Nombre del tipo de atributo.
     */
    private String name;

    /**
     * Valor del atributo.
     */
    private String value;
}
