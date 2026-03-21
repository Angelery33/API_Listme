package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.AttributeItem;

import java.util.List;

@Repository
public interface AttributeItemRepository extends JpaRepository<AttributeItem, Long> {
    List<AttributeItem> findByItemIdItem(Long itemId);
}
