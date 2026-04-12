package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.AttributeItemDTO;
import com.angelcantero.listme.service.AttributeItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p><strong>AttributeItemController</strong></p>
 * <p>Controlador para la gestión de atributos de ítems.</p>
 * <p>Endpoints para crear, listar y eliminar atributos personalizados de ítems.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/attribute-items")
@RequiredArgsConstructor
public class AttributeItemController {

    private final AttributeItemService service;

    /**
     * Obtiene todos los atributos de un ítem.
     *
     * @param itemId ID del ítem
     * @return lista de atributos
     */
    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<AttributeItemDTO>> findByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(service.getByItemId(itemId));
    }

    /**
     * Crea un nuevo atributo para un ítem.
     *
     * @param dto datos del atributo
     * @return el atributo creado
     */
    @PostMapping
    public ResponseEntity<AttributeItemDTO> create(@Valid @RequestBody AttributeItemDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    /**
     * Elimina un atributo.
     *
     * @param id ID del atributo
     * @return sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
