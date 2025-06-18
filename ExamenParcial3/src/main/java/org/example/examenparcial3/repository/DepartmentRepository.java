package org.example.examenparcial3.repository;

import org.example.examenparcial3.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de datos para la entidad Department.
 *
 * Esta interfaz implementa el patrón Repository utilizando Spring Data JPA,
 * proporcionando operaciones de acceso a datos para departamentos de manera
 * declarativa y automática.
 *
 * Características principales:
 * - Hereda operaciones CRUD básicas de JpaRepository
 * - Implementa paginación para listas grandes de departamentos
 * - Métodos de consulta personalizados basados en naming conventions
 * - Manejo automático de transacciones
 */
@Repository // Marca esta interfaz como un componente de repositorio Spring
public interface DepartmentRepository extends JpaRepository<Department, String> {
    // JpaRepository<Department, String> proporciona:
    // - Department: Tipo de entidad gestionada
    // - String: Tipo de la clave primaria (deptNo)

    /**
     * Obtiene todos los departamentos con paginación.
     *
     * Características:
     * - Sobrescribe el método heredado para explicitar el soporte de paginación
     * - Permite manejar grandes volúmenes de departamentos de forma eficiente
     * - Retorna un objeto Page con metadatos de paginación
     *
     * @param pageable Objeto que contiene información de paginación (página, tamaño, ordenamiento)
     * @return Page<Department> Página de departamentos con metadatos de paginación
     */
    Page<Department> findAll(Pageable pageable);

    /**
     * Busca un departamento por su número de departamento.
     *
     * Características:
     * - Utiliza Spring Data JPA Query Methods (naming convention)
     * - "findBy" + "DeptNo" se traduce automáticamente a:
     *   SELECT * FROM departments WHERE dept_no = ?
     * - Método derivado automáticamente sin necesidad de implementación
     * - Retorna un único departamento o null si no existe
     *
     * Ventajas sobre findById():
     * - Nombre más semánticamente claro y específico del dominio
     * - Hace explícito que busca por el campo deptNo
     * - Mejor legibilidad en el código de servicio
     *
     * @param deptNo Número del departamento a buscar
     * @return Department Departamento encontrado o null si no existe
     */
    Department findByDeptNo(String deptNo);

    /*
     * MÉTODOS HEREDADOS DE JpaRepository<Department, String>:
     *
     * OPERACIONES BÁSICAS CRUD:
     * - save(Department entity): Guarda o actualiza un departamento
     * - findById(String id): Busca por clave primaria
     * - findAll(): Obtiene todos los departamentos
     * - deleteById(String id): Elimina por ID
     * - delete(Department entity): Elimina la entidad
     * - count(): Cuenta total de departamentos
     * - existsById(String id): Verifica si existe un departamento
     *
     * OPERACIONES BATCH:
     * - saveAll(Iterable<Department>): Guarda múltiples departamentos
     * - deleteAll(): Elimina todos los departamentos
     * - deleteAllInBatch(): Eliminación en lote optimizada
     *
     * PAGINACIÓN Y ORDENAMIENTO:
     * - findAll(Sort sort): Obtiene todos con ordenamiento
     * - findAll(Pageable pageable): Obtiene página de resultados
     *
     * CARACTERÍSTICAS TÉCNICAS:
     *
     * 1. PROXY PATTERN:
     *    - Spring crea una implementación proxy en tiempo de ejecución
     *    - No requiere implementación manual de métodos CRUD
     *
     * 2. QUERY DERIVATION:
     *    - findByDeptNo se deriva automáticamente del nombre del método
     *    - Spring analiza el nombre y genera la consulta SQL correspondiente
     *
     * 3. TRANSACTION MANAGEMENT:
     *    - Operaciones de escritura son transaccionales por defecto
     *    - Lectura en modo read-only para optimización
     *
     * 4. EXCEPTION TRANSLATION:
     *    - Convierte excepciones específicas de JPA a DataAccessException
     *    - Abstrae la tecnología de persistencia subyacente
     */
}