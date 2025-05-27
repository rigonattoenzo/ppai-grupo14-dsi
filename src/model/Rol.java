package model;

/**
 * Representa un Rol de empleado.
 */
public class Rol {
    private String nombre;
    private String descripcionRol;

    // Constructor
    public Rol(String nombre, String descripcionRol) {
        this.nombre = nombre;
        this.descripcionRol = descripcionRol;
    }

    // Métodos de la realización de caso de uso
    public String getNombreRol() {
        return nombre;
    }

    public boolean esResponsableReparacion() {
        return nombre.equalsIgnoreCase("RR");
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public String getDescripcionRol() {
        return descripcionRol;
    }

    /*
    @Override
    public String toString() {
        return "Rol{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcionRol + '\'' +
                '}';
    }*/
}
