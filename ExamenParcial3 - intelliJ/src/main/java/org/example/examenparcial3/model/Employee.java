package org.example.examenparcial3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Entidad JPA que representa un empleado en el sistema de recursos humanos.
 *
 * Esta clase modela la información básica de un empleado, incluyendo
 * datos personales y laborales fundamentales. Es el núcleo del sistema
 * de gestión de empleados y se relaciona con departamentos a través
 * de la tabla de asociación DeptEmp.
 *
 * Características principales:
 * - Información personal básica (nombre, fecha nacimiento, género)
 * - Datos laborales esenciales (número empleado, fecha contratación)
 * - Diseño simple sin relaciones directas para mejor rendimiento
 */
@Entity // Marca esta clase como una entidad JPA persistente
@Table(name = "employees") // Mapea la entidad a la tabla "employees" en la base de datos
@Data // Lombok: Genera automáticamente getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: Genera constructor sin parámetros (requerido por JPA)
@AllArgsConstructor // Lombok: Genera constructor con todos los parámetros
public class Employee {

    /**
     * Número único del empleado - identificador principal.
     *
     * Características:
     * - Clave primaria de la entidad Employee
     * - Tipo Integer para IDs numéricos secuenciales
     * - No usa @GeneratedValue, sugiere asignación manual o externa
     * - Mapea a la columna "emp_no" en la base de datos
     */
    @Id // Marca este campo como la clave primaria de la entidad
    @Column(name = "emp_no") // Mapea explícitamente al campo "emp_no" de la tabla
    private Integer empNo;

    /**
     * Fecha de nacimiento del empleado.
     *
     * Características:
     * - Utiliza LocalDate para fechas sin zona horaria
     * - Esencial para cálculos de edad y elegibilidad
     * - Información personal protegida por políticas de privacidad
     */
    @Column(name = "birth_date") // Mapea al campo "birth_date" de la tabla
    private LocalDate birthDate;

    /**
     * Nombre(s) del empleado.
     *
     * Características:
     * - Almacena el primer nombre o nombres del empleado
     * - Campo de texto para nombres compuestos
     * - Usado en informes y comunicaciones oficiales
     */
    @Column(name = "first_name") // Mapea al campo "first_name" de la tabla
    private String firstName;

    /**
     * Apellido(s) del empleado.
     *
     * Características:
     * - Almacena el apellido o apellidos del empleado
     * - Campo de texto para apellidos compuestos
     * - Usado junto con firstName para identificación completa
     */
    @Column(name = "last_name") // Mapea al campo "last_name" de la tabla
    private String lastName;

    /**
     * Género del empleado.
     *
     * Características:
     * - Campo de texto simple (probablemente códigos como "M", "F")
     * - Información demográfica para reportes y estadísticas
     * - Considerar enum para mayor seguridad de tipos en versiones futuras
     */
    @Column(name = "gender") // Mapea al campo "gender" de la tabla
    private String gender;

    /**
     * Fecha de contratación del empleado.
     *
     * Características:
     * - Marca cuándo el empleado se unió a la empresa
     * - Utilizada para cálculos de antigüedad y beneficios
     * - Base para reportes de recursos humanos y aniversarios
     * - Esencial para políticas laborales basadas en tiempo de servicio
     */
    @Column(name = "hire_date") // Mapea al campo "hire_date" de la tabla
    private LocalDate hireDate;
}