package com.angelcantero.listme.service;

import com.angelcantero.listme.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.angelcantero.listme.dto.ItemImageDTO;
import com.angelcantero.listme.model.Item;
import com.angelcantero.listme.model.ItemImage;
import com.angelcantero.listme.repository.ItemImageRepository;
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
public class ItemImageService {
    private final ItemImageRepository itemImageRepository;
    private final ItemRepository itemRepository;
    private final LibraryService libraryService; // Changed from LibraryRepository
    private final UsuarioRepository usuarioRepository; // Kept for potential future use or if other methods not shown use it.

    // Removed getCurrentUser() as LibraryService handles user context

    private void validateLibraryReadAccess(Long libraryId) {
        libraryService.validateLibraryReadAccess(libraryId);
    }

    private void validateLibraryWriteAccess(Long libraryId) {
        libraryService.validateLibraryWriteAccess(libraryId);
    }

    public List<ItemImageDTO> getImagesByItemId(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        validateLibraryReadAccess(item.getLibrary().getIdLibrary());
        return itemImageRepository.findByItemIdItem(itemId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public ItemImageDTO createImage(ItemImageDTO dto) {
        Item item = itemRepository.findById(dto.getIdItem())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        validateLibraryWriteAccess(item.getLibrary().getIdLibrary());

        ItemImage image = new ItemImage();
        image.setRemoteImageUrl(dto.getRemoteImageUrl());
        image.setImageUri(dto.getImageUri());
        image.setItem(item);
        return mapToDTO(itemImageRepository.save(image));
    }

    @Transactional
    public void deleteImage(Long id) {
        ItemImage image = itemImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        validateLibraryWriteAccess(image.getItem().getLibrary().getIdLibrary()); // Changed validation method
        itemImageRepository.delete(image); // Changed from deleteById
    }

    private ItemImageDTO mapToDTO(ItemImage image) {
        ItemImageDTO dto = new ItemImageDTO();
        dto.setIdImage(image.getIdImage());
        dto.setIdItem(image.getItem().getIdItem());
        dto.setImageUri(image.getImageUri());
        dto.setRemoteImageUrl(image.getRemoteImageUrl());
        return dto;
    }
}
