package com.angelcantero.listme.repository;

import com.angelcantero.listme.model.Invitation;
import com.angelcantero.listme.model.InvitationStatus;
import com.angelcantero.listme.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p><strong>InvitationRepository</strong></p>
 * <p>Repositorio para la entidad Invitation.</p>
 */
@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByReceiverAndStatus(Usuario receiver, InvitationStatus status);
    List<Invitation> findBySenderAndStatus(Usuario sender, InvitationStatus status);
}
