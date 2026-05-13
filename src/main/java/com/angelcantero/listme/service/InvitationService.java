package com.angelcantero.listme.service;

import com.angelcantero.listme.dto.InvitationDTO;
import com.angelcantero.listme.dto.ShareRequest;
import com.angelcantero.listme.exception.ResourceNotFoundException;
import com.angelcantero.listme.model.*;
import com.angelcantero.listme.repository.InvitationRepository;
import com.angelcantero.listme.repository.LibraryRepository;
import com.angelcantero.listme.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final LibraryRepository libraryRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public InvitationDTO sendInvitation(Long libraryId, ShareRequest request) {
        Usuario sender = getCurrentUser();
        Library library = libraryRepository.findOwnedById(libraryId, sender)
                .orElseThrow(() -> new ResourceNotFoundException("Only owner can invite to library"));

        Usuario receiver = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("You cannot invite yourself");
        }

        Invitation invitation = Invitation.builder()
                .sender(sender)
                .receiver(receiver)
                .library(library)
                .readOnly(request.isReadOnly())
                .status(InvitationStatus.PENDING)
                .build();

        return mapToDTO(invitationRepository.save(invitation));
    }

    public List<InvitationDTO> getPendingInvitations() {
        Usuario currentUser = getCurrentUser();
        return invitationRepository.findByReceiverAndStatus(currentUser, InvitationStatus.PENDING)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public void acceptInvitation(Long invitationId) {
        Usuario currentUser = getCurrentUser();
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (!invitation.getReceiver().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only accept your own invitations");
        }

        Library library = invitation.getLibrary();
        if (invitation.isReadOnly()) {
            library.getViewers().add(currentUser);
        } else {
            library.getEditors().add(currentUser);
        }

        invitation.setStatus(InvitationStatus.ACCEPTED);
        libraryRepository.save(library);
        invitationRepository.save(invitation);
    }

    @Transactional
    public void rejectInvitation(Long invitationId) {
        Usuario currentUser = getCurrentUser();
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        if (!invitation.getReceiver().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only reject your own invitations");
        }

        invitation.setStatus(InvitationStatus.REJECTED);
        invitationRepository.save(invitation);
    }

    private InvitationDTO mapToDTO(Invitation invitation) {
        InvitationDTO dto = new InvitationDTO();
        dto.setId(invitation.getId());
        dto.setSenderUsername(invitation.getSender().getUsername());
        dto.setReceiverUsername(invitation.getReceiver().getUsername());
        dto.setLibraryId(invitation.getLibrary().getIdLibrary());
        dto.setLibraryName(invitation.getLibrary().getName());
        dto.setReadOnly(invitation.isReadOnly());
        dto.setStatus(invitation.getStatus());
        dto.setCreatedAt(invitation.getCreatedAt());
        return dto;
    }
}
