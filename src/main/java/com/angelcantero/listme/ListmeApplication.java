package com.angelcantero.listme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p><strong>ListmeApplication</strong></p>
 * <p>Punto de entrada principal de la aplicación ListMe API.</p>
 * <p>ListMe es una API REST para la gestión de listas y colecciones personalizables
 * que permite a los usuarios crear bibliotecas, administrar ítems, compartir colecciones
 * y gestionar atributos personalizados.</p>
 *
 * @author Angel Cantero
 * @since 1.0.0
 */
@SpringBootApplication
public class ListmeApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(ListmeApplication.class, args);
    }

}
//