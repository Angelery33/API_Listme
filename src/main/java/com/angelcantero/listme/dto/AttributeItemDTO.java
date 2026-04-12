package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>AttributeItemDTO</strong></p>
 * <p>DTO para representar un atributo personalizado de un ítem.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeItemDTO {
    /**
     * Identificador del atributo.
     */
    private Long attributeItemId;

    /**
     * Valor del atributo.
     */
    @NotBlank(message = "El valor es obligatorio")
    private String value;

    /**
     * Identificador del ítem.
     */
    @NotNull(message = "El ID del ítem es obligatorio")
    private Long idItem;

    /**
     * Identificador del tipo de atributo.
     */
    @NotNull(message = "El ID del tipo de atributo es obligatorio")
    private Long attributeTypeId;
}
