package com.angelcantero.listme.service;

import com.angelcantero.listme.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.angelcantero.listme.dto.LibraryDTO;
import com.angelcantero.listme.model.Library;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.LibraryRepository;
import com.angelcantero.listme.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.angelcantero.listme.dto.LibraryReorderItemDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<LibraryDTO> getAllLibraries() {
        Usuario currentUser = getCurrentUser();
        return libraryRepository.findAllAccessibleByUser(currentUser).stream()
                .map(library -> {
                    LibraryDTO dto = mapToDTO(library);
                    boolean isOwner = library.getUsuario().getId().equals(currentUser.getId());
                    boolean isEditor = library.getEditors().stream().anyMatch(u -> u.getId().equals(currentUser.getId()));
                    boolean isViewer = library.getViewers().stream().anyMatch(u -> u.getId().equals(currentUser.getId()));
                    dto.setOwner(isOwner);
                    dto.setShared(isEditor || isViewer);
                    dto.setCanEdit(isOwner || isEditor);
                    dto.setItemCount((long) library.getItems().size());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public LibraryDTO getLibraryById(Long id) {
        Usuario currentUser = getCurrentUser();
        Library library = libraryRepository.findAccessibleById(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found or you don't have access"));
        return mapToDTOWithFlags(library, currentUser);
    }

    @Transactional
    public LibraryDTO createLibrary(LibraryDTO createDTO) {
        Library library = new Library();
        Usuario currentUser = getCurrentUser();
        library.setUsuario(currentUser);
        populateEntityFromDTO(library, createDTO);
        return mapToDTOWithFlags(libraryRepository.save(library), currentUser);
    }

    @Transactional
    public LibraryDTO updateLibrary(Long id, LibraryDTO updateDTO) {
        Usuario currentUser = getCurrentUser();
        Library library = libraryRepository.findOwnedById(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Only the owner can update the library properties"));

        if (updateDTO != null) {
            populateEntityFromDTO(library, updateDTO);
        }
        return mapToDTOWithFlags(libraryRepository.save(library), currentUser);
    }

    @Transactional
    public void deleteLibrary(Long id) {
        Library library = libraryRepository.findOwnedById(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Only the owner can delete the library"));
        libraryRepository.delete(library);
    }

    @Transactional
    public void reorderLibraries(List<LibraryReorderItemDTO> items) {
        Usuario currentUser = getCurrentUser();
        List<Long> ids = items.stream().map(LibraryReorderItemDTO::getId).collect(Collectors.toList());
        Map<Long, Integer> positionMap = items.stream()
                .collect(Collectors.toMap(LibraryReorderItemDTO::getId, LibraryReorderItemDTO::getPosition));

        List<Library> libraries = libraryRepository.findAllById(ids);
        for(Library lib : libraries) {
            if(lib.getUsuario().getId().equals(currentUser.getId())) {
                lib.setPosition(positionMap.get(lib.getIdLibrary()));
            }
        }
        libraryRepository.saveAll(libraries);
    }

    public void shareLibrary(Long id, com.angelcantero.listme.dto.ShareRequest shareRequest) {
        Library library = libraryRepository.findOwnedById(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Only the owner can share the library"));
        Usuario targetUser = usuarioRepository.findByUsername(shareRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found: " + shareRequest.getUsername()));
        
        if (shareRequest.isReadOnly()) {
            library.getViewers().add(targetUser);
            library.getEditors().remove(targetUser); // Ensure it's not in both
        } else {
            library.getEditors().add(targetUser);
            library.getViewers().remove(targetUser); // Ensure it's not in both
        }
        libraryRepository.save(library);
    }

    public void validateLibraryReadAccess(Long libraryId) {
        if (libraryId == null) return;
        libraryRepository.findAccessibleById(libraryId, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found or you don't have access"));
    }

    public void validateLibraryWriteAccess(Long libraryId) {
        if (libraryId == null) return;
        libraryRepository.findEditableById(libraryId, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found or you don't have permission to edit"));
    }

    private LibraryDTO mapToDTOWithFlags(Library library, Usuario currentUser) {
        LibraryDTO dto = mapToDTO(library);
        boolean isOwner = library.getUsuario().getId().equals(currentUser.getId());
        boolean isEditor = library.getEditors().stream().anyMatch(u -> u.getId().equals(currentUser.getId()));
        boolean isViewer = library.getViewers().stream().anyMatch(u -> u.getId().equals(currentUser.getId()));

        dto.setOwner(isOwner);
        dto.setShared(isEditor || isViewer);
        dto.setCanEdit(isOwner || isEditor);
        dto.setItemCount((long) library.getItems().size());
        return dto;
    }

    private void populateEntityFromDTO(Library entity, LibraryDTO dto) {
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setSupportsCompletion(dto.isSupportsCompletion());
        entity.setGradeable(dto.isGradeable());
        entity.setThematic(dto.isThematic());
        entity.setSupportsWishlist(dto.isSupportsWishlist());
        entity.setTracksDates(dto.isTracksDates());
        entity.setSupportsPrice(dto.isSupportsPrice());
        entity.setDescription(dto.getDescription());
        entity.setGenreLayoutMode(dto.getGenreLayoutMode());
        entity.setCompact(dto.isCompact());
        entity.setPosition(dto.getPosition());
        entity.setSupportsProgress(dto.isSupportsProgress());
        entity.setProgressType(dto.getProgressType());
        entity.setCustomProgressUnit(dto.getCustomProgressUnit());
        entity.setDefaultCategory(dto.getDefaultCategory());
        entity.setRatingScale(dto.getRatingScale());
    }

    private LibraryDTO mapToDTO(Library library) {
        LibraryDTO dto = new LibraryDTO();
        dto.setIdLibrary(library.getIdLibrary());
        dto.setName(library.getName());
        dto.setType(library.getType());
        dto.setSupportsCompletion(library.isSupportsCompletion());
        dto.setGradeable(library.isGradeable());
        dto.setThematic(library.isThematic());
        dto.setSupportsWishlist(library.isSupportsWishlist());
        dto.setTracksDates(library.isTracksDates());
        dto.setSupportsPrice(library.isSupportsPrice());
        dto.setDescription(library.getDescription());
        dto.setGenreLayoutMode(library.getGenreLayoutMode());
        dto.setCompact(library.isCompact());
        dto.setPosition(library.getPosition());
        dto.setSupportsProgress(library.isSupportsProgress());
        dto.setProgressType(library.getProgressType());
        dto.setCustomProgressUnit(library.getCustomProgressUnit());
        dto.setDefaultCategory(library.getDefaultCategory());
        dto.setRatingScale(library.getRatingScale());
        return dto;
    }
}
