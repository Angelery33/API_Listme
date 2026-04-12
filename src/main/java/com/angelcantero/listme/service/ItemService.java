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

/**
 * <p><strong>ItemService</strong></p>
 * <p>Servicio para la gestión de ítems.</p>
 * <p>Proporciona operaciones CRUD para ítems dentro de bibliotecas.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final LibraryRepository libraryRepository;
    private final LibraryService libraryService;
    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene el usuario actualmente autenticado.
     *
     * @return el usuario actual
     */
    private Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Valida que el usuario tenga acceso de lectura a la biblioteca.
     *
     * @param libraryId ID de la biblioteca
     */
    private void validateLibraryReadAccess(Long libraryId) {
        libraryService.validateLibraryReadAccess(libraryId);
    }

    /**
     * Valida que el usuario tenga acceso de escritura a la biblioteca.
     *
     * @param libraryId ID de la biblioteca
     */
    private void validateLibraryWriteAccess(Long libraryId) {
        libraryService.validateLibraryWriteAccess(libraryId);
    }

    /**
     * Obtiene todos los ítems accesibles por el usuario.
     *
     * @return lista de ítems
     */
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAllAccessibleByUser(getCurrentUser()).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Obtiene los ítems de una biblioteca específica.
     *
     * @param libraryId ID de la biblioteca
     * @return lista de ítems de la biblioteca
     */
    public List<ItemDTO> getItemsByLibraryId(Long libraryId) {
        validateLibraryReadAccess(libraryId);
        return itemRepository.findByLibraryIdLibrary(libraryId).stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un ítem por su ID.
     *
     * @param id ID del ítem
     * @return el ítem encontrado
     * @throws ResourceNotFoundException si no existe
     */
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        
        if (item.getLibrary() == null) {
            throw new ResourceNotFoundException("El ítem no tiene una biblioteca asociada");
        }
        
        validateLibraryReadAccess(item.getLibrary().getIdLibrary());
        return mapToDTO(item);
    }

    /**
     * Crea un nuevo ítem.
     *
     * @param createDTO datos del ítem a crear
     * @return el ítem creado
     * @throws ResourceNotFoundException si la biblioteca no existe
     */
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

    /**
     * Actualiza un ítem existente.
     *
     * @param id ID del ítem a actualizar
     * @param updateDTO nuevos datos del ítem
     * @return el ítem actualizado
     * @throws ResourceNotFoundException si el ítem no existe
     */
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

    /**
     * Elimina un ítem.
     *
     * @param id ID del ítem a eliminar
     * @throws ResourceNotFoundException si no existe
     */
    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        validateLibraryWriteAccess(item.getLibrary().getIdLibrary());
        itemRepository.deleteById(id);
    }

    /**
     * Popula una entidad Item desde un DTO.
     *
     * @param item la entidad a poblar
     * @param dto el DTO con los datos
     * @param library la biblioteca
     * @param parentItem el ítem padre (si aplica)
     */
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

    /**
     * Convierte una entidad Item a DTO.
     *
     * @param item la entidad
     * @return el DTO
     */
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
