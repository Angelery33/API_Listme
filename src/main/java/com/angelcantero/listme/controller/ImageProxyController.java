package com.angelcantero.listme.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Controlador proxy para imágenes externas.
 *
 * <p>Actúa como intermediario entre el cliente Flutter Web y los servidores de imágenes
 * externos, resolviendo las restricciones CORS que impiden cargar imágenes directamente
 * desde el navegador. Solo se permiten dominios de confianza predefinidos para evitar
 * un uso abusivo del proxy.</p>
 */
@RestController
@RequestMapping("/api/v1/proxy")
public class ImageProxyController {

    /**
     * Conjunto de dominios externos autorizados para ser proxificados.
     * Cualquier URL cuyo host no pertenezca a esta lista será rechazada con HTTP 403.
     */
    private static final Set<String> ALLOWED_DOMAINS = Set.of(
            "cdn.myanimelist.net",
            "myanimelist.net",
            "img.myanimelist.net"
    );

    /** Tiempo máximo de espera para establecer la conexión con el servidor externo, en milisegundos. */
    private static final int CONNECT_TIMEOUT_MS = 5_000;

    /** Tiempo máximo de espera para leer la respuesta del servidor externo, en milisegundos. */
    private static final int READ_TIMEOUT_MS = 10_000;

    /** Límite de 5 MB para imágenes proxificadas. */
    private static final int MAX_IMAGE_BYTES = 5 * 1024 * 1024;

    /**
     * Descarga y retransmite una imagen externa al cliente.
     *
     * <p>Valida que la URL utilice un esquema HTTP/HTTPS y que el dominio esté en la lista
     * de permitidos antes de realizar la descarga. La respuesta incluye cabeceras de caché
     * de una hora para reducir peticiones repetidas.</p>
     *
     * @param url URL absoluta de la imagen externa que se desea proxificar.
     * @return {@link ResponseEntity} con los bytes de la imagen y el tipo de contenido
     *         correspondiente, o un código de error si la URL no es válida (403),
     *         la imagen es demasiado grande (413) o el servidor externo falla (502).
     */
    @GetMapping("/image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
        try {
            URL imageUrl = URI.create(url).toURL();

            // Solo se permiten esquemas HTTP/HTTPS (evita file://, jar://, etc.)
            String protocol = imageUrl.getProtocol().toLowerCase();
            if (!protocol.equals("http") && !protocol.equals("https")) {
                return ResponseEntity.status(403).build();
            }

            String host = imageUrl.getHost().toLowerCase();

            boolean allowed = ALLOWED_DOMAINS.stream()
                    .anyMatch(domain -> host.equals(domain) || host.endsWith("." + domain));

            if (!allowed) {
                return ResponseEntity.status(403).build();
            }

            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; ListMe/1.0)");
            connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
            connection.setReadTimeout(READ_TIMEOUT_MS);
            connection.setInstanceFollowRedirects(true);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return ResponseEntity.status(responseCode).build();
            }

            // Rechazar si Content-Length supera el límite antes de leer
            int contentLength = connection.getContentLength();
            if (contentLength > MAX_IMAGE_BYTES) {
                return ResponseEntity.status(413).build();
            }

            // Leer con límite estricto para evitar descargas masivas
            byte[] imageBytes;
            try (var is = connection.getInputStream()) {
                imageBytes = is.readNBytes(MAX_IMAGE_BYTES);
            }

            String contentType = connection.getContentType();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            contentType != null ? contentType : "image/jpeg"))
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                    .body(imageBytes);

        } catch (Exception e) {
            return ResponseEntity.status(502).build();
        }
    }
}
