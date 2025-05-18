package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un Sismógrafo.
 */
public class Sismografo {
    private String identificadorSismografo;
    private String nroSerie;
    private LocalDateTime fechaAdquisicion;

    // Asociaciones
    private EstacionSismologica estacion;            // Estación donde está instalado
    private List<CambioDeEstado> cambiosDeEstado
            = new ArrayList<>();                       // Historial de cambios

    public Sismografo(String identificador,
                      String nroSerie,
                      LocalDateTime fechaAdquisicion,
                      EstacionSismologica estacion) {
        this.identificadorSismografo = identificador;
        this.nroSerie = nroSerie;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estacion = estacion;
    }

    //getters
    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public List<CambioDeEstado> getCambiosDeEstado() {
        return cambiosDeEstado;
    }

    public EstacionSismologica getEstacion() {
        return estacion;
    }

    // Getter para fechaAdquisicion
    public LocalDateTime getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    // Getter para nroSerie
    public String getNroSerie() {
        return nroSerie;
    }

    //setters
    // Setter para estacion, en caso de que el sismografo cambie de estación
    public void setEstacion(EstacionSismologica estacion) {
        this.estacion = estacion;
    }

    //metodos extra
    public void agregarCambioDeEstado(CambioDeEstado cambio) {
        cambiosDeEstado.add(cambio);
    }
}
