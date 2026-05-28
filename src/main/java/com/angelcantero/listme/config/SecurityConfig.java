package com.angelcantero.listme.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

/**
 * <p><strong>SecurityConfig</strong></p>
 * <p>Configuración de seguridad de la aplicación.</p>
 * <p>Define la cadena de filtros, proveedores de autenticación y políticas CORS.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    /**
     * Orígenes CORS permitidos. Configurable vía variable de entorno {@code LISTME_CORS_ORIGINS}
     * (valores separados por coma). En desarrollo se incluyen los puertos habituales de Flutter Web
     * (5000, 5001, 7357, 8080, 8081, 3000). En producción se sobreescribe con solo el dominio publicado.
     */
    @Value("${listme.cors.allowed-origins:" +
            "https://app.angelcantero.store," +
            "http://localhost:*," +
            "http://127.0.0.1:*}")
    private String allowedOriginsRaw;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers(Config.API_URL + "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, Config.API_URL + "/proxy/image").permitAll()

                        // Roles
                        .requestMatchers(HttpMethod.GET, Config.API_URL + "/**").hasAnyRole("STANDARD", "ADMIN")
                        .requestMatchers(HttpMethod.POST, Config.API_URL + "/**").hasAnyRole("STANDARD", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, Config.API_URL + "/**").hasAnyRole("STANDARD", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, Config.API_URL + "/**").hasAnyRole("STANDARD", "ADMIN")

                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Orígenes exactos leídos de variable de entorno LISTME_CORS_ORIGINS.
        // En desarrollo se admiten localhost; en producción solo el dominio publicado.
        // CSRF está deshabilitado porque la API usa JWT en headers, no cookies.
        List<String> origins = Arrays.asList(allowedOriginsRaw.split(","));
        // setAllowedOriginPatterns permite wildcards (e.g. http://localhost:*)
        // necesario para el puerto aleatorio que asigna Flutter web en debug.
        // En producción la variable de entorno solo contendrá el dominio exacto.
        configuration.setAllowedOriginPatterns(origins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of("Authorization", "X-Token-Expired"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
