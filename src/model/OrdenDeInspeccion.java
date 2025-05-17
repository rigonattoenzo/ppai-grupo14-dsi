package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una Orden de Inspección.
 */
public class OrdenDeInspeccion {
    private int numeroOrden;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraCierre;
    private String observacionCierre;

    // Asociaciones
    private Empleado empleado;                // RI responsable
    private EstacionSismologica estacion;     // Estación relacionada

    public OrdenDeInspeccion(int numeroOrden,
                             LocalDateTime inicio,
                             LocalDateTime finalizacion,
                             Empleado empleado,
                             EstacionSismologica estacion) {
        this.numeroOrden = numeroOrden;
        this.fechaHoraInicio = inicio;
        this.fechaHoraFinalizacion = finalizacion;
        this.empleado = empleado;
        this.estacion = estacion;
    }

    public int getNumeroOrden() {
        return numeroOrden;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaHoraFinalizacion;
    }

    public void setEstado(Estado estado) {
        // Podrías agregar lógica para crear un CambioDeEstado, etc.
    }

    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public void setObservacionCierre(String observacion) {
        this.observacionCierre = observacion;
    }

    public boolean estaCompletamenteRealizada() {
        return fechaHoraCierre != null;
    }

    // Getters de las asociaciones
    public Empleado getEmpleado() {
        return empleado;
    }

    public EstacionSismologica getEstacion() {
        return estacion;
    }

    // Getter para fechaHoraCierre
    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }
}
