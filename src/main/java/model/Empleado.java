package model;

import jakarta.persistence.*;

// Representa a un empleado del sistema
@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "mail", nullable = false, length = 150)
    private String mail;

    // Asociaciones
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = true)
    private Rol rol;

    // Constructor sin parámetros
    public Empleado() {
    }

    // Constructor
    public Empleado(String nombre,
            String apellido,
            String codigo,
            String telefono,
            String mail,
            Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.codigo = codigo;
        this.telefono = telefono;
        this.mail = mail;
        this.rol = rol;
    }

    // Métodos de la realización de caso de uso
    public boolean esResponsableReparacion() {
        return rol != null && rol.esResponsableReparacion();
    }

    public String obtenerMail() {
        return mail;
    }

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public Rol getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Método toString para mejor visualización
    /*
     * @Override
     * public String toString() {
     * return "Empleado: " + nombre + " " + apellido + ", Tel: " + telefono +
     * ", Mail: " + mail + ", Rol: " + rol.getNombreRol();
     * }
     */
}
