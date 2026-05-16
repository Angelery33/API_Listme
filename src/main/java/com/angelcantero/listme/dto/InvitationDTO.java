package com.angelcantero.listme.dto;

import com.angelcantero.listme.model.InvitationStatus;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO para transferir información de una invitación entre usuarios.
 *
 * <p>Representa los datos relevantes de una invitación al ser enviada a la capa
 * de presentación o al cliente, evitando exponer directamente las entidades JPA.</p>
 */
@Data
public class InvitationDTO {

    /** Identificador único de la invitación. */
    private Long id;

    /** Nombre de usuario de quien envía la invitación. */
    private String senderUsername;

    /** Nombre de usuario de quien recibe la invitación. */
    private String receiverUsername;

    /** Identificador de la biblioteca a la que se invita. */
    private Long libraryId;

    /** Nombre descriptivo de la biblioteca a la que se invita. */
    private String libraryName;

    /** Indica si el acceso concedido es de solo lectura ({@code true}) o de edición ({@code false}). */
    private boolean readOnly;

    /** Estado actual de la invitación (PENDING, ACCEPTED o REJECTED). */
    private InvitationStatus status;

    /** Fecha y hora en que se creó la invitación. */
    private LocalDateTime createdAt;
}
