package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.ItemDTO;
import com.angelcantero.listme.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p><strong>ItemController</strong></p>
 * <p>Controlador para la gestión de ítems individuales en las bibliotecas.</p>
 * <p>Proporciona endpoints CRUD para crear, leer, actualizar y eliminar ítems.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * Obtiene todos los ítems accesibles por el usuario.
     *
     * @return lista de ítems
     */
    @GetMapping
    public ResponseEntity<List<ItemDTO>> findAll() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    /**
     * Obtiene los ítems de una biblioteca específica.
     *
     * @param libraryId ID de la biblioteca
     * @return lista de ítems
     */
    @GetMapping("/library/{libraryId}")
    public ResponseEntity<List<ItemDTO>> findByLibraryId(@PathVariable Long libraryId) {
        return ResponseEntity.ok(itemService.getItemsByLibraryId(libraryId));
    }

    /**
     * Obtiene un ítem por su ID.
     *
     * @param id ID del ítem
     * @return el ítem
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    /**
     * Crea un nuevo ítem.
     *
     * @param itemDTO datos del ítem
     * @return el ítem creado
     */
    @PostMapping
    public ResponseEntity<ItemDTO> create(@Valid @RequestBody ItemDTO itemDTO) {
        return new ResponseEntity<>(itemService.createItem(itemDTO), HttpStatus.CREATED);
    }

    /**
     * Actualiza un ítem existente.
     *
     * @param id ID del ítem
     * @param itemDTO nuevos datos
     * @return el ítem actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> update(@PathVariable Long id, @Valid @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.updateItem(id, itemDTO));
    }

    /**
     * Elimina un ítem.
     *
     * @param id ID del ítem a eliminar
     * @return sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
