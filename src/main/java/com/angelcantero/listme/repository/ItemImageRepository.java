package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.ItemImage;

import java.util.List;

/**
 * <p><strong>ItemImageRepository</strong></p>
 * <p>Repositorio para la entidad ItemImage.</p>
 * <p>Proporciona métodos de acceso a datos para imágenes de ítems.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    
    /**
     * Busca todas las imágenes de un ítem.
     *
     * @param itemId el ID del ítem
     * @return lista de imágenes
     */
    List<ItemImage> findByItemIdItem(Long itemId);
}
