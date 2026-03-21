package com.angelcantero.listme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.angelcantero.listme.dto.AttributeTypeDTO;
import com.angelcantero.listme.model.AttributeType;
import com.angelcantero.listme.repository.AttributeTypeRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttributeTypeService {

    private final AttributeTypeRepository repository;

    public List<AttributeTypeDTO> getAll() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public AttributeTypeDTO create(AttributeTypeDTO dto) {
        AttributeType entity = new AttributeType();
        entity.setName(dto.getName());
        entity.setDataType(dto.getDataType());
        return mapToDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private AttributeTypeDTO mapToDTO(AttributeType entity) {
        AttributeTypeDTO dto = new AttributeTypeDTO();
        dto.setAttributeTypeId(entity.getAttributeTypeId());
        dto.setName(entity.getName());
        dto.setDataType(entity.getDataType());
        return dto;
    }
}
