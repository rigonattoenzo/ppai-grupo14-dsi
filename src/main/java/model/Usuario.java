package model;

import jakarta.persistence.*;

// Representa un Usuario del sistema.
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_usuario", unique = true, nullable = false, length = 100)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @OneToOne
    @JoinColumn(name = "empleado_id", unique = true, nullable = false)
    private Empleado empleado;

    // Constructor
    public Usuario() {
    }

    public Usuario(String nombreUsuario, String contrasena, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.empleado = empleado;
    }

    // Métodos de la realización de caso de uso
    public Empleado getRiLogueado() {
        return getEmpleado();
    }

    public boolean esContrasenaValida(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String perfilLogueado() {
        return empleado.getNombreCompleto();
    }
}
