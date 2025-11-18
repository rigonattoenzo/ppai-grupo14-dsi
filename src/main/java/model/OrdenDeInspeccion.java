package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import model.estados.Estado;
import model.estados.PendienteDeRealizacion;

/**
 * Representa una Orden de Inspección.
 */
@Entity
@Table(name = "orden_inspeccion")
public class OrdenDeInspeccion {
    @Id
    @Column(name = "nro_orden", unique = true, nullable = false)
    private int numeroOrden;

    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_finalizacion")
    private LocalDateTime fechaHoraFinalizacion;

    @Column(name = "fecha_hora_cierre")
    private LocalDateTime fechaHoraCierre;

    @Column(name = "observacion_cierre_orden", columnDefinition = "TEXT")
    private String observacionCierreOrden;

    // Asociación Many-to-One con Empleado
    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = true)
    private Empleado empleado;

    // Asociación Many-to-One con EstacionSismologica
    @ManyToOne
    @JoinColumn(name = "estacion_id", nullable = true)
    private EstacionSismologica estacion;

    @Column(name = "estado")
    private String estado; // <-- Este campo se guarda en la BD

    // Estado actual (NO persiste, se reconstruye en runtime)
    @Transient
    private Estado estadoActual;

    // Constructor vacío
    public OrdenDeInspeccion() {
        if (estado == null) {
            this.estadoActual = new PendienteDeRealizacion();
            this.estado = "PendienteDeRealizacion";
        } else {
            this.estadoActual = Estado.fromString(estado);
        }
    }

    // Constructor con parámetros
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
        this.estadoActual = new PendienteDeRealizacion(); // Estado inicial
        this.estado = "PendienteDeRealizacion"; // Para persistencia
    }

    // ==================== MÉTODOS DE DELEGACIÓN AL ESTADO ====================
    /**
     * Registra la primera actividad (transición a Parcialmente Realizada).
     */
    public void registrarPrimera() {
        this.estadoActual.registrarPrimera(this);
        this.estado = "ParcialmenteRealizada";
    }

    /**
     * Completa todas las actividades (transición a Completamente Realizada).
     */
    public void completarTodas() {
        this.estadoActual.completarTodas(this);
        this.estado = "CompletamenteRealizada";
    }

    /**
     * Cierra la orden (transición a Cerrada).
     */
    public void cerrarOrden(LocalDateTime fechaCierre, String observacion) {
        this.estadoActual.cerrar(this);
        this.setFechaHoraCierre(fechaCierre);
        this.setObservacionCierreOrden(observacion);
        this.estado = "Cerrada";
    }

    public Map<String, Object> obtenerDatosOI() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("nroDeOrden", getNroDeOrden());
        datos.put("fechaFinalizacion", getFechaFinalizacion());
        datos.put("nombreEstacionSismologica", getNombreEstacionSismologica());
        datos.put("codigoEstacionSismologica", getCodigoEstacionSismologica());
        return datos;
    }

    // ==================== GETTERS Y SETTERS ====================
    public int getNroDeOrden() {
        return numeroOrden;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaHoraFinalizacion;
    }

    public String getNombreEstacionSismologica() {
        return estacion.getNombre();
    }

    public String getCodigoEstacionSismologica() {
        if (this.estacion == null) {
            return null;
        }
        return this.estacion.getCodigoEstacion();
    }

    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    @PostLoad
    private void postLoad() {
        // Reconstruir el estado transient a partir del String persistido
        this.estadoActual = Estado.fromString(this.estado);
    }

    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public void setEstadoActual(Estado nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.estado = nuevoEstado.getClass().getSimpleName();
    }

    public String getEstadoActual() {
        if (this.estadoActual != null) {
            return this.estadoActual.getClass().getSimpleName();
        }
        return Estado.fromString(this.estado).getClass().getSimpleName();
    }

    public EstacionSismologica getEstacion() {
        return this.estacion;
    }

    public void setObservacionCierreOrden(String observacionCierreOrden) {
        this.observacionCierreOrden = observacionCierreOrden;
    }

    public String getObservacionCierreOrden() {
        return this.observacionCierreOrden;
    }

    public boolean sosDeEmpleado(Empleado empleado) {
        return this.empleado != null
                && empleado != null
                && this.empleado.getId() == empleado.getId();
    }

    public boolean sosCompletamenteRealizada() {
        return "CompletamenteRealizada".equals(this.estado);
    }

    public void cerrar(Estado estadoCerrado) {
        this.setFechaHoraCierre(LocalDateTime.now());
        this.setEstadoActual(estadoCerrado);
    }
}
