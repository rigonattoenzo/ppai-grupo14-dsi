package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representa un Sismógrafo.
 */
public class Sismografo {
    private String identificadorSismografo;
    private String nroSerie;
    private LocalDateTime fechaAdquisicion;

    // Asociaciones
    private EstacionSismologica estacion;            // Estación donde está instalado
    private List<CambioDeEstado> cambiosDeEstado = new ArrayList<>();
    private CambioDeEstado cambioEstado; // Historial de cambios

    public Sismografo(String identificador,
                      String nroSerie,
                      LocalDateTime fechaAdquisicion,
                      EstacionSismologica estacion) {
        this.identificadorSismografo = identificador;
        this.nroSerie = nroSerie;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estacion = estacion;
    }

    // Getters
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

    // Setter para la estación
    public void setEstacion(EstacionSismologica estacion) {
        this.estacion = estacion;
    }

    // Métodos extra
    public void fueraServicio(Estado estado, Map<MotivoTipo, String> motivosYComentarios){
        CambioDeEstado actual = null;
        for (CambioDeEstado cambio : cambiosDeEstado) {
            if (cambio.esEstadoActual()) {
                actual = cambio;
                break;
            }
        }

        if (actual != null) {
            actual.setFechaHoraFin(LocalDateTime.now());
        }

        ejecutarCambioDeEstado(estado, motivosYComentarios);
    }

    public void setCambiosDeEstado(List<CambioDeEstado> cambiosDeEstado) {
        this.cambiosDeEstado = cambiosDeEstado;
    }

    public void ejecutarCambioDeEstado(Estado estado, Map<MotivoTipo, String> motivosYComentarios){
        CambioDeEstado nuevo = new CambioDeEstado(estado, LocalDateTime.now());
        nuevo.crearMotivoFueraServicio(motivosYComentarios);
    }
}
