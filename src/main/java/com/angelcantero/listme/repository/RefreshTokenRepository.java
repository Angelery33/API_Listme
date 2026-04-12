package com.angelcantero.listme.repository;

import com.angelcantero.listme.model.RefreshToken;
import com.angelcantero.listme.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <p><strong>RefreshTokenRepository</strong></p>
 * <p>Repositorio para la entidad RefreshToken.</p>
 * <p>Proporciona métodos de acceso a datos para tokens de refresco.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    /**
     * Busca un token de refresco por su valor.
     *
     * @param token el token a buscar
     * @return Optional con el token si existe
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * Busca el token de refresco de un usuario.
     *
     * @param usuario el usuario propietario
     * @return Optional con el token si existe
     */
    Optional<RefreshToken> findByUsuario(Usuario usuario);

    /**
     * Elimina todos los tokens de refresco de un usuario.
     *
     * @param usuario el usuario
     * @return cantidad de tokens eliminados
     */
    @Modifying
    int deleteByUsuario(Usuario usuario);
}
