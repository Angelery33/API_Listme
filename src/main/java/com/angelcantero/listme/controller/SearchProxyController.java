package com.angelcantero.listme.controller;

import com.angelcantero.listme.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Proxy de búsqueda para APIs de medios externas que requieren clave de API.
 *
 * <p>Actúa como intermediario entre el cliente Flutter y TMDb / Google Books,
 * de modo que las claves de API nunca se distribuyen en los binarios de la app.
 * Las claves se leen de variables de entorno del servidor en el arranque.</p>
 *
 * <p>Todos los endpoints requieren autenticación JWT (heredada de la configuración
 * de seguridad global). Devuelven el JSON raw de la API externa para que el cliente
 * pueda aplicar su lógica de normalización existente sin cambios.</p>
 */
@RestController
@RequestMapping(Config.API_URL + "/search")
public class SearchProxyController {

    /** Clave de API de TMDb leída de la variable de entorno {@code LISTME_TMDB_KEY}. */
    @Value("${listme.api.tmdb-key:}")
    private String tmdbKey;

    /** Clave de API de Google Books leída de la variable de entorno {@code LISTME_BOOKS_KEY}. */
    @Value("${listme.api.books-key:}")
    private String booksKey;

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    /**
     * Busca películas o series de TV en TMDb.
     *
     * @param query    Texto de búsqueda.
     * @param page     Número de página (base 1).
     * @param type     {@code "movie"} o {@code "tv"}.
     * @param language Código de idioma BCP-47 (por defecto {@code "es-ES"}).
     * @return JSON raw de TMDb o 503 si la clave no está configurada.
     */
    @GetMapping("/tmdb")
    public ResponseEntity<String> searchTMDb(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "movie") String type,
            @RequestParam(defaultValue = "es-ES") String language
    ) {
        if (tmdbKey.isBlank()) {
            return ResponseEntity.status(503).body("{\"error\":\"TMDb no configurado en el servidor\"}");
        }

        final String endpoint = type.equals("tv") ? "search/tv" : "search/movie";
        final String url = "https://api.themoviedb.org/3/" + endpoint
                + "?query=" + URI.create(query).toASCIIString()
                + "&page=" + page
                + "&language=" + language
                + "&api_key=" + tmdbKey;

        return forwardGet(url);
    }

    /**
     * Obtiene detalles completos de una película o serie de TV en TMDb.
     *
     * @param id       Identificador numérico de TMDb.
     * @param type     {@code "movie"} o {@code "tv"}.
     * @param language Código de idioma BCP-47 (por defecto {@code "es-ES"}).
     * @return JSON raw de TMDb o 503 si la clave no está configurada.
     */
    @GetMapping("/tmdb/details")
    public ResponseEntity<String> getTMDbDetails(
            @RequestParam String id,
            @RequestParam(defaultValue = "movie") String type,
            @RequestParam(defaultValue = "es-ES") String language
    ) {
        if (tmdbKey.isBlank()) {
            return ResponseEntity.status(503).body("{\"error\":\"TMDb no configurado en el servidor\"}");
        }

        final String endpoint = type.equals("tv") ? "tv" : "movie";
        final String url = "https://api.themoviedb.org/3/" + endpoint + "/" + id
                + "?api_key=" + tmdbKey
                + "&language=" + language
                + "&append_to_response=credits,images";

        return forwardGet(url);
    }

    /**
     * Busca volúmenes en Google Books.
     *
     * @param query      Cadena de búsqueda (título, autor, ISBN, etc.).
     * @param startIndex Índice de inicio (paginación), base 0.
     * @param maxResults Número máximo de resultados (máx. 40).
     * @param printType  Filtro de tipo ({@code "books"}, {@code "magazines"}, etc.).
     * @return JSON raw de Google Books o 503 si la clave no está configurada.
     */
    @GetMapping("/books")
    public ResponseEntity<String> searchBooks(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int startIndex,
            @RequestParam(defaultValue = "15") int maxResults,
            @RequestParam(defaultValue = "books") String printType
    ) {
        if (booksKey.isBlank()) {
            return ResponseEntity.status(503).body("{\"error\":\"Google Books no configurado en el servidor\"}");
        }

        final String url = "https://www.googleapis.com/books/v1/volumes"
                + "?q=" + URI.create(query).toASCIIString()
                + "&startIndex=" + startIndex
                + "&maxResults=" + maxResults
                + "&printType=" + printType
                + "&key=" + booksKey;

        return forwardGet(url);
    }

    /**
     * Realiza una petición GET al {@code url} externo y retransmite la respuesta al cliente.
     *
     * @param url URL absoluta del servicio externo.
     * @return Respuesta con el cuerpo y el código de estado originales,
     *         o 502 si se produce un error de red.
     */
    private ResponseEntity<String> forwardGet(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(TIMEOUT)
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(
                    request, HttpResponse.BodyHandlers.ofString());

            return ResponseEntity
                    .status(response.statusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.body());

        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body("{\"error\":\"Error al contactar servicio externo\"}");
        }
    }
}
