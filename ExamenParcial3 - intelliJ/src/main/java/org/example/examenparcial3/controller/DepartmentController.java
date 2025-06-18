package org.example.examenparcial3.controller;

import org.example.examenparcial3.model.Department;
import org.example.examenparcial3.model.Employee;
import org.example.examenparcial3.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador MVC para gestionar las operaciones relacionadas con departamentos.
 * Maneja las peticiones HTTP y coordina entre la vista y la lógica de negocio.
 *
 * Rutas base: /departments
 */
@Controller // Marca esta clase como un controlador Spring MVC
@RequestMapping("/departments") // Define la ruta base para todas las operaciones
public class DepartmentController {

    // Inyección de dependencias del servicio de departamentos
    @Autowired
    private DepartmentService departmentService;

    /**
     * Maneja peticiones GET a /departments
     * Obtiene y muestra una lista paginada de todos los departamentos
     *
     * @param model Objeto para pasar datos a la vista
     * @param page Número de página (por defecto 0)
     * @param size Tamaño de página (por defecto 10 elementos)
     * @return Nombre de la vista "departments" a renderizar
     */
    @GetMapping // Mapea peticiones GET a la ruta base /departments
    public String getAllDepartments(Model model,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {

        // Obtiene la página de departamentos desde el servicio
        Page<Department> departmentPage = departmentService.getAllDepartmentsPaginated(page, size);

        // Agrega los datos necesarios para la paginación al modelo
        model.addAttribute("departments", departmentPage.getContent()); // Lista de departamentos
        model.addAttribute("currentPage", page); // Página actual
        model.addAttribute("totalPages", departmentPage.getTotalPages()); // Total de páginas
        model.addAttribute("totalItems", departmentPage.getTotalElements()); // Total de elementos

        // Retorna el nombre de la vista Thymeleaf a renderizar
        return "departments";
    }

    /**
     * Maneja peticiones GET a /departments/{deptNo}
     * Obtiene los detalles de un departamento específico y sus empleados paginados
     *
     * @param deptNo Número del departamento (capturado de la URL)
     * @param page Número de página para empleados (por defecto 0)
     * @param size Tamaño de página para empleados (por defecto 10)
     * @param model Objeto para pasar datos a la vista
     * @return Nombre de la vista "department-detail" a renderizar
     */
    @GetMapping("/{deptNo}") // Mapea peticiones GET con variable de ruta
    public String getDepartmentDetails(@PathVariable String deptNo, // Captura el parámetro de la URL
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model) {

        // Obtiene la información del departamento específico
        Department department = departmentService.getDepartmentById(deptNo);

        // Obtiene los empleados del departamento de forma paginada
        Page<Employee> employeePage = departmentService.getEmployeesByDepartmentPaginated(deptNo, page, size);

        // Agrega todos los datos necesarios al modelo para la vista
        model.addAttribute("department", department); // Información del departamento
        model.addAttribute("employees", employeePage.getContent()); // Lista de empleados
        model.addAttribute("currentPage", page); // Página actual de empleados
        model.addAttribute("totalPages", employeePage.getTotalPages()); // Total de páginas de empleados
        model.addAttribute("totalItems", employeePage.getTotalElements()); // Total de empleados
        model.addAttribute("deptNo", deptNo); // Número del departamento para referencia

        // Retorna la vista de detalle del departamento
        return "department-detail";
    }
}