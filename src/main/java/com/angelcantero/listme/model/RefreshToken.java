package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * <p><strong>RefreshToken</strong></p>
 * <p>Entidad que representa un token de refresco para la autenticación JWT.</p>
 * <p>Los refresh tokens permiten obtener nuevos access tokens sin necesidad
 * de volver a autenticarse con credenciales.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    /**
     * Identificador único del token de refresco.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Token único generado aleatoriamente.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * Fecha de expiración del token.
     * El token no será válido después de esta fecha.
     */
    @Column(nullable = false)
    private Instant expiryDate;

    /**
     * Usuario propietario del token de refresco.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
}
