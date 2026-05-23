package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad que representa una solicitud de amistad entre dos usuarios.
 *
 * <p>El campo {@code sender} es el usuario que envía la solicitud y {@code receiver}
 * el que la recibe. El estado evoluciona de {@link FriendshipStatus#PENDING} a
 * {@link FriendshipStatus#ACCEPTED} o {@link FriendshipStatus#REJECTED}.</p>
 *
 * <p>Se aplica una restricción única sobre el par {@code (sender_id, receiver_id)}
 * para evitar solicitudes duplicadas.</p>
 *
 * @author Angel Cantero
 * @since 1.5.0
 */
@Entity
@Table(
    name = "friendship",
    uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Friendship {

    /**
     * Identificador único de la solicitud de amistad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Usuario que envía la solicitud de amistad.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Usuario sender;

    /**
     * Usuario que recibe la solicitud de amistad.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Usuario receiver;

    /**
     * Estado actual de la solicitud.
     * Por defecto {@link FriendshipStatus#PENDING} al crear la solicitud.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private FriendshipStatus status = FriendshipStatus.PENDING;

    /**
     * Fecha y hora en que se creó la solicitud, gestionada automáticamente por Hibernate.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
