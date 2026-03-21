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
 * <p>Gestión de los valores de atributos asignados a los ítems.</p>
 * Created on: 2026-03-20
 *
 * @author Angel Cantero
 * @version 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/attribute-items")
@RequiredArgsConstructor
public class AttributeItemController {

    private final AttributeItemService service;

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<AttributeItemDTO>> findByItemId(@PathVariable Long itemId) {
        return ResponseEntity.ok(service.getByItemId(itemId));
    }

    @PostMapping
    public ResponseEntity<AttributeItemDTO> create(@Valid @RequestBody AttributeItemDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
