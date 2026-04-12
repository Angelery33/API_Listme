package com.angelcantero.listme.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p><strong>JwtService</strong></p>
 * <p>Servicio para la gestión de tokens JWT.</p>
 * <p>Proporciona métodos para generar, validar y extraer información
 * de tokens JWT utilizados en la autenticación de la API.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@Service
public class JwtService {

    /**
     * Clave secreta para firmar los tokens JWT.
     */
    @Value("${application.security.jwt.secret-key:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    /**
     * Tiempo de expiración del token en milisegundos (por defecto 1 día).
     */
    @Value("${application.security.jwt.expiration:86400000}") // 1 day
    private long jwtExpiration;

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token el token JWT
     * @return el nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim específico del token JWT.
     *
     * @param token el token JWT
     * @param claimsResolver función para extraer el claim deseado
     * @return el valor del claim extraído
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token JWT para el usuario sin claims adicionales.
     *
     * @param userDetails los detalles del usuario
     * @return el token JWT generado
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con claims adicionales.
     *
     * @param extraClaims claims adicionales a incluir en el token
     * @param userDetails los detalles del usuario
     * @return el token JWT generado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Obtiene el tiempo de expiración configurado.
     *
     * @return tiempo de expiración en milisegundos
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Construye el token JWT con los parámetros dados.
     *
     * @param extraClaims claims adicionales
     * @param userDetails detalles del usuario
     * @param expiration tiempo de expiración
     * @return token JWT firmado
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Valida que el token sea válido para el usuario dado.
     *
     * @param token el token JWT
     * @param userDetails los detalles del usuario
     * @return true si el token es válido
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null && Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Verifica si el token ha expirado.
     *
     * @param token el token JWT
     * @return true si el token ha expirado
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token.
     *
     * @param token el token JWT
     * @return fecha de expiración
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todos los claims del token.
     *
     * @param token el token JWT
     * @return claims del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtiene la clave de firma para los tokens.
     *
     * @return clave secreta
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
