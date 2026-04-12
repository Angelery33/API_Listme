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

/**
 * <p><strong>LibraryService</strong></p>
 * <p>Servicio para la gestión de bibliotecas.</p>
 * <p>Proporciona operaciones CRUD y de compartición para bibliotecas.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryService {

    private final LibraryRepository libraryRepository;
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
     * Obtiene todas las bibliotecas accesibles por el usuario.
     *
     * @return lista de bibliotecas
     */
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

    /**
     * Obtiene una biblioteca por su ID.
     *
     * @param id ID de la biblioteca
     * @return la biblioteca encontrada
     * @throws ResourceNotFoundException si no existe o no tiene acceso
     */
    public LibraryDTO getLibraryById(Long id) {
        Usuario currentUser = getCurrentUser();
        Library library = libraryRepository.findAccessibleById(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Library not found or you don't have access"));
        return mapToDTOWithFlags(library, currentUser);
    }

    /**
     * Crea una nueva biblioteca.
     *
     * @param createDTO datos de la biblioteca
     * @return la biblioteca creada
     */
    @Transactional
    public LibraryDTO createLibrary(LibraryDTO createDTO) {
        Library library = new Library();
        Usuario currentUser = getCurrentUser();
        library.setUsuario(currentUser);
        populateEntityFromDTO(library, createDTO);
        return mapToDTOWithFlags(libraryRepository.save(library), currentUser);
    }

    /**
     * Actualiza una biblioteca existente.
     *
     * @param id ID de la biblioteca
     * @param updateDTO nuevos datos
     * @return la biblioteca actualizada
     * @throws ResourceNotFoundException si no es propietario
     */
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

    /**
     * Elimina una biblioteca.
     *
     * @param id ID de la biblioteca
     * @throws ResourceNotFoundException si no es propietario
     */
    @Transactional
    public void deleteLibrary(Long id) {
        Library library = libraryRepository.findOwnedById(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Only the owner can delete the library"));
        libraryRepository.delete(library);
    }

    /**
     * Reordena las bibliotecas del usuario.
     *
     * @param items lista con IDs y nuevas posiciones
     * @throws ResourceNotFoundException si no tiene permiso sobre alguna
     */
    @Transactional
    public void reorderLibraries(List<LibraryReorderItemDTO> items) {
        Usuario currentUser = getCurrentUser();
        List<Long> ids = items.stream().map(LibraryReorderItemDTO::getId).collect(Collectors.toList());
        Map<Long, Integer> positionMap = items.stream()
                .collect(Collectors.toMap(LibraryReorderItemDTO::getId, LibraryReorderItemDTO::getPosition));

        List<Library> libraries = libraryRepository.findAllById(ids);
        
        for (Library lib : libraries) {
            if (!lib.getUsuario().getId().equals(currentUser.getId())) {
                throw new ResourceNotFoundException("No tienes permiso para reordenar la biblioteca: " + lib.getName());
            }
            lib.setPosition(positionMap.get(lib.getIdLibrary()));
        }
        libraryRepository.saveAll(libraries);
    }

    /**
     * Comparte una biblioteca con otro usuario.
     *
     * @param id ID de la biblioteca
     * @param shareRequest datos de compartición
     * @throws ResourceNotFoundException si no es propietario o usuario no existe
     */
    public void shareLibrary(Long id, com.angelcantero.listme.dto.ShareRequest shareRequest) {
        Library library = libraryRepository.findOwnedById(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Only the owner can share the library"));
        
        Usuario currentUser = getCurrentUser();
        Usuario targetUser = usuarioRepository.findByUsername(shareRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found: " + shareRequest.getUsername()));
        
        if (targetUser.getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("No puedes compartir una biblioteca contigo mismo");
        }
        
        if (shareRequest.isReadOnly()) {
            library.getViewers().add(targetUser);
            library.getEditors().remove(targetUser);
        } else {
            library.getEditors().add(targetUser);
            library.getViewers().remove(targetUser);
        }
        libraryRepository.save(library);
    }

    /**
     * Valida que el usuario tenga acceso de lectura.
     *
     * @param libraryId ID de la biblioteca
     * @throws ResourceNotFoundException si no tiene acceso
     */
    public void validateLibraryReadAccess(Long libraryId) {
        if (libraryId == null) return;
        libraryRepository.findAccessibleById(libraryId, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found or you don't have access"));
    }

    /**
     * Valida que el usuario tenga acceso de escritura.
     *
     * @param libraryId ID de la biblioteca
     * @throws ResourceNotFoundException si no tiene permiso
     */
    public void validateLibraryWriteAccess(Long libraryId) {
        if (libraryId == null) return;
        libraryRepository.findEditableById(libraryId, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found or you don't have permission to edit"));
    }

    /**
     * Convierte una entidad a DTO con flags de acceso.
     *
     * @param library la entidad
     * @param currentUser el usuario actual
     * @return el DTO
     */
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

    /**
     * Pobla una entidad desde un DTO.
     *
     * @param entity la entidad
     * @param dto el DTO
     */
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

    /**
     * Convierte una entidad a DTO.
     *
     * @param library la entidad
     * @return el DTO
     */
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
