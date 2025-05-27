package model;

/**
 * Representa a un Empleado.
 */
public class Empleado {
    private String nombre;
    private String apellido;
    private String telefono;
    private String mail;

    // Asociación
    private Rol rol;   // Rol que desempeña el empleado

    // Constructor
    public Empleado(String nombre,
                    String apellido,
                    String telefono,
                    String mail,
                    Rol rol) {
        this.nombre = nombre;
        this.apellido = apellido;
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
    /*@Override
    public String toString() {
        return "Empleado: " + nombre + " " + apellido + ", Tel: " + telefono + ", Mail: " + mail + ", Rol: " + rol.getNombreRol();
    } */
}
