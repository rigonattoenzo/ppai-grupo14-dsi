package model;

/**
 * Representa un Usuario del sistema.
 */
public class Usuario {
    private String nombreUsuario;
    private String contraseña;

    // Asociación
    private Empleado empleado;  // Cada usuario corresponde a un empleado

    // Constructor
    public Usuario(String nombreUsuario, String contraseña, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.empleado = empleado;
    }

    // Métodos de la realización de caso de uso
    public Empleado getRiLogueado() {
        return getEmpleado();
    }

    public boolean esContrasenaValida(String contrasena) {
        return this.contraseña.equals(contrasena);
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String perfilLogueado() {
        return empleado.getNombreCompleto();
    }

    // Método toString para facilitar la depuración
    /*@Override
    public String toString() {
        return "Usuario: " + nombreUsuario + ", Empleado: " + empleado.getNombreCompleto();
    } */
}
