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
    private EstacionSismologica estacionSismologica;        // Estación donde está instalado
    private List<CambioDeEstado> cambiosDeEstado = new ArrayList<>();
    private CambioDeEstado cambioEstado;                    // Historial de cambios

    // Constructor -> Equivalente al new()
    public Sismografo(String identificador,
                      String nroSerie,
                      LocalDateTime fechaAdquisicion,
                      EstacionSismologica estacion) {
        this.identificadorSismografo = identificador;
        this.nroSerie = nroSerie;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estacionSismologica = estacion;
    }

    // Métodos de la realización de caso de uso
    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public void fueraDeServicio(Estado estado, Map<MotivoTipo, String> motivosYComentarios){
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

    public void ejecutarCambioDeEstado(Estado estado, Map<MotivoTipo, String> motivosYComentarios){
        CambioDeEstado nuevo = new CambioDeEstado(estado, LocalDateTime.now());
        nuevo.crearMotivoFueraServicio(motivosYComentarios);
    }

    // agregarCambioEstado está de más

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public List<CambioDeEstado> getCambiosDeEstado() {
        return cambiosDeEstado;
    }

    public EstacionSismologica getEstacion() {
        return estacionSismologica;
    }

    public LocalDateTime getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setEstacion(EstacionSismologica estacion) {
        this.estacionSismologica = estacion;
    }

    public void setCambiosDeEstado(List<CambioDeEstado> cambiosDeEstado) {
        this.cambiosDeEstado = cambiosDeEstado;
    }
}
