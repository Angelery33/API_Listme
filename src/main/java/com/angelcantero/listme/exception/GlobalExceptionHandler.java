package com.angelcantero.listme.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.angelcantero.listme.dto.ErrorResponseDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST.
 *
 * <p>Centraliza el tratamiento de errores de toda la aplicación mediante
 * {@code @RestControllerAdvice}, traduciendo las excepciones más comunes en
 * respuestas HTTP con el código de estado y el mensaje adecuados, usando
 * {@link com.angelcantero.listme.dto.ErrorResponseDTO} como cuerpo de respuesta.</p>
 *
 * @author Angel Cantero
 * @version 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja los errores de validación de argumentos de entrada.
     *
     * <p>Se activa cuando un parámetro anotado con {@code @Valid} no supera
     * las restricciones declaradas (Bean Validation). Devuelve un mapa con
     * el nombre de cada campo inválido y el mensaje de error correspondiente.</p>
     *
     * @param ex excepción lanzada por Spring al detectar errores de validación.
     * @return {@link ResponseEntity} con estado HTTP 400 y el detalle de los campos fallidos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                System.currentTimeMillis(),
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Maneja violaciones de integridad de datos en la base de datos.
     *
     * <p>Se activa ante duplicados en campos únicos, referencias nulas a columnas
     * obligatorias u otras restricciones a nivel de base de datos.</p>
     *
     * @param ex excepción lanzada por Spring Data al detectar una violación de integridad.
     * @return {@link ResponseEntity} con estado HTTP 409 y un mensaje descriptivo del conflicto.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        String cause = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String message = "Error de integridad: Ya existe un registro con esos datos o faltan campos obligatorios. Detalles: " + cause;
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                message,
                System.currentTimeMillis(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Maneja el caso en que un recurso solicitado no existe en el sistema.
     *
     * @param ex excepción lanzada por los servicios cuando no se encuentra la entidad buscada.
     * @return {@link ResponseEntity} con estado HTTP 404 y el mensaje de la excepción.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja el intento de autenticación con credenciales incorrectas.
     *
     * @param ex excepción lanzada por Spring Security cuando el usuario o la contraseña no coinciden.
     * @return {@link ResponseEntity} con estado HTTP 401 y un mensaje informativo.
     */
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Credenciales incorrectas: El usuario o la contraseña no coinciden.",
                System.currentTimeMillis(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Manejador de último recurso para excepciones de tiempo de ejecución no contempladas.
     *
     * <p>Captura cualquier {@link RuntimeException} que no haya sido gestionada por
     * un manejador más específico, evitando que el stack trace llegue al cliente.</p>
     *
     * @param ex excepción de tiempo de ejecución no controlada.
     * @return {@link ResponseEntity} con estado HTTP 500 y el mensaje de la excepción.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntime(RuntimeException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor: " + ex.getMessage(),
                System.currentTimeMillis(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
