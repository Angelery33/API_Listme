package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>AttributeTypeDTO</strong></p>
 * <p>DTO para representar un tipo de atributo personalizado.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeTypeDTO {
    /**
     * Identificador del tipo de atributo.
     */
    private Long attributeTypeId;

    /**
     * Nombre del tipo de atributo.
     */
    @NotBlank(message = "El nombre del tipo de atributo es obligatorio")
    private String name;

    /**
     * Tipo de dato del atributo.
     */
    @NotBlank(message = "El tipo de dato es obligatorio")
    private String dataType;
}
