package model;

/**
 * Tipo de motivo para poner fuera de servicio.
 */
public class MotivoTipo {
    private String descripcion;

    // Constructor
    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    // Métodos de la realización de caso de uso
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
   /* @Override
   public String toString() {
        return "MotivoTipo{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}*/