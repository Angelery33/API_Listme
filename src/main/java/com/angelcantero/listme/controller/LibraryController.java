package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.LibraryDTO;
import com.angelcantero.listme.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.angelcantero.listme.dto.LibraryReorderItemDTO;

/**
 * <p><strong>LibraryController</strong></p>
 * <p>Gestión de las bibliotecas del usuario.</p>
 * Created on: 2026-03-20
 *
 * @author Angel Cantero
 * @version 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping
    public ResponseEntity<List<LibraryDTO>> findAll() {
        return ResponseEntity.ok(libraryService.getAllLibraries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.getLibraryById(id));
    }

    @PostMapping
    public ResponseEntity<LibraryDTO> create(@Valid @RequestBody LibraryDTO libraryDTO) {
        return new ResponseEntity<>(libraryService.createLibrary(libraryDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryDTO> update(@PathVariable Long id, @Valid @RequestBody LibraryDTO libraryDTO) {
        return ResponseEntity.ok(libraryService.updateLibrary(id, libraryDTO));
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Void> share(@PathVariable Long id, @Valid @RequestBody com.angelcantero.listme.dto.ShareRequest shareRequest) {
        libraryService.shareLibrary(id, shareRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reorder")
    public ResponseEntity<Void> reorder(@Valid @RequestBody List<LibraryReorderItemDTO> items) {
        libraryService.reorderLibraries(items);
        return ResponseEntity.noContent().build();
    }
}
