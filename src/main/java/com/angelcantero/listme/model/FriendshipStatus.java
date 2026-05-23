package com.angelcantero.listme.model;

/**
 * Estados posibles de una solicitud de amistad entre dos usuarios.
 *
 * <ul>
 *   <li>{@link #PENDING}  – La solicitud ha sido enviada pero aún no ha sido respondida.</li>
 *   <li>{@link #ACCEPTED} – El destinatario aceptó la solicitud; ambos son amigos.</li>
 *   <li>{@link #REJECTED} – El destinatario rechazó la solicitud.</li>
 * </ul>
 *
 * @author Angel Cantero
 * @since 1.5.0
 */
public enum FriendshipStatus {

    /** Solicitud enviada y pendiente de respuesta. */
    PENDING,

    /** Solicitud aceptada; la amistad está activa. */
    ACCEPTED,

    /** Solicitud rechazada por el destinatario. */
    REJECTED
}
