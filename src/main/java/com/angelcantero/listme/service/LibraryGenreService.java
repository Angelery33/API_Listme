package com.angelcantero.listme.service;

import com.angelcantero.listme.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.angelcantero.listme.dto.LibraryGenreDTO;
import com.angelcantero.listme.model.Library;
import com.angelcantero.listme.model.LibraryGenre;
import com.angelcantero.listme.repository.LibraryGenreRepository;
import com.angelcantero.listme.repository.LibraryRepository;

import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryGenreService {
    private final LibraryGenreRepository libraryGenreRepository;
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

    public List<LibraryGenreDTO> getGenresByLibraryId(Long libraryId) {
        validateLibraryReadAccess(libraryId);
        return libraryGenreRepository.findByLibraryIdLibrary(libraryId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public LibraryGenreDTO createGenre(LibraryGenreDTO dto) {
        validateLibraryWriteAccess(dto.getLibraryId());
        Library library = libraryRepository.findById(dto.getLibraryId())
                .orElseThrow(() -> new ResourceNotFoundException("Library not found"));
        LibraryGenre genre = new LibraryGenre();
        genre.setName(dto.getName());
        genre.setLibrary(library);
        return mapToDTO(libraryGenreRepository.save(genre));
    }

    @Transactional
    public void deleteGenre(Long id) {
        LibraryGenre genre = libraryGenreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        validateLibraryWriteAccess(genre.getLibrary().getIdLibrary());
        libraryGenreRepository.delete(genre);
    }

    private LibraryGenreDTO mapToDTO(LibraryGenre genre) {
        LibraryGenreDTO dto = new LibraryGenreDTO();
        dto.setId(genre.getId());
        dto.setLibraryId(genre.getLibrary().getIdLibrary());
        dto.setName(genre.getName());
        return dto;
    }
}
