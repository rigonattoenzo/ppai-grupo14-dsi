package model;

/**
 * Representa un estado dentro del dominio.
 */
public class Estado {
    private String ambito;
    private String nombreEstado;

    // Constructor -> Equivalente al new()
    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public String getNombreEstado() {
        return nombreEstado;
    }

    public String getAmbito() {
        return ambito;
    }

    // Métodos de la realización de caso de uso
    public boolean esCompletamenteRealizada() {
        return "Completamente Realizada".equalsIgnoreCase(nombreEstado);
    }

    public boolean sosAmbitoOrdenDeInspeccion() {
        return "Orden de Inspeccion".equalsIgnoreCase(ambito);
    }

    public boolean sosCerrada() {
        return "Cerrada".equalsIgnoreCase(nombreEstado);
    }

    public boolean sosAmbitoSismografo() {
        return "Sismografo".equalsIgnoreCase(ambito);
    }

    public boolean sosFueraDeServicio() {
        return "Fuera De Servicio".equalsIgnoreCase(nombreEstado);
    }

    /*
    @Override
    public String toString() {
        return "Estado{" +
                "ambito='" + ambito + '\'' +
                ", nombreEstado='" + nombreEstado + '\'' +
                '}';
    } */
}
