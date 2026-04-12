package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.Library;
import com.angelcantero.listme.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * <p><strong>LibraryRepository</strong></p>
 * <p>Repositorio para la entidad Library.</p>
 * <p>Proporciona métodos de acceso a datos para bibliotecas.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface LibraryRepository extends JpaRepository<Library, Long> {
    
    /**
     * Busca todas las bibliotecas accesibles por un usuario.
     * Incluye las propias y las compartidas (editores y visualizadores).
     *
     * @param usuario el usuario que accede
     * @return lista de bibliotecas accesibles
     */
    @Query("SELECT DISTINCT l FROM Library l LEFT JOIN FETCH l.items i LEFT JOIN l.editors e LEFT JOIN l.viewers v WHERE l.usuario = :usuario OR :usuario MEMBER OF l.editors OR :usuario MEMBER OF l.viewers")
    List<Library> findAllAccessibleByUser(@Param("usuario") Usuario usuario);

    /**
     * Busca una biblioteca por ID con acceso de lectura.
     *
     * @param id el ID de la biblioteca
     * @param usuario el usuario que accede
     * @return Optional con la biblioteca si tiene acceso
     */
    @Query("SELECT l FROM Library l LEFT JOIN l.editors e LEFT JOIN l.viewers v WHERE l.idLibrary = :id AND (l.usuario = :usuario OR :usuario MEMBER OF l.editors OR :usuario MEMBER OF l.viewers)")
    Optional<Library> findAccessibleById(@Param("id") Long id, @Param("usuario") Usuario usuario);

    /**
     * Busca una biblioteca por ID con acceso de edición.
     *
     * @param id el ID de la biblioteca
     * @param usuario el usuario que intenta editar
     * @return Optional con la biblioteca si tiene permisos
     */
    @Query("SELECT l FROM Library l LEFT JOIN l.editors e WHERE l.idLibrary = :id AND (l.usuario = :usuario OR :usuario MEMBER OF l.editors)")
    Optional<Library> findEditableById(@Param("id") Long id, @Param("usuario") Usuario usuario);

    /**
     * Busca una biblioteca por ID solo si es owned por el usuario.
     *
     * @param id el ID de la biblioteca
     * @param usuario el presunto propietario
     * @return Optional con la biblioteca si es propietario
     */
    @Query("SELECT l FROM Library l WHERE l.idLibrary = :id AND l.usuario = :usuario")
    Optional<Library> findOwnedById(@Param("id") Long id, @Param("usuario") Usuario usuario);
}
