package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.Item;
import com.angelcantero.listme.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p><strong>ItemRepository</strong></p>
 * <p>Repositorio para la entidad Item.</p>
 * <p>Proporciona métodos de acceso a datos para ítems de bibliotecas.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    /**
     * Busca todos los ítems de una biblioteca por su ID.
     *
     * @param libraryId el ID de la biblioteca
     * @return lista de ítems
     */
    List<Item> findByLibraryIdLibrary(Long libraryId);

    /**
     * Busca todos los ítems accesibles por un usuario.
     * Incluye ítems de sus bibliotecas propias y las compartidas con él.
     *
     * @param usuario el usuario que accede
     * @return lista de ítems accesibles
     */
    @Query("SELECT DISTINCT i FROM Item i LEFT JOIN i.library l LEFT JOIN l.editors e LEFT JOIN l.viewers v WHERE l.usuario = :usuario OR :usuario MEMBER OF l.editors OR :usuario MEMBER OF l.viewers")
    List<Item> findAllAccessibleByUser(@Param("usuario") Usuario usuario);
}
