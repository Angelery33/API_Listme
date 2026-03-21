package com.angelcantero.listme.config;

import com.angelcantero.listme.model.Roles;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BootstrapUserConfig {

    private static final Logger log = LoggerFactory.getLogger(BootstrapUserConfig.class);

    @Bean
    CommandLineRunner bootstrapAdminUser(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("angel") String username,
            @Value("1234") String password
    ) {
        return args -> {
            if (username == null || username.isBlank() || password == null || password.isBlank()) {
                // No bootstrap credentials provided; do nothing.
                return;
            }

            if (usuarioRepository.findByUsername(username).isPresent()) {
                log.info("Bootstrap user '{}' already exists; skipping creation.", username);
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setEmail("admin@listme.com");
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRol(Roles.ADMIN);

            usuarioRepository.save(usuario);
            log.info("Bootstrap ADMIN user '{}' created.", username);
        };
    }
}

