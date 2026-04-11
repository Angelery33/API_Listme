package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO {
    private Long idLibrary;

    @NotBlank(message = "El nombre de la biblioteca es obligatorio")
    private String name;
    private String type;
    private boolean supportsCompletion;
    private boolean isGradeable;
    private boolean isThematic;
    private boolean supportsWishlist;
    private boolean tracksDates;
    private boolean supportsPrice;
    private String description;
    private int genreLayoutMode;
    private boolean isCompact;
    private int position;
    private boolean supportsProgress;
    private String progressType;
    private String customProgressUnit;
    private String defaultCategory;
    private int ratingScale;
    private boolean isOwner;
    private boolean isShared;
    private boolean canEdit;
    private Long itemCount;
}
