package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.LibraryGenre;

import java.util.List;

/**
 * <p><strong>LibraryGenreRepository</strong></p>
 * <p>Repositorio para la entidad LibraryGenre.</p>
 * <p>Proporciona métodos de acceso a datos para géneros de bibliotecas.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface LibraryGenreRepository extends JpaRepository<LibraryGenre, Long> {
    
    /**
     * Busca todos los géneros de una biblioteca.
     *
     * @param libraryId el ID de la biblioteca
     * @return lista de géneros
     */
    List<LibraryGenre> findByLibraryIdLibrary(Long libraryId);
}
