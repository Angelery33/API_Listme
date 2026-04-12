package com.angelcantero.listme.service;

import com.angelcantero.listme.exception.ResourceNotFoundException;
import com.angelcantero.listme.model.RefreshToken;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.RefreshTokenRepository;
import com.angelcantero.listme.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * <p><strong>RefreshTokenService</strong></p>
 * <p>Servicio para la gestión de tokens de refresco.</p>
 * <p>Maneja la creación, validación y eliminación de refresh tokens.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    /**
     * Tiempo de expiración del refresh token en milisegundos (por defecto 30 días).
     */
    @Value("${application.security.jwt.refresh-token.expiration:2592000000}")
    private long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Busca un refresh token por su valor.
     *
     * @param token el token a buscar
     * @return Optional conteniendo el token si existe
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Crea o actualiza un token de refresco para un usuario.
     * Si el usuario ya tiene un token, lo renueva con nueva fecha de expiración.
     *
     * @param userId el ID del usuario
     * @return el refresh token creado/actualizado
     * @throws ResourceNotFoundException si el usuario no existe
     */
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
        
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUsuario(usuario);

        RefreshToken refreshToken;
        if (existingToken.isPresent()) {
            refreshToken = existingToken.get();
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUsuario(usuario);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
        }

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Verifica si un refresh token no ha expirado.
     *
     * @param token el token a verificar
     * @return el token si es válido
     * @throws RuntimeException si el token ha expirado
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("El token de refresco ha expirado. Por favor, inicie sesión nuevamente");
        }
        return token;
    }

    /**
     * Elimina todos los tokens de refresco de un usuario.
     *
     * @param userId el ID del usuario
     * @return cantidad de tokens eliminados
     * @throws ResourceNotFoundException si el usuario no existe
     */
    @Transactional
    public int deleteByUserId(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));
        return refreshTokenRepository.deleteByUsuario(usuario);
    }
}