package com.angelcantero.listme.repository;

import com.angelcantero.listme.model.Friendship;
import com.angelcantero.listme.model.FriendshipStatus;
import com.angelcantero.listme.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link Friendship}.
 *
 * <p>Proporciona consultas para gestionar solicitudes de amistad y recuperar la lista
 * de amigos confirmados de un usuario.</p>
 *
 * @author Angel Cantero
 * @since 1.5.0
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    /**
     * Busca todas las solicitudes recibidas por {@code receiver} con el estado indicado.
     *
     * @param receiver el usuario destinatario.
     * @param status   el estado a filtrar ({@code PENDING}, {@code ACCEPTED} o {@code REJECTED}).
     * @return lista de solicitudes que coincidan.
     */
    List<Friendship> findByReceiverAndStatus(Usuario receiver, FriendshipStatus status);

    /**
     * Devuelve todas las amistades confirmadas ({@link FriendshipStatus#ACCEPTED}) en las que
     * participa {@code user}, ya sea como remitente o como destinatario.
     *
     * @param user el usuario cuyas amistades se quieren recuperar.
     * @return lista de amistades aceptadas del usuario.
     */
    @Query("SELECT f FROM Friendship f WHERE f.status = 'ACCEPTED' AND (f.sender = :user OR f.receiver = :user)")
    List<Friendship> findAcceptedFriendships(@Param("user") Usuario user);

    /**
     * Busca la solicitud de amistad entre dos usuarios, independientemente de quién la inició.
     *
     * @param userA primer usuario.
     * @param userB segundo usuario.
     * @return {@link Optional} con la solicitud si existe.
     */
    @Query("SELECT f FROM Friendship f WHERE (f.sender = :a AND f.receiver = :b) OR (f.sender = :b AND f.receiver = :a)")
    Optional<Friendship> findBetween(@Param("a") Usuario userA, @Param("b") Usuario userB);

    /**
     * Comprueba si ya existe alguna solicitud entre dos usuarios (en cualquier dirección y estado).
     *
     * @param userA primer usuario.
     * @param userB segundo usuario.
     * @return {@code true} si existe al menos una solicitud entre ambos usuarios.
     */
    @Query("SELECT COUNT(f) > 0 FROM Friendship f WHERE (f.sender = :a AND f.receiver = :b) OR (f.sender = :b AND f.receiver = :a)")
    boolean existsBetween(@Param("a") Usuario userA, @Param("b") Usuario userB);
}
