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

    //metodos vista estatica
    public boolean esResponsableReparacion() {
        return rol != null && rol.esResponsableReparacion();
    }

    public String obtenerMail() {
        return mail;
    }

    //getters
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public Rol getRol() {
        return rol;
    }

    // Getters individuales (nombre, apellido, telefono)
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    // Setter para rol
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Método toString para mejor visualización
    @Override
    public String toString() {
        return "Empleado: " + nombre + " " + apellido + ", Tel: " + telefono + ", Mail: " + mail + ", Rol: " + rol.getNombreRol();
    }
}
