package org.example.examenparcial3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Clase de clave primaria compuesta para la entidad DeptEmp.
 *
 * Esta clase encapsula los campos que forman la clave primaria de la tabla
 * de asociación dept_emp, permitiendo identificar únicamente cada relación
 * empleado-departamento.
 *
 * Implementa Serializable como requisito de JPA para claves primarias
 * compuestas, lo que permite:
 * - Almacenamiento en caché
 * - Serialización para distribución remota
 * - Uso como identificador en operaciones de persistencia
 */
@Embeddable // Marca esta clase como embebible en otras entidades JPA
@Data // Lombok: Genera getters, setters, toString, equals y hashCode automáticamente
@NoArgsConstructor // Lombok: Constructor sin parámetros (requerido por JPA)
@AllArgsConstructor // Lombok: Constructor con todos los parámetros
public class DeptEmpId implements Serializable {

    /**
     * Serial Version UID para control de versiones durante la serialización.
     *
     * Características:
     * - Garantiza compatibilidad entre versiones de la clase
     * - Requerido al implementar Serializable
     * - Valor 1L indica versión inicial de la clase
     */
    private static final long serialVersionUID = 1L;

    /**
     * Número del empleado - parte 1 de la clave primaria compuesta.
     *
     * Características:
     * - Tipo Integer para IDs numéricos de empleados
     * - Referencia al campo emp_no de la tabla dept_emp
     * - Debe coincidir con la clave primaria de la tabla employees
     */
    @Column(name = "emp_no") // Mapea al campo "emp_no" en la base de datos
    private Integer empNo;

    /**
     * Número del departamento - parte 2 de la clave primaria compuesta.
     *
     * Características:
     * - Tipo String para códigos alfanuméricos de departamentos
     * - Referencia al campo dept_no de la tabla dept_emp
     * - Debe coincidir con la clave primaria de la tabla departments
     */
    @Column(name = "dept_no") // Mapea al campo "dept_no" en la base de datos
    private String deptNo;
}