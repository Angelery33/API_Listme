package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_item")
    private Long idItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_library", nullable = false)
    @ToString.Exclude
    private Library library;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    private Long itemDate; // named 'date' in dart, renaming to itemDate in Java to avoid keyword collision

    @Column(nullable = false)
    private String status = "PENDING";

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Item parentItem;
    
    private Double externalRating;

    private Double imageAlignmentX;
    private Double imageAlignmentY;

    private String itemNumber;
    private String productType;
    private String edition;
}
