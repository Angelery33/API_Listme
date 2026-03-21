package com.angelcantero.listme.service;

import com.angelcantero.listme.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.angelcantero.listme.dto.ItemDTO;
import com.angelcantero.listme.model.Item;
import com.angelcantero.listme.model.Library;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.ItemRepository;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final LibraryRepository libraryRepository;
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

    public List<ItemDTO> getAllItems() {
        return itemRepository.findAllAccessibleByUser(getCurrentUser()).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ItemDTO> getItemsByLibraryId(Long libraryId) {
        validateLibraryReadAccess(libraryId);
        return itemRepository.findByLibraryIdLibrary(libraryId).stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        validateLibraryReadAccess(item.getLibrary().getIdLibrary());
        return mapToDTO(item);
    }

    @Transactional
    public ItemDTO createItem(ItemDTO createDTO) {
        validateLibraryWriteAccess(createDTO.getIdLibrary());
        Library library = libraryRepository.findById(createDTO.getIdLibrary())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with id: " + createDTO.getIdLibrary()));

        Item parentItem = null;
        if (createDTO.getParentId() != null) {
            parentItem = itemRepository.findById(createDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Item not found with id: " + createDTO.getParentId()));
            validateLibraryReadAccess(parentItem.getLibrary().getIdLibrary());
        }

        Item item = new Item();
        populateEntityFromDTO(item, createDTO, library, parentItem);
        return mapToDTO(itemRepository.save(item));
    }

    @Transactional
    public ItemDTO updateItem(Long id, ItemDTO updateDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        validateLibraryWriteAccess(item.getLibrary().getIdLibrary());

        Library library = libraryRepository.findById(updateDTO.getIdLibrary())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found with id: " + updateDTO.getIdLibrary()));
        validateLibraryWriteAccess(library.getIdLibrary());

        Item parentItem = null;
        if (updateDTO.getParentId() != null) {
            parentItem = itemRepository.findById(updateDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent Item not found with id: " + updateDTO.getParentId()));
            validateLibraryReadAccess(parentItem.getLibrary().getIdLibrary());
        }

        populateEntityFromDTO(item, updateDTO, library, parentItem);
        return mapToDTO(itemRepository.save(item));
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        validateLibraryWriteAccess(item.getLibrary().getIdLibrary());
        itemRepository.deleteById(id);
    }
    private void populateEntityFromDTO(Item item, ItemDTO dto, Library library, Item parentItem) {
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setItemDate(dto.getDate());
        item.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        item.setImagePath(dto.getImagePath());
        item.setScore(dto.getScore());
        item.setGenre(dto.getGenre());
        item.setStartDate(dto.getStartDate());
        item.setCompletionDate(dto.getCompletionDate());
        item.setWishlist(dto.isWishlist());
        item.setAcquisitionDate(dto.getAcquisitionDate());
        item.setPrice(dto.getPrice());
        item.setCurrent(dto.isCurrent());
        item.setRemoteImageUrl(dto.getRemoteImageUrl());
        item.setProgressUnit(dto.getProgressUnit());
        item.setCurrentProgress(dto.getCurrentProgress());
        item.setTotalProgress(dto.getTotalProgress());
        item.setSeason(dto.getSeason());
        item.setChapter(dto.getChapter());
        item.setPage(dto.getPage());
        item.setVolume(dto.getVolume());
        item.setTotalSeason(dto.getTotalSeason());
        item.setTotalChapter(dto.getTotalChapter());
        item.setTotalPage(dto.getTotalPage());
        item.setTotalVolume(dto.getTotalVolume());
        item.setCollection(dto.isCollection());
        item.setParentItem(parentItem);
        item.setExternalRating(dto.getExternalRating());
        item.setImageAlignmentX(dto.getImageAlignmentX());
        item.setImageAlignmentY(dto.getImageAlignmentY());
        item.setItemNumber(dto.getItemNumber());
        item.setProductType(dto.getProductType());
        item.setEdition(dto.getEdition());
        item.setLibrary(library);
    }

    private ItemDTO mapToDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setIdItem(item.getIdItem());
        dto.setIdLibrary(item.getLibrary() != null ? item.getLibrary().getIdLibrary() : null);
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setDate(item.getItemDate());
        dto.setStatus(item.getStatus());
        dto.setImagePath(item.getImagePath());
        dto.setScore(item.getScore());
        dto.setGenre(item.getGenre());
        dto.setStartDate(item.getStartDate());
        dto.setCompletionDate(item.getCompletionDate());
        dto.setWishlist(item.isWishlist());
        dto.setAcquisitionDate(item.getAcquisitionDate());
        dto.setPrice(item.getPrice());
        dto.setCurrent(item.isCurrent());
        dto.setRemoteImageUrl(item.getRemoteImageUrl());
        dto.setProgressUnit(item.getProgressUnit());
        dto.setCurrentProgress(item.getCurrentProgress());
        dto.setTotalProgress(item.getTotalProgress());
        dto.setSeason(item.getSeason());
        dto.setChapter(item.getChapter());
        dto.setPage(item.getPage());
        dto.setVolume(item.getVolume());
        dto.setTotalSeason(item.getTotalSeason());
        dto.setTotalChapter(item.getTotalChapter());
        dto.setTotalPage(item.getTotalPage());
        dto.setTotalVolume(item.getTotalVolume());
        dto.setCollection(item.isCollection());
        dto.setParentId(item.getParentItem() != null ? item.getParentItem().getIdItem() : null);
        dto.setExternalRating(item.getExternalRating());
        dto.setImageAlignmentX(item.getImageAlignmentX());
        dto.setImageAlignmentY(item.getImageAlignmentY());
        dto.setItemNumber(item.getItemNumber());
        dto.setProductType(item.getProductType());
        dto.setEdition(item.getEdition());
        return dto;
    }
}
