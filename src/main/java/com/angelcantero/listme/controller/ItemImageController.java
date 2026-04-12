package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.ItemImageDTO;
import com.angelcantero.listme.service.ItemImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p><strong>ItemImageController</strong></p>
 * <p>Controlador para la gestión de imágenes de ítems.</p>
 * <p>Endpoints para crear, listar, actualizar y eliminar imágenes de ítems.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/images")
@RequiredArgsConstructor
public class ItemImageController {

    private final ItemImageService service;

    /**
     * Obtiene todas las imágenes de un ítem.
     *
     * @param itemId ID del ítem
     * @return lista de imágenes
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ItemImageDTO>> findByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(service.getImagesByItemId(itemId));
    }

    /**
     * Crea una nueva imagen para un ítem.
     *
     * @param dto datos de la imagen
     * @return la imagen creada
     */
    @PostMapping
    public ResponseEntity<ItemImageDTO> create(@Valid @RequestBody ItemImageDTO dto) {
        return new ResponseEntity<>(service.createImage(dto), HttpStatus.CREATED);
    }

    /**
     * Actualiza una imagen existente.
     *
     * @param id ID de la imagen
     * @param dto nuevos datos
     * @return la imagen actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemImageDTO> update(@PathVariable Long id, @Valid @RequestBody ItemImageDTO dto) {
        return ResponseEntity.ok(service.updateImage(id, dto));
    }

    /**
     * Establece una imagen como favorita.
     *
     * @param itemId ID del ítem
     * @param imageId ID de la imagen
     * @return la imagen marcada
     */
    @PutMapping("/item/{itemId}/favorite/{imageId}")
    public ResponseEntity<ItemImageDTO> setFavorite(@PathVariable Long itemId, @PathVariable Long imageId) {
        return ResponseEntity.ok(service.setFavorite(itemId, imageId));
    }

    /**
     * Elimina una imagen.
     *
     * @param id ID de la imagen
     * @return sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
