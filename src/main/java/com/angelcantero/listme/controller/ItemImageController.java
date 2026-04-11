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
 * <p>Gestión de las imágenes asociadas a los ítems.</p>
 * Created on: 2026-03-20
 *
 * @author Angel Cantero
 * @version 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/images")
@RequiredArgsConstructor
public class ItemImageController {

    private final ItemImageService service;

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ItemImageDTO>> findByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(service.getImagesByItemId(itemId));
    }

    @PostMapping
    public ResponseEntity<ItemImageDTO> create(@Valid @RequestBody ItemImageDTO dto) {
        return new ResponseEntity<>(service.createImage(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemImageDTO> update(@PathVariable Long id, @Valid @RequestBody ItemImageDTO dto) {
        return ResponseEntity.ok(service.updateImage(id, dto));
    }

    @PutMapping("/item/{itemId}/favorite/{imageId}")
    public ResponseEntity<ItemImageDTO> setFavorite(@PathVariable Long itemId, @PathVariable Long imageId) {
        return ResponseEntity.ok(service.setFavorite(itemId, imageId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
