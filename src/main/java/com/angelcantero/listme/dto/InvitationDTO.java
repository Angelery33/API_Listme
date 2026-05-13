package com.angelcantero.listme.dto;

import com.angelcantero.listme.model.InvitationStatus;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * <p><strong>InvitationDTO</strong></p>
 * <p>DTO para transferir información de una invitación.</p>
 */
@Data
public class InvitationDTO {
    private Long id;
    private String senderUsername;
    private String receiverUsername;
    private Long libraryId;
    private String libraryName;
    private boolean readOnly;
    private InvitationStatus status;
    private LocalDateTime createdAt;
}
