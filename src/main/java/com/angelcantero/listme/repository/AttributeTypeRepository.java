package com.angelcantero.listme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.angelcantero.listme.model.AttributeType;

/**
 * <p><strong>AttributeTypeRepository</strong></p>
 * <p>Repositorio para la entidad AttributeType.</p>
 * <p>Proporciona métodos de acceso a datos para tipos de atributos.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Repository
public interface AttributeTypeRepository extends JpaRepository<AttributeType, Long> {
}
