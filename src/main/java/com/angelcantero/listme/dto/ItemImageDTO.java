package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p><strong>ItemImageDTO</strong></p>
 * <p>DTO para representar una imagen de ítem.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDTO {
    /**
     * Identificador de la imagen.
     */
    private Long idImage;

    /**
     * Identificador del ítem.
     */
    @NotNull(message = "El ID del ítem es obligatorio")
    private Long idItem;

    /**
     * URI de la imagen local.
     */
    private String imageUri;

    /**
     * URL de la imagen remota.
     */
    private String remoteImageUrl;

    /**
     * Indica si es la imagen favorita.
     */
    private Boolean isFavorite = false;
}
