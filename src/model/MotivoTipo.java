package model;

/**
 * Tipo de motivo para poner fuera de servicio.
 */
public class MotivoTipo {
    private String descripcion;

    public MotivoTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

   /* @Override
   public String toString() {
        return "MotivoTipo{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}*/