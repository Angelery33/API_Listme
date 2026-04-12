package com.angelcantero.listme.exception;

/**
 * <p><strong>InvalidRefreshTokenException</strong></p>
 * <p>Excepción para tokens de refresco inválidos o expirados.</p>
 * <p>Se lanza cuando un refresh token no es válido o ha expirado.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
public class InvalidRefreshTokenException extends RuntimeException {

    /**
     * Crea una excepción con el mensaje especificado.
     *
     * @param message mensaje de error
     */
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}