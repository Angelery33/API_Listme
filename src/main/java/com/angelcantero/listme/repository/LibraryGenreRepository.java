package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.LibraryGenre;

import java.util.List;

@Repository
public interface LibraryGenreRepository extends JpaRepository<LibraryGenre, Long> {
    List<LibraryGenre> findByLibraryIdLibrary(Long libraryId);
}
