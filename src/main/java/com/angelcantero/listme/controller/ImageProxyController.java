package com.angelcantero.listme.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Proxy de imágenes externas para evitar restricciones CORS en Flutter Web.
 * Solo permite dominios de confianza para evitar abuso.
 */
@RestController
@RequestMapping("/api/v1/proxy")
public class ImageProxyController {

    private static final Set<String> ALLOWED_DOMAINS = Set.of(
            "cdn.myanimelist.net",
            "myanimelist.net",
            "img.myanimelist.net"
    );

    private static final int CONNECT_TIMEOUT_MS = 5_000;
    private static final int READ_TIMEOUT_MS = 10_000;

    @GetMapping("/image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
        try {
            URL imageUrl = new URL(url);
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

            byte[] imageBytes = connection.getInputStream().readAllBytes();
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
