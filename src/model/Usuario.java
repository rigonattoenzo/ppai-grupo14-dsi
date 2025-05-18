package model;

/**
 * Representa un Usuario del sistema.
 */
public class Usuario {
    private String nombreUsuario;
    private String contrasena;

    // Asociación
    private Empleado empleado;  // Cada usuario corresponde a un empleado

    public Usuario(String nombreUsuario, String contrasena, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.empleado = empleado;
    } // Constructor

    //getters
    public Empleado getRiLogueado() {
        return getEmpleado();
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    //metodos extra
    public String perfilLogueado() {
        return empleado.getNombreCompleto();
    }

    // Método para validar contraseña
    public boolean esContrasenaValida(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        return "Usuario: " + nombreUsuario + ", Empleado: " + empleado.getNombreCompleto();
    }
}
