package org.example.examenparcial3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal de la aplicación web.
 * Maneja la página de inicio y actúa como punto de entrada principal
 * para los usuarios que acceden a la aplicación.
 *
 * Este controlador implementa un patrón simple y común en aplicaciones web
 * donde se necesita una página de bienvenida o dashboard principal.
 */
@Controller // Marca esta clase como un controlador Spring MVC
public class HomeController {

    /**
     * Maneja peticiones GET a la ruta raíz de la aplicación "/"
     *
     * Este método actúa como el punto de entrada principal de la aplicación,
     * dirigiendo a los usuarios a la página de inicio cuando acceden al dominio base.
     *
     * Características:
     * - No requiere parámetros ni modelo de datos
     * - Implementación minimalista y eficiente
     * - Ideal para páginas de bienvenida, dashboard o menús principales
     *
     * @return "index" - Nombre de la vista Thymeleaf a renderizar (index.html)
     */
    @GetMapping("/") // Mapea peticiones GET a la ruta raíz de la aplicación
    public String home() {
        // Retorna el nombre de la plantilla de la página principal
        // Spring Boot buscará el archivo "index.html" en src/main/resources/templates/
        return "index";
    }
}