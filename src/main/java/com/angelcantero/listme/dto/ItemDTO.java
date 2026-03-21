package com.angelcantero.listme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long idItem;

    @NotNull(message = "El ID de la biblioteca es obligatorio")
    private Long idLibrary;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    private String description;
    private Long date;
    private String status;
    private String imagePath;
    private Double score;
    private String genre;
    private Long startDate;
    private Long completionDate;
    private boolean isWishlist;
    private Long acquisitionDate;
    private Double price;
    private boolean isCurrent;
    private String remoteImageUrl;
    private String progressUnit;
    private Integer currentProgress;
    private Integer totalProgress;
    private Integer season;
    private Integer chapter;
    private Integer page;
    private Integer volume;
    private Integer totalSeason;
    private Integer totalChapter;
    private Integer totalPage;
    private Integer totalVolume;
    private boolean isCollection;
    private Long parentId;
    private Double externalRating;
    private Double imageAlignmentX;
    private Double imageAlignmentY;
    private String itemNumber;
    private String productType;
    private String edition;
}
