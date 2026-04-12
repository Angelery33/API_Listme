package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.AttributeItem;

import java.util.List;

/**
 * <p><strong>AttributeItemRepository</strong></p>
 * <p>Repositorio para la entidad AttributeItem.</p>
 * <p>Proporciona métodos de acceso a datos para atributos de ítems.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface AttributeItemRepository extends JpaRepository<AttributeItem, Long> {
    
    /**
     * Busca todos los atributos de un ítem.
     *
     * @param itemId el ID del ítem
     * @return lista de atributos
     */
    List<AttributeItem> findByItemIdItem(Long itemId);
}
