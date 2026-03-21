package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.AttributeTypeDTO;
import com.angelcantero.listme.service.AttributeTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p><strong>AttributeTypeController</strong></p>
 * <p>Gestión de los tipos de atributos disponibles para las bibliotecas.</p>
 * Created on: 2026-03-20
 *
 * @author Angel Cantero
 * @version 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/attribute-types")
@RequiredArgsConstructor
public class AttributeTypeController {

    private final AttributeTypeService service;

    @GetMapping
    public ResponseEntity<List<AttributeTypeDTO>> findAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<AttributeTypeDTO> create(@Valid @RequestBody AttributeTypeDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
