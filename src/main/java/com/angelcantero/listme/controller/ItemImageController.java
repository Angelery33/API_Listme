package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.ItemImageDTO;
import com.angelcantero.listme.service.ItemImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
@Slf4j
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
     * Sube una imagen a Firebase Storage y crea su registro.
     *
     * @param file archivo de imagen
     * @param itemId ID del ítem
     * @return la imagen creada con URL remota
     */
    @PostMapping("/upload")
    public ResponseEntity<ItemImageDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("itemId") Long itemId) {
        try {
            log.info("Subiendo imagen para ítem: {}", itemId);
            ItemImageDTO result = service.uploadImage(file, itemId);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("Error al subir imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            log.warn("Validación de archivo fallida: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
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
     * Obtiene una imagen específica de un ítem (con validación de acceso).
     * Redirige a Firebase Storage para descargar la imagen.
     *
     * @param itemId ID del ítem
     * @param imageId ID de la imagen
     * @return redirección a la imagen en Firebase Storage
     */
    @GetMapping("/{itemId}/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable Long itemId, @PathVariable Long imageId) {
        try {
            log.info("Obteniendo imagen {} del ítem {}", imageId, itemId);
            String imageUrl = service.getImageUrl(itemId, imageId);
            if (imageUrl == null || imageUrl.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            // Redirige al cliente a la URL de Firebase Storage
            return ResponseEntity.status(org.springframework.http.HttpStatus.MOVED_PERMANENTLY)
                    .header("Location", imageUrl)
                    .build();
        } catch (Exception e) {
            log.error("Error al obtener imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
