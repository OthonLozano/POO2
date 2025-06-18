package org.example.examenparcial3.service;

import org.example.examenparcial3.model.Department;
import org.example.examenparcial3.model.Employee;
import org.example.examenparcial3.repository.DepartmentRepository;
import org.example.examenparcial3.repository.DeptEmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para la gestión de departamentos.
 *
 * Esta clase implementa la capa de servicio del patrón de arquitectura en capas,
 * encapsulando la lógica de negocio relacionada con departamentos y coordinando
 * entre múltiples repositorios para operaciones complejas.
 *
 * Características principales:
 * - Gestión completa de departamentos (consulta, listado, relaciones)
 * - Coordinación entre DepartmentRepository y DeptEmpRepository
 * - Implementación de paginación optimizada con ordenamiento
 * - Uso de Java Streams para transformaciones de datos
 * - Manejo de relaciones many-to-many a través de tabla de asociación
 */
@Service // Marca esta clase como un componente de servicio de Spring
public class DepartmentService {

    // Inyección de dependencias para acceso a datos de departamentos
    @Autowired
    private DepartmentRepository departmentRepository;

    // Inyección de dependencias para acceso a la tabla de asociación
    @Autowired
    private DeptEmpRepository deptEmpRepository;

    /**
     * Obtiene todos los departamentos con paginación y ordenamiento.
     *
     * Características del método:
     * - Implementa paginación para manejar grandes volúmenes de departamentos
     * - Aplica ordenamiento ascendente por número de departamento (deptNo)
     * - Retorna objeto Page con metadatos de paginación
     * - Delega la operación al repositorio con configuración específica
     *
     * Lógica de ordenamiento:
     * - Sort.by("deptNo").ascending(): Ordena alfabéticamente por código de departamento
     * - Garantiza presentación consistente (ej: "HR", "IT", "SALES")
     * - Facilita navegación predecible para usuarios
     *
     * @param page Número de página (0-based)
     * @param size Cantidad de departamentos por página
     * @return Page<Department> Página de departamentos con metadatos
     */
    public Page<Department> getAllDepartmentsPaginated(int page, int size) {
        return departmentRepository.findAll(
                PageRequest.of(page, size, Sort.by("deptNo").ascending())
        );
    }

    /**
     * Obtiene un departamento específico por su número identificador.
     *
     * Características del método:
     * - Operación simple de consulta por clave de negocio
     * - Delega directamente al repositorio sin lógica adicional
     * - Retorna el departamento o null si no existe
     * - Método base para operaciones de detalle de departamento
     *
     * @param deptNo Número único del departamento a buscar
     * @return Department Departamento encontrado o null
     */
    public Department getDepartmentById(String deptNo) {
        return departmentRepository.findByDeptNo(deptNo);
    }

    /**
     * Obtiene todos los empleados asignados a un departamento específico.
     *
     * LÓGICA DE NEGOCIO COMPLEJA:
     * Este método implementa una operación que requiere coordinación entre
     * múltiples repositorios y transformación de datos usando Java Streams.
     *
     * Flujo de ejecución:
     * 1. Consulta DeptEmpRepository para obtener asignaciones del departamento
     * 2. Usa Stream API para transformar DeptEmp -> Employee
     * 3. Aplica map() para extraer el objeto Employee de cada asignación
     * 4. Colecta los resultados en una List<Employee>
     *
     * Ventajas del enfoque con Streams:
     * - Código funcional y declarativo
     * - Lazy evaluation para mejor rendimiento
     * - Fácil mantenimiento y lectura
     * - Composición de operaciones complejas
     *
     * Consideraciones:
     * - Incluye empleados actuales E históricos del departamento
     * - No filtra por fechas de vigencia (from_date/to_date)
     * - Retorna lista completa sin paginación
     *
     * @param deptNo Número del departamento para obtener empleados
     * @return List<Employee> Lista de todos los empleados del departamento
     */
    public List<Employee> getEmployeesByDepartment(String deptNo) {
        // Usando Streams para transformar y filtrar a través de la tabla DeptEmp
        return deptEmpRepository.findByIdDeptNo(deptNo).stream()
                .map(deptEmp -> deptEmp.getEmployee()) // Transformación DeptEmp -> Employee
                .collect(Collectors.toList()); // Recolección en lista inmutable
    }

    /**
     * Obtiene empleados de un departamento con paginación y ordenamiento.
     *
     * OPERACIÓN AVANZADA CON PAGINACIÓN:
     * Este método combina la lógica de relaciones many-to-many con paginación
     * optimizada, transformando los datos en el proceso.
     *
     * Flujo de ejecución:
     * 1. Crea Pageable con ordenamiento por número de empleado
     * 2. Consulta DeptEmpRepository con paginación aplicada
     * 3. Usa map() en el Page para transformar DeptEmp -> Employee
     * 4. Retorna Page<Employee> manteniendo metadatos de paginación
     *
     * Optimizaciones implementadas:
     * - Sort.by("id.empNo").ascending(): Ordena por número de empleado
     * - Paginación aplicada a nivel de base de datos (eficiente)
     * - Transformación lazy con Page.map() (no carga todo en memoria)
     * - Mantiene metadatos de paginación intactos
     *
     * Ventajas del ordenamiento por id.empNo:
     * - Orden numérico predecible y lógico
     * - Facilita búsqueda manual en listas largas
     * - Consistencia con identificadores de negocio
     *
     * @param deptNo Número del departamento
     * @param page Número de página (0-based)
     * @param size Cantidad de empleados por página
     * @return Page<Employee> Página de empleados con metadatos de paginación
     */
    public Page<Employee> getEmployeesByDepartmentPaginated(String deptNo, int page, int size) {
        // Configuración de paginación con ordenamiento por número de empleado
        Pageable pageable = PageRequest.of(page, size, Sort.by("id.empNo").ascending());

        // Consulta paginada y transformación de DeptEmp a Employee
        return deptEmpRepository.findByIdDeptNo(deptNo, pageable)
                .map(deptEmp -> deptEmp.getEmployee()); // Transformación lazy por página
    }
}