package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.LibraryGenreDTO;
import com.angelcantero.listme.service.LibraryGenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p><strong>LibraryGenreController</strong></p>
 * <p>Controlador para la gestión de géneros de bibliotecas.</p>
 * <p>Endpoints para crear, listar y eliminar géneros dentro de cada biblioteca.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/library-genres")
@RequiredArgsConstructor
public class LibraryGenreController {

    private final LibraryGenreService service;

    /**
     * Obtiene los géneros de una biblioteca.
     *
     * @param libraryId ID de la biblioteca
     * @return lista de géneros
     */
    @GetMapping("/library/{libraryId}")
    public ResponseEntity<List<LibraryGenreDTO>> findByLibraryId(@PathVariable Long libraryId) {
        return ResponseEntity.ok(service.getGenresByLibraryId(libraryId));
    }

    /**
     * Crea un nuevo género.
     *
     * @param dto datos del género
     * @return el género creado
     */
    @PostMapping
    public ResponseEntity<LibraryGenreDTO> create(@Valid @RequestBody LibraryGenreDTO dto) {
        return new ResponseEntity<>(service.createGenre(dto), HttpStatus.CREATED);
    }

    /**
     * Elimina un género.
     *
     * @param id ID del género
     * @return sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
