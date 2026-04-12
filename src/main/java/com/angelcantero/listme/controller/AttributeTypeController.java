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
 * <p>Controlador para la gestión de tipos de atributos.</p>
 * <p>Endpoints para crear, listar y eliminar tipos de atributos personalizados.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/attribute-types")
@RequiredArgsConstructor
public class AttributeTypeController {

    private final AttributeTypeService service;

    /**
     * Obtiene todos los tipos de atributos.
     *
     * @return lista de tipos de atributos
     */
    @GetMapping
    public ResponseEntity<List<AttributeTypeDTO>> findAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Crea un nuevo tipo de atributo.
     *
     * @param dto datos del tipo
     * @return el tipo creado
     */
    @PostMapping
    public ResponseEntity<AttributeTypeDTO> create(@Valid @RequestBody AttributeTypeDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    /**
     * Elimina un tipo de atributo.
     *
     * @param id ID del tipo a eliminar
     * @return sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
