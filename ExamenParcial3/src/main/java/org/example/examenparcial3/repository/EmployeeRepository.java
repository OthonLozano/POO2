package org.example.examenparcial3.repository;

import org.example.examenparcial3.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de datos para la entidad Employee.
 *
 * Esta interfaz proporciona operaciones de acceso a datos para empleados,
 * implementando el patrón Repository con Spring Data JPA. Maneja la persistencia
 * y consultas de la entidad Employee de manera declarativa y automática.
 *
 * Características principales:
 * - Hereda operaciones CRUD completas de JpaRepository
 * - Soporte nativo para paginación de empleados
 * - Métodos de consulta personalizados basados en convenciones de naming
 * - Manejo automático de transacciones y traducción de excepciones
 */
@Repository // Marca esta interfaz como un componente de repositorio Spring
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Obtiene todos los empleados con soporte de paginación.
     *
     * Características del método:
     * - Sobrescribe explícitamente el método heredado de JpaRepository
     * - Técnicamente redundante, pero útil para documentación clara
     * - Garantiza soporte explícito de paginación para listas de empleados
     *
     * Justificación de la declaración explícita:
     * - Claridad en el contrato de la interfaz
     * - Documentación de funcionalidad crítica
     * - Base para futuras personalizaciones con @Query si es necesario
     * - Facilita testing y mockeo específico
     *
     * Casos de uso típicos:
     * - Listados paginados en interfaces web
     * - Reportes de empleados por páginas
     * - APIs REST con paginación estándar
     *
     * @param pageable Configuración de paginación (página, tamaño, ordenamiento)
     * @return Page<Employee> Página de empleados con metadatos de paginación
     */
    Page<Employee> findAll(Pageable pageable);

    /**
     * Busca un empleado específico por su número de empleado.
     *
     * Características del método:
     * - Utiliza Query Derivation de Spring Data JPA
     * - "findBy" + "EmpNo" se traduce automáticamente a:
     *   SELECT * FROM employees WHERE emp_no = ?
     * - Método derivado sin necesidad de implementación manual
     * - Retorna un empleado específico o null si no existe
     *
     * Ventajas sobre findById():
     * - Nombre más semánticamente claro y específico del dominio
     * - Hace explícito que busca por el campo empNo (número de empleado)
     * - Mejor legibilidad y comprensión en el código de servicio
     * - Consistencia con la nomenclatura del modelo de datos
     *
     * Casos de uso:
     * - Búsqueda de empleado específico en formularios
     * - Validación de existencia de empleado
     * - Obtención de detalles para pantallas de perfil
     * - Operaciones de consulta por identificador de negocio
     *
     * @param empNo Número único del empleado a buscar
     * @return Employee Empleado encontrado o null si no existe
     */
    Employee findByEmpNo(Integer empNo);
}