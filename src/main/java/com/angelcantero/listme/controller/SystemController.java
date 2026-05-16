package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.UserStatsDTO;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.ItemRepository;
import com.angelcantero.listme.repository.LibraryRepository;
import com.angelcantero.listme.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para información general del sistema.
 *
 * <p>Proporciona endpoints de utilidad que no pertenecen a ningún dominio funcional
 * específico, como la versión de la API y las estadísticas agregadas del usuario
 * autenticado.</p>
 */
@RestController
@RequestMapping(Config.API_URL + "/system")
@RequiredArgsConstructor
public class SystemController {

    /** Repositorio para consultar y contar las bibliotecas del usuario. */
    private final LibraryRepository libraryRepository;

    /** Repositorio para consultar y contar los ítems del usuario. */
    private final ItemRepository itemRepository;

    /** Repositorio para recuperar la entidad del usuario autenticado. */
    private final UsuarioRepository usuarioRepository;

    /**
     * Devuelve la versión actual de la API.
     *
     * @return {@link ResponseEntity} con un mapa que contiene la clave {@code "version"}
     *         y su valor correspondiente.
     */
    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getVersion() {
        Map<String, String> response = new HashMap<>();
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * Devuelve estadísticas de uso agregadas del usuario autenticado.
     *
     * <p>Incluye el número total de bibliotecas y de ítems que pertenecen
     * al usuario que realiza la petición.</p>
     *
     * @return {@link ResponseEntity} con un {@link UserStatsDTO} que contiene
     *         el recuento de bibliotecas e ítems del usuario.
     */
    @GetMapping("/stats")
    public ResponseEntity<UserStatsDTO> getStats() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario user = usuarioRepository.findByUsername(username).orElseThrow();

        long libraries = libraryRepository.countByUsuario(user);
        long items = itemRepository.countByLibraryUsuario(user);

        return ResponseEntity.ok(new UserStatsDTO(libraries, items));
    }
}
