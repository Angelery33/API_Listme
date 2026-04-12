package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import com.angelcantero.listme.dto.ChangePasswordRequest;
import com.angelcantero.listme.dto.LoginRequest;
import com.angelcantero.listme.dto.LoginResponse;
import com.angelcantero.listme.dto.RegisterRequest;
import com.angelcantero.listme.dto.TokenRefreshRequest;
import com.angelcantero.listme.exception.InvalidRefreshTokenException;
import com.angelcantero.listme.exception.ResourceNotFoundException;
import com.angelcantero.listme.model.RefreshToken;
import com.angelcantero.listme.model.Roles;
import com.angelcantero.listme.model.Usuario;
import com.angelcantero.listme.repository.UsuarioRepository;
import com.angelcantero.listme.service.JwtService;
import com.angelcantero.listme.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p><strong>AuthController</strong></p>
 * <p>Controlador para la autenticación y gestión de usuarios.</p>
 * <p>Endpoints para registro, login,刷新 tokens, cambio de contraseña y eliminación de cuenta.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@RestController
@RequestMapping(Config.API_URL + "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request datos de registro
     * @return mensaje de éxito o error
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: El correo electrónico ya está en uso");
        }

        Usuario user = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Roles.STANDARD)
                .build();

        usuarioRepository.save(user);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    /**
     * Autentica un usuario y retorna tokens JWT.
     *
     * @param request credenciales
     * @return tokens de acceso y refresh
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);
        
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario.getId());

        return ResponseEntity.ok(LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build());
    }

    /**
     * Refresca el token de acceso usando el refresh token.
     *
     * @param request包含 refresh token
     * @return nuevos tokens de acceso
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsuario)
                .map(usuario -> {
                    String token = jwtService.generateToken(userDetailsService.loadUserByUsername(usuario.getUsername()));
                    return ResponseEntity.ok(LoginResponse.builder()
                            .accessToken(token)
                            .refreshToken(request.getRefreshToken())
                            .build());
                })
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token no válido o no existe en la base de datos"));
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     *
     * @return datos del usuario
     */
    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualiza el perfil del usuario actual.
     *
     * @param updatedUser nuevos datos del usuario
     * @return usuario actualizado
     */
    @PutMapping("/profile")
    public ResponseEntity<Usuario> updateProfile(@RequestBody Usuario updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setUsername(updatedUser.getUsername());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuario);
    }

    /**
     * Cambia la contraseña del usuario actual.
     *
     * @param request contraseñas actual y nueva
     * @return mensaje de éxito
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), usuario.getPassword())) {
            return ResponseEntity.badRequest().body("Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    /**
     * Elimina la cuenta del usuario actual.
     *
     * @return mensaje de éxito
     */
    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);
        return ResponseEntity.ok("Cuenta eliminada correctamente");
    }
}