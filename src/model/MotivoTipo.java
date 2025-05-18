package model;

/**
 * Tipo de motivo para poner fuera de servicio.
 */
public class MotivoTipo {
    private String descripcion;

    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    //getter
    public String getDescripcion() {
        return descripcion;
    }

    // Dentro de MotivoTipo.java

    @Override
    public String toString() {
        return "MotivoTipo{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}
