package org.example.examenparcial3.controller;

import org.example.examenparcial3.model.Department;
import org.example.examenparcial3.model.Employee;
import org.example.examenparcial3.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador MVC para gestionar las operaciones relacionadas con empleados.
 * Maneja peticiones HTTP para listado, búsqueda y visualización de detalles de empleados.
 *
 * Rutas base: /employees
 */
@Controller // Marca esta clase como un controlador Spring MVC
@RequestMapping("/employees") // Define la ruta base para todas las operaciones de empleados
public class EmployeeController {

    // Inyección de dependencias del servicio de empleados
    @Autowired
    private EmployeeService employeeService;

    /**
     * Maneja peticiones GET a /employees
     * Obtiene y muestra una lista paginada de todos los empleados
     *
     * Nota: Usa un tamaño de página mayor (50) comparado con departamentos,
     * sugiriendo que hay más empleados que departamentos en el sistema
     *
     * @param model Objeto para pasar datos a la vista
     * @param page Número de página (por defecto 0 - primera página)
     * @param size Tamaño de página (por defecto 50 empleados por página)
     * @return Nombre de la vista "employees" a renderizar
     */
    @GetMapping // Mapea peticiones GET a la ruta base /employees
    public String getAllEmployees(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "50") int size) {

        // Obtiene la página de empleados desde el servicio con paginación
        Page<Employee> employeePage = employeeService.getAllEmployeesPaginated(page, size);

        // Agrega los datos de paginación al modelo para la vista
        model.addAttribute("employees", employeePage.getContent()); // Lista actual de empleados
        model.addAttribute("currentPage", page); // Página actual para navegación
        model.addAttribute("totalPages", employeePage.getTotalPages()); // Total de páginas disponibles
        model.addAttribute("totalItems", employeePage.getTotalElements()); // Número total de empleados

        // Retorna el nombre de la vista Thymeleaf a renderizar
        return "employees";
    }

    /**
     * Maneja peticiones GET a /employees/{empNo}
     * Obtiene los detalles completos de un empleado específico incluyendo
     * su departamento actual y el historial de departamentos
     *
     * @param empNo Número del empleado (capturado de la URL como Integer)
     * @param model Objeto para pasar datos a la vista
     * @return Nombre de la vista "employee-detail" a renderizar
     */
    @GetMapping("/{empNo}") // Mapea peticiones GET con variable de ruta numérica
    public String getEmployeeDetails(@PathVariable Integer empNo, // Captura el ID del empleado de la URL
                                     Model model) {

        // Obtiene la información básica del empleado
        Employee employee = employeeService.getEmployeeById(empNo);

        // Obtiene el departamento actual del empleado
        Department department = employeeService.getCurrentDepartmentByEmployee(empNo);

        // Obtiene el historial completo de departamentos del empleado
        List<Department> allDepartments = employeeService.getAllDepartmentsByEmployee(empNo);

        // Agrega todos los datos relacionados al empleado al modelo
        model.addAttribute("employee", employee); // Información personal del empleado
        model.addAttribute("currentDepartment", department); // Departamento actual
        model.addAttribute("allDepartments", allDepartments); // Historial de departamentos

        // Retorna la vista de detalle del empleado
        return "employee-detail";
    }

    /**
     * Maneja peticiones GET a /employees/search
     * Realiza búsqueda de empleados por número de empleado
     *
     * Útil para funcionalidad de búsqueda rápida desde formularios
     *
     * @param empNo Número del empleado a buscar (parámetro de consulta requerido)
     * @param model Objeto para pasar datos a la vista
     * @return Nombre de la vista "employee-search" con resultados de búsqueda
     */
    @GetMapping("/search") // Mapea peticiones GET a la ruta de búsqueda
    public String searchEmployee(@RequestParam Integer empNo, // Captura el parámetro de búsqueda
                                 Model model) {

        // Realiza la búsqueda del empleado por número
        // Nota: Retorna una lista aunque probablemente solo haya un resultado
        List<Employee> employees = employeeService.searchEmployeesByEmpNo(empNo);

        // Agrega los resultados de búsqueda al modelo
        model.addAttribute("employees", employees); // Lista de empleados encontrados
        model.addAttribute("searchTerm", empNo); // Término de búsqueda para mostrar en la vista

        // Retorna la vista específica para mostrar resultados de búsqueda
        return "employee-search";
    }
}