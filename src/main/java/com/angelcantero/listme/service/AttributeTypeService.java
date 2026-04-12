package com.angelcantero.listme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.angelcantero.listme.dto.AttributeTypeDTO;
import com.angelcantero.listme.model.AttributeType;
import com.angelcantero.listme.repository.AttributeTypeRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p><strong>AttributeTypeService</strong></p>
 * <p>Servicio para la gestión de tipos de atributos.</p>
 * <p>Administra los tipos de atributos personalizados disponibles.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttributeTypeService {

    private final AttributeTypeRepository repository;

    /**
     * Obtiene todos los tipos de atributos.
     *
     * @return lista de tipos de atributos
     */
    public List<AttributeTypeDTO> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Crea un nuevo tipo de atributo.
     *
     * @param dto datos del tipo
     * @return el tipo creado
     * @throws IllegalArgumentException si el nombre ya existe
     */
    @Transactional
    public AttributeTypeDTO create(AttributeTypeDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del tipo de atributo no puede estar vacío");
        }
        
        boolean exists = repository.findAll().stream()
                .anyMatch(t -> t.getName().equalsIgnoreCase(dto.getName()));
        if (exists) {
            throw new IllegalArgumentException("Ya existe un tipo de atributo con el nombre: " + dto.getName());
        }
        
        AttributeType entity = new AttributeType();
        entity.setName(dto.getName());
        entity.setDataType(dto.getDataType());
        return mapToDTO(repository.save(entity));
    }

    /**
     * Elimina un tipo de atributo.
     *
     * @param id ID del tipo a eliminar
     */
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /**
     * Convierte una entidad a DTO.
     *
     * @param entity la entidad
     * @return el DTO
     */
    private AttributeTypeDTO mapToDTO(AttributeType entity) {
        AttributeTypeDTO dto = new AttributeTypeDTO();
        dto.setAttributeTypeId(entity.getAttributeTypeId());
        dto.setName(entity.getName());
        dto.setDataType(entity.getDataType());
        return dto;
    }
}
