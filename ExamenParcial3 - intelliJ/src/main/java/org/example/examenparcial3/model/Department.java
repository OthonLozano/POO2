package org.example.examenparcial3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidad JPA que representa un departamento en la base de datos.
 *
 * Esta clase modela la estructura de la tabla "departments" y define
 * las propiedades básicas de un departamento organizacional.
 *
 * Utiliza Lombok para reducir el código boilerplate y JPA para
 * el mapeo objeto-relacional con la base de datos.
 */
@Entity // Marca esta clase como una entidad JPA persistente
@Table(name = "departments") // Mapea la entidad a la tabla "departments" en la base de datos
@Data // Lombok: Genera automáticamente getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: Genera constructor sin parámetros (requerido por JPA)
@AllArgsConstructor // Lombok: Genera constructor con todos los parámetros
public class Department {

    /**
     * Identificador único del departamento.
     *
     * Características:
     * - Clave primaria de la entidad
     * - Tipo String para códigos alfanuméricos (ej: "HR", "IT", "SALES")
     * - Mapea a la columna "dept_no" en la base de datos
     * - No usa generación automática, sugiere códigos predefinidos
     */
    @Id // Marca este campo como la clave primaria de la entidad
    @Column(name = "dept_no") // Mapea al campo "dept_no" de la tabla
    private String deptNo;

    /**
     * Nombre descriptivo del departamento.
     *
     * Características:
     * - Almacena el nombre completo del departamento
     * - Mapea a la columna "dept_name" en la base de datos
     * - Ejemplo: "Human Resources", "Information Technology"
     */
    @Column(name = "dept_name") // Mapea al campo "dept_name" de la tabla
    private String deptName;
}