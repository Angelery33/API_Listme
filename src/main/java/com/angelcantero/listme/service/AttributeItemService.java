package com.angelcantero.listme.service;

import com.angelcantero.listme.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.angelcantero.listme.dto.AttributeItemDTO;
import com.angelcantero.listme.model.AttributeItem;
import com.angelcantero.listme.model.AttributeType;
import com.angelcantero.listme.model.Item;
import com.angelcantero.listme.repository.AttributeItemRepository;
import com.angelcantero.listme.repository.AttributeTypeRepository;
import com.angelcantero.listme.repository.ItemRepository;

import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.LibraryRepository;
import com.angelcantero.listme.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttributeItemService {
    private final AttributeItemRepository attributeItemRepository;
    private final AttributeTypeRepository attributeTypeRepository;
    private final ItemRepository itemRepository;
    private final LibraryService libraryService;
    private final UsuarioRepository usuarioRepository;

    private Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateLibraryReadAccess(Long libraryId) {
        libraryService.validateLibraryReadAccess(libraryId);
    }

    private void validateLibraryWriteAccess(Long libraryId) {
        libraryService.validateLibraryWriteAccess(libraryId);
    }

    public List<AttributeItemDTO> getByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        validateLibraryReadAccess(item.getLibrary().getIdLibrary());
        return attributeItemRepository.findByItemIdItem(itemId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public AttributeItemDTO create(AttributeItemDTO dto) {
        Item item = itemRepository.findById(dto.getIdItem())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        validateLibraryWriteAccess(item.getLibrary().getIdLibrary());

        AttributeType type = attributeTypeRepository.findById(dto.getAttributeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Attribute Type not found"));

        AttributeItem attribute = new AttributeItem();
        attribute.setValue(dto.getValue());
        attribute.setItem(item);
        attribute.setAttributeType(type);
        return mapToDTO(attributeItemRepository.save(attribute));
    }

    @Transactional
    public void delete(Long id) {
        AttributeItem attribute = attributeItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute not found"));
        validateLibraryWriteAccess(attribute.getItem().getLibrary().getIdLibrary());
        attributeItemRepository.delete(attribute);
    }

    private AttributeItemDTO mapToDTO(AttributeItem entity) {
        AttributeItemDTO dto = new AttributeItemDTO();
        dto.setAttributeItemId(entity.getAttributeItemId());
        dto.setValue(entity.getValue());
        if (entity.getItem() != null)
            dto.setIdItem(entity.getItem().getIdItem());
        if (entity.getAttributeType() != null)
            dto.setAttributeTypeId(entity.getAttributeType().getAttributeTypeId());
        return dto;
    }
}
