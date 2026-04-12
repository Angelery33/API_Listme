package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.Usuario;

import java.util.Optional;

/**
 * <p><strong>UsuarioRepository</strong></p>
 * <p>Repositorio para la entidad Usuario.</p>
 * <p>Proporciona métodos de acceso a datos para usuarios.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario a buscar
     * @return Optional containing the usuario if found
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email el correo electrónico a buscar
     * @return Optional containing the usuario if found
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el nombre dado.
     *
     * @param username el nombre de usuario a verificar
     * @return true if exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el correo dado.
     *
     * @param email el correo electrónico a verificar
     * @return true if exists
     */
    boolean existsByEmail(String email);
}
