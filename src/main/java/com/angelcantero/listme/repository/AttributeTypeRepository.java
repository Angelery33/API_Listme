package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.AttributeType;

@Repository
public interface AttributeTypeRepository extends JpaRepository<AttributeType, Long> {
}
