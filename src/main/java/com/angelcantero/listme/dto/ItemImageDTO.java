package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDTO {
    private Long idImage;

    @NotNull(message = "El ID del ítem es obligatorio")
    private Long idItem;

    private String imageUri;
    private String remoteImageUrl;
}
