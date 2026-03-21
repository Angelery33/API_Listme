package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "library_genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LibraryGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "library_id", nullable = false)
    @ToString.Exclude
    private Library library;

    @Column(nullable = false)
    private String name;
}
