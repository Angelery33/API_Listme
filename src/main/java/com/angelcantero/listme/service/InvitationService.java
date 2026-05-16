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

/**
 * Servicio que gestiona la lógica de negocio de las invitaciones entre usuarios.
 *
 * <p>Permite al propietario de una biblioteca invitar a otros usuarios, y a los
 * destinatarios aceptar o rechazar dichas invitaciones. Al aceptar, el usuario
 * queda registrado como editor o visor según el tipo de acceso definido.</p>
 */
@Service
@RequiredArgsConstructor
public class InvitationService {

    /** Repositorio para persistir y consultar las invitaciones. */
    private final InvitationRepository invitationRepository;

    /** Repositorio para buscar y actualizar las bibliotecas involucradas. */
    private final LibraryRepository libraryRepository;

    /** Repositorio para recuperar entidades de usuario por nombre. */
    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene la entidad {@link Usuario} correspondiente al usuario autenticado en la sesión actual.
     *
     * @return el {@link Usuario} autenticado.
     * @throws ResourceNotFoundException si el nombre de usuario del contexto de seguridad no existe en la base de datos.
     */
    private Usuario getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Envía una invitación a un usuario para que acceda a una biblioteca.
     *
     * <p>Solo el propietario de la biblioteca puede invitar. No se permite
     * que un usuario se invite a sí mismo.</p>
     *
     * @param libraryId identificador de la biblioteca a compartir.
     * @param request   datos de la invitación: nombre de usuario destinatario y tipo de acceso.
     * @return {@link InvitationDTO} con los datos de la invitación recién creada.
     * @throws ResourceNotFoundException si la biblioteca no pertenece al usuario autenticado
     *                                   o si el usuario destinatario no existe.
     * @throws IllegalArgumentException  si el remitente intenta invitarse a sí mismo.
     */
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

    /**
     * Devuelve todas las invitaciones pendientes dirigidas al usuario autenticado.
     *
     * @return lista de {@link InvitationDTO} con estado {@code PENDING} para el usuario actual.
     */
    @Transactional(readOnly = true)
    public List<InvitationDTO> getPendingInvitations() {
        Usuario currentUser = getCurrentUser();
        return invitationRepository.findByReceiverAndStatus(currentUser, InvitationStatus.PENDING)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Acepta una invitación pendiente y concede acceso a la biblioteca.
     *
     * <p>Añade al usuario autenticado como visor o editor de la biblioteca según
     * el campo {@code readOnly} de la invitación, y actualiza su estado a {@code ACCEPTED}.</p>
     *
     * @param invitationId identificador de la invitación a aceptar.
     * @throws ResourceNotFoundException si la invitación no existe.
     * @throws SecurityException         si la invitación no pertenece al usuario autenticado.
     */
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

    /**
     * Rechaza una invitación pendiente.
     *
     * <p>Actualiza el estado de la invitación a {@code REJECTED}. El usuario
     * no obtiene acceso a la biblioteca.</p>
     *
     * @param invitationId identificador de la invitación a rechazar.
     * @throws ResourceNotFoundException si la invitación no existe.
     * @throws SecurityException         si la invitación no pertenece al usuario autenticado.
     */
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
