package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.InvitationDTO;
import com.angelcantero.listme.dto.ShareRequest;
import com.angelcantero.listme.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de invitaciones entre usuarios.
 *
 * <p>Expone los endpoints necesarios para enviar invitaciones a una biblioteca,
 * consultar las invitaciones pendientes del usuario autenticado, y aceptar
 * o rechazar una invitación recibida.</p>
 */
@RestController
@RequestMapping(Config.API_URL + "/invitations")
@RequiredArgsConstructor
public class InvitationController {

    /** Servicio que encapsula la lógica de negocio de las invitaciones. */
    private final InvitationService invitationService;

    /**
     * Envía una invitación a otro usuario para que acceda a una biblioteca concreta.
     *
     * @param libraryId identificador de la biblioteca a la que se invita.
     * @param request   datos de la solicitud, incluyendo el nombre de usuario destinatario
     *                  y si el acceso será de solo lectura.
     * @return {@link ResponseEntity} con el {@link InvitationDTO} de la invitación creada.
     */
    @PostMapping("/library/{libraryId}")
    public ResponseEntity<InvitationDTO> send(@PathVariable Long libraryId, @Valid @RequestBody ShareRequest request) {
        return ResponseEntity.ok(invitationService.sendInvitation(libraryId, request));
    }

    /**
     * Obtiene todas las invitaciones pendientes del usuario autenticado.
     *
     * @return {@link ResponseEntity} con la lista de {@link InvitationDTO} en estado pendiente.
     */
    @GetMapping("/pending")
    public ResponseEntity<List<InvitationDTO>> getPending() {
        return ResponseEntity.ok(invitationService.getPendingInvitations());
    }

    /**
     * Acepta una invitación recibida por el usuario autenticado.
     *
     * <p>Al aceptar, el usuario queda registrado como editor o visor de la biblioteca
     * según el tipo de acceso definido en la invitación.</p>
     *
     * @param id identificador de la invitación que se desea aceptar.
     * @return {@link ResponseEntity} sin cuerpo con estado HTTP 204 (No Content).
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id) {
        invitationService.acceptInvitation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rechaza una invitación recibida por el usuario autenticado.
     *
     * @param id identificador de la invitación que se desea rechazar.
     * @return {@link ResponseEntity} sin cuerpo con estado HTTP 204 (No Content).
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        invitationService.rejectInvitation(id);
        return ResponseEntity.noContent().build();
    }
}
