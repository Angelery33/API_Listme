package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.FriendDTO;
import com.angelcantero.listme.dto.FriendshipDTO;
import com.angelcantero.listme.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de amistades entre usuarios.
 *
 * <p>Expone los endpoints necesarios para enviar solicitudes de amistad, consultar
 * las solicitudes pendientes recibidas, aceptar o rechazar una solicitud,
 * obtener la lista de amigos con estadísticas y eliminar una amistad.</p>
 *
 * <p>Todos los endpoints requieren autenticación JWT.</p>
 *
 * @author Angel Cantero
 * @since 1.5.0
 */
@RestController
@RequestMapping(Config.API_URL + "/friends")
@RequiredArgsConstructor
public class FriendshipController {

    /** Servicio que encapsula la lógica de negocio de las amistades. */
    private final FriendshipService friendshipService;

    /**
     * Envía una solicitud de amistad al usuario indicado.
     *
     * @param username nombre de usuario del destinatario.
     * @return {@link ResponseEntity} con el {@link FriendshipDTO} de la solicitud creada.
     */
    @PostMapping("/request/{username}")
    public ResponseEntity<FriendshipDTO> sendRequest(@PathVariable String username) {
        return ResponseEntity.ok(friendshipService.sendRequest(username));
    }

    /**
     * Obtiene todas las solicitudes de amistad pendientes recibidas por el usuario autenticado.
     *
     * @return {@link ResponseEntity} con la lista de {@link FriendshipDTO} en estado pendiente.
     */
    @GetMapping("/requests/pending")
    public ResponseEntity<List<FriendshipDTO>> getPendingRequests() {
        return ResponseEntity.ok(friendshipService.getPendingRequests());
    }

    /**
     * Acepta la solicitud de amistad con el id indicado.
     *
     * @param id identificador de la solicitud de amistad.
     * @return {@link ResponseEntity} sin cuerpo con estado HTTP 204 (No Content).
     */
    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long id) {
        friendshipService.acceptRequest(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechaza la solicitud de amistad con el id indicado.
     *
     * @param id identificador de la solicitud de amistad.
     * @return {@link ResponseEntity} sin cuerpo con estado HTTP 204 (No Content).
     */
    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long id) {
        friendshipService.rejectRequest(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene la lista de amigos del usuario autenticado con sus estadísticas de uso.
     *
     * @return {@link ResponseEntity} con la lista de {@link FriendDTO}.
     */
    @GetMapping
    public ResponseEntity<List<FriendDTO>> getFriends() {
        return ResponseEntity.ok(friendshipService.getFriends());
    }

    /**
     * Elimina la amistad entre el usuario autenticado y el usuario indicado.
     *
     * @param username nombre de usuario del amigo a eliminar.
     * @return {@link ResponseEntity} sin cuerpo con estado HTTP 204 (No Content).
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> removeFriend(@PathVariable String username) {
        friendshipService.removeFriend(username);
        return ResponseEntity.noContent().build();
    }
}
