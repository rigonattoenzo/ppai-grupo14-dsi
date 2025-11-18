package model;

import jakarta.persistence.*;

// Representa un rol de empleado
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // Constructor
    public Rol() {
    }

    public Rol(String nombre, String descripcionRol) {
        this.nombre = nombre;
        this.descripcion = descripcionRol;
    }

    // Métodos de la realización de caso de uso
    public String getNombreRol() {
        return nombre;
    }

    public boolean esResponsableReparacion() {
        return nombre.equalsIgnoreCase("RR");
    }
}
