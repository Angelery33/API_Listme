package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "library")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_library")
    private Long idLibrary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "library_editors",
            joinColumns = @JoinColumn(name = "library_id", referencedColumnName = "id_library"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<Usuario> editors = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "library_viewers",
            joinColumns = @JoinColumn(name = "library_id", referencedColumnName = "id_library"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    private Set<Usuario> viewers = new HashSet<>();

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private java.util.List<Item> items = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private java.util.List<LibraryGenre> genres = new java.util.ArrayList<>();

    @Column(nullable = false)
    private String name;

    private String type;

    private boolean supportsCompletion;
    private boolean isGradeable;
    private boolean isThematic;
    private boolean supportsWishlist;
    private boolean tracksDates;
    private boolean supportsPrice;

    @Column(length = 2000)
    private String description;

    private int genreLayoutMode;
    private boolean isCompact;

    @Column(name = "position_index")
    private int position;

    private boolean supportsProgress;
    private String progressType;
    private String customProgressUnit;
    private String defaultCategory;
    private int ratingScale = 10;
}
