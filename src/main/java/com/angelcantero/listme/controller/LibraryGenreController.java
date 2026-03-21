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
 * <p>Gestión de los géneros definidos para cada biblioteca.</p>
 * Created on: 2026-03-20
 *
 * @author Angel Cantero
 * @version 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/library-genres")
@RequiredArgsConstructor
public class LibraryGenreController {

    private final LibraryGenreService service;

    @GetMapping("/library/{libraryId}")
    public ResponseEntity<List<LibraryGenreDTO>> findByLibraryId(@PathVariable Long libraryId) {
        return ResponseEntity.ok(service.getGenresByLibraryId(libraryId));
    }

    @PostMapping
    public ResponseEntity<LibraryGenreDTO> create(@Valid @RequestBody LibraryGenreDTO dto) {
        return new ResponseEntity<>(service.createGenre(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
