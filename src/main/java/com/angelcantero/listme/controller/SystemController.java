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

@RestController
@RequestMapping(Config.API_URL + "/system")
@RequiredArgsConstructor
public class SystemController {

    private final LibraryRepository libraryRepository;
    private final ItemRepository itemRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/version")
    public ResponseEntity<Map<String, String>> getVersion() {
        Map<String, String> response = new HashMap<>();
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<UserStatsDTO> getStats() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario user = usuarioRepository.findByUsername(username).orElseThrow();
        
        long libraries = libraryRepository.countByUsuario(user);
        long items = itemRepository.countByLibraryUsuario(user);
        
        return ResponseEntity.ok(new UserStatsDTO(libraries, items));
    }
}
