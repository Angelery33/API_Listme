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

@RestController
@RequestMapping(Config.API_URL + "/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/library/{libraryId}")
    public ResponseEntity<InvitationDTO> send(@PathVariable Long libraryId, @Valid @RequestBody ShareRequest request) {
        return ResponseEntity.ok(invitationService.sendInvitation(libraryId, request));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<InvitationDTO>> getPending() {
        return ResponseEntity.ok(invitationService.getPendingInvitations());
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long id) {
        invitationService.acceptInvitation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        invitationService.rejectInvitation(id);
        return ResponseEntity.noContent().build();
    }
}
