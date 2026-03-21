package com.angelcantero.listme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemAttributeDetailsDTO {
    private Long attributeItemId;
    private Long attributeTypeId;
    private String name;
    private String value;
}
