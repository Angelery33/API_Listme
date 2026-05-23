package com.angelcantero.listme.service;

import com.angelcantero.listme.dto.FriendDTO;
import com.angelcantero.listme.dto.FriendshipDTO;
import com.angelcantero.listme.exception.ResourceNotFoundException;
import com.angelcantero.listme.model.Friendship;
import com.angelcantero.listme.model.FriendshipStatus;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.FriendshipRepository;
import com.angelcantero.listme.repository.ItemRepository;
import com.angelcantero.listme.repository.LibraryRepository;
import com.angelcantero.listme.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio de las amistades entre usuarios.
 *
 * <p>Cubre el ciclo completo: enviar solicitud, aceptar, rechazar, obtener
 * la lista de amigos con estadísticas y eliminar una amistad.</p>
 *
 * @author Angel Cantero
 * @since 1.5.0
 */
@Service
@RequiredArgsConstructor
public class FriendshipService {

    /** Repositorio de solicitudes de amistad. */
    private final FriendshipRepository friendshipRepository;

    /** Repositorio de usuarios para buscar al destinatario. */
    private final UsuarioRepository usuarioRepository;

    /** Repositorio de bibliotecas para calcular estadísticas del amigo. */
    private final LibraryRepository libraryRepository;

    /** Repositorio de ítems para calcular estadísticas del amigo. */
    private final ItemRepository itemRepository;

    /**
     * Obtiene el {@link Usuario} autenticado actualmente.
     *
     * @return usuario del contexto de seguridad.
     * @throws ResourceNotFoundException si el nombre de usuario no existe en la base de datos.
     */
    private Usuario getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    /**
     * Envía una solicitud de amistad al usuario con el nombre {@code targetUsername}.
     *
     * @param targetUsername nombre de usuario del destinatario.
     * @return {@link FriendshipDTO} con los datos de la solicitud creada.
     * @throws ResourceNotFoundException si el destinatario no existe.
     * @throws IllegalArgumentException  si el remitente intenta enviarse una solicitud a sí mismo
     *                                   o si ya existe una solicitud entre ambos usuarios.
     */
    @Transactional
    public FriendshipDTO sendRequest(String targetUsername) {
        Usuario sender = getCurrentUser();
        Usuario receiver = usuarioRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + targetUsername));

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("No puedes enviarte una solicitud de amistad a ti mismo");
        }

        if (friendshipRepository.existsBetween(sender, receiver)) {
            throw new IllegalArgumentException("Ya existe una solicitud o amistad con este usuario");
        }

        Friendship friendship = Friendship.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendshipStatus.PENDING)
                .build();

        return mapToDTO(friendshipRepository.save(friendship));
    }

    /**
     * Devuelve todas las solicitudes de amistad pendientes recibidas por el usuario autenticado.
     *
     * @return lista de {@link FriendshipDTO} con estado {@code PENDING}.
     */
    @Transactional(readOnly = true)
    public List<FriendshipDTO> getPendingRequests() {
        Usuario currentUser = getCurrentUser();
        return friendshipRepository.findByReceiverAndStatus(currentUser, FriendshipStatus.PENDING)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Acepta la solicitud de amistad con el id indicado.
     *
     * <p>Solo el destinatario de la solicitud puede aceptarla.</p>
     *
     * @param friendshipId id de la solicitud de amistad.
     * @throws ResourceNotFoundException si la solicitud no existe.
     * @throws SecurityException         si el usuario autenticado no es el destinatario.
     * @throws IllegalStateException     si la solicitud no está en estado {@code PENDING}.
     */
    @Transactional
    public void acceptRequest(Long friendshipId) {
        Usuario currentUser = getCurrentUser();
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        if (!friendship.getReceiver().getId().equals(currentUser.getId())) {
            throw new SecurityException("Solo puedes aceptar tus propias solicitudes");
        }
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("La solicitud ya fue respondida");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    /**
     * Rechaza la solicitud de amistad con el id indicado.
     *
     * <p>Solo el destinatario de la solicitud puede rechazarla.</p>
     *
     * @param friendshipId id de la solicitud de amistad.
     * @throws ResourceNotFoundException si la solicitud no existe.
     * @throws SecurityException         si el usuario autenticado no es el destinatario.
     * @throws IllegalStateException     si la solicitud no está en estado {@code PENDING}.
     */
    @Transactional
    public void rejectRequest(Long friendshipId) {
        Usuario currentUser = getCurrentUser();
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        if (!friendship.getReceiver().getId().equals(currentUser.getId())) {
            throw new SecurityException("Solo puedes rechazar tus propias solicitudes");
        }
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("La solicitud ya fue respondida");
        }

        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
    }

    /**
     * Devuelve la lista de amigos del usuario autenticado con sus estadísticas de uso.
     *
     * @return lista de {@link FriendDTO} con username, foto de perfil, listas totales e ítems totales.
     */
    @Transactional(readOnly = true)
    public List<FriendDTO> getFriends() {
        Usuario currentUser = getCurrentUser();
        return friendshipRepository.findAcceptedFriendships(currentUser)
                .stream()
                .map(f -> {
                    Usuario friend = f.getSender().getId().equals(currentUser.getId())
                            ? f.getReceiver()
                            : f.getSender();
                    long libraries = libraryRepository.countByUsuario(friend);
                    long items = itemRepository.countByLibraryUsuario(friend);
                    return new FriendDTO(friend.getId(), friend.getUsername(), friend.getPhotoUrl(), libraries, items);
                })
                .collect(Collectors.toList());
    }

    /**
     * Elimina una amistad existente entre el usuario autenticado y {@code targetUsername}.
     *
     * @param targetUsername nombre de usuario del amigo a eliminar.
     * @throws ResourceNotFoundException si el usuario no existe o no hay amistad activa entre ambos.
     * @throws SecurityException         si el usuario autenticado no es parte de la amistad.
     */
    @Transactional
    public void removeFriend(String targetUsername) {
        Usuario currentUser = getCurrentUser();
        Usuario target = usuarioRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + targetUsername));

        Friendship friendship = friendshipRepository.findBetween(currentUser, target)
                .orElseThrow(() -> new ResourceNotFoundException("No existe amistad con ese usuario"));

        if (!friendship.getSender().getId().equals(currentUser.getId()) &&
            !friendship.getReceiver().getId().equals(currentUser.getId())) {
            throw new SecurityException("No puedes eliminar una amistad en la que no participas");
        }

        friendshipRepository.delete(friendship);
    }

    /**
     * Convierte una entidad {@link Friendship} a su {@link FriendshipDTO} correspondiente.
     *
     * @param f la entidad a convertir.
     * @return el DTO poblado.
     */
    private FriendshipDTO mapToDTO(Friendship f) {
        FriendshipDTO dto = new FriendshipDTO();
        dto.setId(f.getId());
        dto.setSenderUsername(f.getSender().getUsername());
        dto.setSenderPhotoUrl(f.getSender().getPhotoUrl());
        dto.setReceiverUsername(f.getReceiver().getUsername());
        dto.setReceiverPhotoUrl(f.getReceiver().getPhotoUrl());
        dto.setStatus(f.getStatus());
        dto.setCreatedAt(f.getCreatedAt());
        return dto;
    }
}
