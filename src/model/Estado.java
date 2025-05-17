package model;

/**
 * Representa un estado dentro del dominio.
 */
public class Estado {
    private String ambito;
    private String nombreEstado;

    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public String getAmbito() {
        return ambito;
    }

    // Dentro de Estado.java

    @Override
    public String toString() {
        return "Estado{" +
                "ambito='" + ambito + '\'' +
                ", nombreEstado='" + nombreEstado + '\'' +
                '}';
    }
}
