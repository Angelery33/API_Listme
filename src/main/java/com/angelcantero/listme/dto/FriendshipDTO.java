package com.angelcantero.listme.dto;

import com.angelcantero.listme.model.FriendshipStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO que representa una solicitud de amistad entre dos usuarios.
 *
 * <p>Se utiliza tanto para devolver solicitudes pendientes recibidas como para
 * mostrar el historial de solicitudes enviadas.</p>
 *
 * @author Angel Cantero
 * @since 1.5.0
 */
@Data
public class FriendshipDTO {

    /** Identificador único de la solicitud de amistad. */
    private Long id;

    /** Nombre de usuario del remitente de la solicitud. */
    private String senderUsername;

    /** URL de la foto de perfil del remitente, o {@code null} si no tiene. */
    private String senderPhotoUrl;

    /** Nombre de usuario del destinatario de la solicitud. */
    private String receiverUsername;

    /** URL de la foto de perfil del destinatario, o {@code null} si no tiene. */
    private String receiverPhotoUrl;

    /** Estado actual de la solicitud ({@code PENDING}, {@code ACCEPTED} o {@code REJECTED}). */
    private FriendshipStatus status;

    /** Fecha y hora en que se envió la solicitud. */
    private LocalDateTime createdAt;
}
