package com.angelcantero.listme.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * <p><strong>Usuario</strong></p>
 * <p>Entidad que representa un usuario en el sistema.</p>
 * <p>Cada usuario tiene un nombre único, correo electrónico, contraseña encriptada
 * y un rol que determina sus permisos en la aplicación.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Entity
@Table(name = "usuario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
    /**
     * Identificador único del usuario (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Nombre de usuario único, usado para autenticación.
     * No puede ser nulo ni estar duplicado en el sistema.
     */
    @Column(name = "nombre", unique = true, nullable = false)
    private String username;

    /**
     * Contraseña encriptada del usuario.
     * Se almacena codificada usando BCrypt.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Correo electrónico único del usuario.
     * No puede ser nulo ni estar duplicado en el sistema.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Rol del usuario que determina sus permisos.
     * Valores posibles: ADMIN, STANDARD.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Roles rol;
}
