package com.angelcantero.listme.util;

import com.angelcantero.listme.exception.ResourceNotFoundException;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene el usuario actualmente autenticado basado en el contexto de seguridad.
     *
     * @return el objeto Usuario del usuario autenticado
     * @throws ResourceNotFoundException si el usuario no se encuentra en la base de datos
     */
    public Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado en el contexto de seguridad"));
    }

    /**
     * Obtiene el nombre de usuario (username) del usuario actualmente autenticado.
     *
     * @return el nombre de usuario autenticado
     */
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}