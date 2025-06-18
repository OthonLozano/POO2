package org.example.examenparcial3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Entidad JPA que representa la relación many-to-many entre Employee y Department.
 *
 * Esta es una tabla de asociación (junction table) que almacena el historial
 * de asignaciones de empleados a departamentos, incluyendo las fechas de
 * inicio y fin de cada asignación.
 *
 * Características principales:
 * - Implementa una relación many-to-many con atributos adicionales
 * - Mantiene historial temporal de asignaciones departamentales
 * - Utiliza clave primaria compuesta (DeptEmpId)
 * - Permite tracking de movimientos organizacionales
 */
@Entity // Marca esta clase como una entidad JPA persistente
@Table(name = "dept_emp") // Mapea la entidad a la tabla "dept_emp" en la base de datos
@Data // Lombok: Genera automáticamente getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: Genera constructor sin parámetros (requerido por JPA)
@AllArgsConstructor // Lombok: Genera constructor con todos los parámetros
public class DeptEmp {

    /**
     * Clave primaria compuesta que incluye emp_no y dept_no.
     *
     * Características:
     * - Utiliza @EmbeddedId para claves primarias complejas
     * - Referencia a la clase DeptEmpId que encapsula ambos IDs
     * - Garantiza unicidad de la combinación empleado-departamento-período
     */
    @EmbeddedId // Indica que esta es una clave primaria embebida/compuesta
    private DeptEmpId id;

    /**
     * Fecha de inicio de la asignación del empleado al departamento.
     *
     * Características:
     * - Marca cuándo comenzó el empleado en este departamento
     * - Utiliza LocalDate para manejo de fechas sin zona horaria
     * - Esencial para el historial temporal de empleados
     */
    @Column(name = "from_date") // Mapea al campo "from_date" de la tabla
    private LocalDate fromDate;

    /**
     * Fecha de fin de la asignación del empleado al departamento.
     *
     * Características:
     * - Puede ser null si la asignación está activa
     * - Marca cuándo terminó el empleado en este departamento
     * - Permite identificar asignaciones actuales vs históricas
     */
    @Column(name = "to_date") // Mapea al campo "to_date" de la tabla
    private LocalDate toDate;

    /**
     * Relación many-to-one hacia la entidad Employee.
     *
     * Configuración específica:
     * - insertable=false, updatable=false: Esta columna está controlada por el ID embebido
     * - Permite navegación desde DeptEmp hacia Employee
     * - Evita conflictos de mapeo con la clave primaria compuesta
     */
    @ManyToOne // Define relación many-to-one con Employee
    @JoinColumn(name = "emp_no", insertable = false, updatable = false) // Mapea pero no controla la columna
    private Employee employee;

    /**
     * Relación many-to-one hacia la entidad Department.
     *
     * Configuración específica:
     * - insertable=false, updatable=false: Esta columna está controlada por el ID embebido
     * - Permite navegación desde DeptEmp hacia Department
     * - Evita conflictos de mapeo con la clave primaria compuesta
     */
    @ManyToOne // Define relación many-to-one con Department
    @JoinColumn(name = "dept_no", insertable = false, updatable = false) // Mapea pero no controla la columna
    private Department department;
}