package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeItemDTO {
    private Long attributeItemId;

    @NotBlank(message = "El valor es obligatorio")
    private String value;

    @NotNull(message = "El ID del ítem es obligatorio")
    private Long idItem;

    @NotNull(message = "El ID del tipo de atributo es obligatorio")
    private Long attributeTypeId;
}
