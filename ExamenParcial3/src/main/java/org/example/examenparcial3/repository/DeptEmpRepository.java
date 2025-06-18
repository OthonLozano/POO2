package org.example.examenparcial3.repository;

import org.example.examenparcial3.model.DeptEmp;
import org.example.examenparcial3.model.DeptEmpId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio de datos para la entidad DeptEmp (tabla de asociación).
 *
 * Esta interfaz maneja las operaciones de acceso a datos para la relación
 * many-to-many entre Employee y Department, incluyendo el historial temporal
 * de asignaciones departamentales.
 *
 * Características principales:
 * - Gestiona entidades con clave primaria compuesta (DeptEmpId)
 * - Consultas especializadas para navegación bidireccional
 * - Soporte para paginación en consultas de asociación
 * - Métodos de consulta derivados para campos embebidos
 */
@Repository // Marca esta interfaz como un componente de repositorio Spring
public interface DeptEmpRepository extends JpaRepository<DeptEmp, DeptEmpId> {
    // JpaRepository<DeptEmp, DeptEmpId> proporciona:
    // - DeptEmp: Tipo de entidad de asociación gestionada
    // - DeptEmpId: Tipo de clave primaria compuesta

    /**
     * Obtiene todas las asignaciones de empleados para un departamento específico.
     *
     * Características del método:
     * - Utiliza Query Derivation de Spring Data JPA
     * - "findBy" + "Id" (campo embebido) + "DeptNo" (campo dentro del embebido)
     * - Se traduce automáticamente a: SELECT * FROM dept_emp WHERE dept_no = ?
     * - Retorna lista completa sin paginación
     *
     * Casos de uso:
     * - Obtener todos los empleados que han trabajado en un departamento
     * - Análisis de historial departamental completo
     * - Reportes de asignaciones sin límite de registros
     *
     * @param deptNo Número del departamento para filtrar asignaciones
     * @return List<DeptEmp> Lista de todas las asignaciones del departamento
     */
    List<DeptEmp> findByIdDeptNo(String deptNo);

    /**
     * Obtiene las asignaciones de empleados para un departamento con paginación.
     *
     * Características del método:
     * - Sobrecarga del método anterior con soporte de paginación
     * - Misma consulta base pero con LIMIT y OFFSET automáticos
     * - Ideal para departamentos con muchos empleados
     * - Retorna metadatos de paginación (total páginas, elementos, etc.)
     *
     * Ventajas de la paginación:
     * - Mejor rendimiento con grandes volúmenes de datos
     * - Menor uso de memoria
     * - Experiencia de usuario mejorada en interfaces web
     *
     * @param deptNo Número del departamento para filtrar asignaciones
     * @param pageable Configuración de paginación y ordenamiento
     * @return Page<DeptEmp> Página de asignaciones con metadatos de paginación
     */
    Page<DeptEmp> findByIdDeptNo(String deptNo, Pageable pageable);

    /**
     * Obtiene todas las asignaciones departamentales de un empleado específico.
     *
     * Características del método:
     * - Consulta por el campo empNo dentro del ID embebido
     * - Se traduce a: SELECT * FROM dept_emp WHERE emp_no = ?
     * - Retorna historial completo de departamentos del empleado
     * - Sin paginación, asume que un empleado no tiene demasiadas asignaciones
     *
     * Casos de uso:
     * - Historial laboral de un empleado
     * - Tracking de movimientos departamentales
     * - Auditoría de carrera profesional
     * - Reportes de trayectoria del empleado
     *
     * @param empNo Número del empleado para obtener sus asignaciones
     * @return List<DeptEmp> Lista completa de asignaciones del empleado
     */
    List<DeptEmp> findByIdEmpNo(Integer empNo);
}