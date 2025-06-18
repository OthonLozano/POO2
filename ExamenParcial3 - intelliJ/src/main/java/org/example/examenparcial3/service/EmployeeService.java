package org.example.examenparcial3.service;

import org.example.examenparcial3.model.Department;
import org.example.examenparcial3.model.DeptEmp;
import org.example.examenparcial3.model.Employee;
import org.example.examenparcial3.repository.DeptEmpRepository;
import org.example.examenparcial3.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para la gestión de empleados.
 *
 * Esta clase implementa la capa de servicio para operaciones relacionadas con
 * empleados, coordinando entre múltiples repositorios para proporcionar
 * funcionalidades complejas que incluyen historial departamental y búsquedas.
 *
 * Características principales:
 * - Gestión completa de empleados (consulta, listado, búsqueda)
 * - Coordinación entre EmployeeRepository y DeptEmpRepository
 * - Manejo de historial laboral y asignaciones departamentales
 * - Uso de Java Streams para transformaciones y filtros
 * - Implementación de paginación para listados eficientes
 */
@Service // Marca esta clase como un componente de servicio de Spring
public class EmployeeService {

    // Inyección de dependencias para acceso a datos de empleados
    @Autowired
    private EmployeeRepository employeeRepository;

    // Inyección de dependencias para acceso a la tabla de asociación
    @Autowired
    private DeptEmpRepository deptEmpRepository;

    /**
     * Obtiene todos los empleados con paginación.
     *
     * Características del método:
     * - Implementa paginación básica sin ordenamiento específico
     * - Delega directamente al repositorio con configuración simple
     * - Usa tamaño de página configurado externamente (típicamente 50)
     * - Retorna objeto Page con metadatos de paginación
     *
     * @param page Número de página (0-based)
     * @param size Cantidad de empleados por página
     * @return Page<Employee> Página de empleados con metadatos
     */
    public Page<Employee> getAllEmployeesPaginated(int page, int size) {
        return employeeRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Obtiene un empleado específico por su número identificador.
     *
     * Características del método:
     * - Operación simple de consulta por clave de negocio
     * - Delega directamente al repositorio sin lógica adicional
     * - Retorna el empleado o null si no existe
     * - Método base para operaciones de detalle de empleado
     *
     * @param empNo Número único del empleado a buscar
     * @return Employee Empleado encontrado o null
     */
    public Employee getEmployeeById(Integer empNo) {
        return employeeRepository.findByEmpNo(empNo);
    }

    /**
     * Busca empleados por número de empleado usando filtrado con Streams.
     *
     *
     * @param empNo Número del empleado a buscar
     * @return List<Employee> Lista con empleados encontrados (normalmente 1 o 0)
     */
    public List<Employee> searchEmployeesByEmpNo(Integer empNo) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getEmpNo().equals(empNo))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el departamento actual de un empleado específico.
     *
     * LÓGICA DE NEGOCIO IMPORTANTE:
     * Este método implementa la lógica para determinar el departamento "actual"
     * de un empleado, considerando que puede tener múltiples asignaciones.
     *
     * @param empNo Número del empleado
     * @return Department Departamento actual del empleado o null si no tiene
     */
    public Department getCurrentDepartmentByEmployee(Integer empNo) {
        List<DeptEmp> deptEmps = deptEmpRepository.findByIdEmpNo(empNo);
        if (!deptEmps.isEmpty()) {
            return deptEmps.get(0).getDepartment();
        }
        return null;
    }

    /**
     * Obtiene el historial completo de departamentos donde ha trabajado un empleado.
     *
     * FUNCIONALIDAD DE HISTORIAL LABORAL:
     * Este método proporciona una vista completa de la carrera del empleado,
     * mostrando todos los departamentos por los que ha pasado.
     *
     *
     * @param empNo Número del empleado
     * @return List<Department> Lista de todos los departamentos del empleado
     */
    public List<Department> getAllDepartmentsByEmployee(Integer empNo) {
        List<DeptEmp> deptEmps = deptEmpRepository.findByIdEmpNo(empNo);
        return deptEmps.stream()
                .map(DeptEmp::getDepartment)
                .collect(Collectors.toList());
    }

}