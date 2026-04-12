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
 * <p>Controlador para la gestión de bibliotecas del usuario.</p>
 * <p>Endpoints para crear, listar, actualizar, eliminar y compartir bibliotecas.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    /**
     * Obtiene todas las bibliotecas del usuario.
     *
     * @return lista de bibliotecas
     */
    @GetMapping
    public ResponseEntity<List<LibraryDTO>> findAll() {
        return ResponseEntity.ok(libraryService.getAllLibraries());
    }

    /**
     * Obtiene una biblioteca por su ID.
     *
     * @param id ID de la biblioteca
     * @return la biblioteca
     */
    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.getLibraryById(id));
    }

    /**
     * Crea una nueva biblioteca.
     *
     * @param libraryDTO datos de la biblioteca
     * @return la biblioteca creada
     */
    @PostMapping
    public ResponseEntity<LibraryDTO> create(@Valid @RequestBody LibraryDTO libraryDTO) {
        return new ResponseEntity<>(libraryService.createLibrary(libraryDTO), HttpStatus.CREATED);
    }

    /**
     * Actualiza una biblioteca existente.
     *
     * @param id ID de la biblioteca
     * @param libraryDTO nuevos datos
     * @return la biblioteca actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<LibraryDTO> update(@PathVariable Long id, @Valid @RequestBody LibraryDTO libraryDTO) {
        return ResponseEntity.ok(libraryService.updateLibrary(id, libraryDTO));
    }

    /**
     * Comparte una biblioteca con otro usuario.
     *
     * @param id ID de la biblioteca
     * @param shareRequest datos de compartición
     * @return sin contenido
     */
    @PostMapping("/{id}/share")
    public ResponseEntity<Void> share(@PathVariable Long id, @Valid @RequestBody com.angelcantero.listme.dto.ShareRequest shareRequest) {
        libraryService.shareLibrary(id, shareRequest);
        return ResponseEntity.ok().build();
    }

    /**
     * Elimina una biblioteca.
     *
     * @param id ID de la biblioteca
     * @return sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reordena las bibliotecas del usuario.
     *
     * @param items lista con IDs y posiciones
     * @return sin contenido
     */
    @PutMapping("/reorder")
    public ResponseEntity<Void> reorder(@Valid @RequestBody List<LibraryReorderItemDTO> items) {
        libraryService.reorderLibraries(items);
        return ResponseEntity.noContent().build();
    }
}
