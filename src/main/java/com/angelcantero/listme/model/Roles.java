package com.angelcantero.listme.model;

/**
 * <p><strong>Roles</strong></p>
 * <p>Enumeración que define los roles disponibles en el sistema.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
public enum Roles {
    /**
     * Rol de administrador con acceso total al sistema.
     * Puede gestionar usuarios y todas las bibliotecas.
     */
    ADMIN,

    /**
     * Rol estándar con permisos limitados.
     * Puede crear y gestionar sus propias bibliotecas.
     */
    STANDARD
}
