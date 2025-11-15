package model;

import jakarta.persistence.*;

// Tipo de motivo para poner fuera de servicio.
@Entity
@Table(name = "motivo_tipo")
public class MotivoTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    // Constructor
    public MotivoTipo() {
    }

    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    // Métodos de la realización de caso de uso
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

// Métodos extra (no se utilizan, pero los implementamos por si acaso)
/*
 * @Override
 * public String toString() {
 * return "MotivoTipo{" +
 * "descripcion='" + descripcion + '\'' +
 * '}';
 * }
 * }
 */