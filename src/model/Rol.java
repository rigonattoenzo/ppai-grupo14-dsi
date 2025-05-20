package model;

/**
 * Representa un Rol de empleado.
 */
public class Rol {
    private String nombre;
    private String descripcionRol;

    public Rol(String nombre, String descripcionRol) {
        this.nombre = nombre;
        this.descripcionRol = descripcionRol;
    }

    //getters
    public String getNombreRol() {
        return nombre;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    //metodos
    public boolean esResponsableReparacion() {
        return nombre.equalsIgnoreCase("RR");
    }

    // Dentro de Rol.java
    /*
    @Override
    public String toString() {
        return "Rol{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcionRol + '\'' +
                '}';
    }*/
}
