package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.Item;
import com.angelcantero.listme.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByLibraryIdLibrary(Long libraryId);

    @Query("SELECT DISTINCT i FROM Item i LEFT JOIN i.library l LEFT JOIN l.editors e LEFT JOIN l.viewers v WHERE l.usuario = :usuario OR :usuario MEMBER OF l.editors OR :usuario MEMBER OF l.viewers")
    List<Item> findAllAccessibleByUser(@Param("usuario") Usuario usuario);
}
