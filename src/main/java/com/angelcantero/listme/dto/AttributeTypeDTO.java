package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeTypeDTO {
    private Long attributeTypeId;

    @NotBlank(message = "El nombre del tipo de atributo es obligatorio")
    private String name;

    @NotBlank(message = "El tipo de dato es obligatorio")
    private String dataType;
}
