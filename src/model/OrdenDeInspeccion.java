package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import model.estados.Estado;
import model.estados.PendienteDeRealizacion;

/**
 * Representa una Orden de Inspección.
 */
public class OrdenDeInspeccion {
    private int numeroOrden;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFinalizacion;
    private LocalDateTime fechaHoraCierre;
    private String observacionCierreOrden;

    // Asociaciones
    private Empleado empleado; // RI responsable
    private EstacionSismologica estacion; // Estación relacionada
    private Estado estadoActual; // Estado

    // Constructor
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
    }

    // ==================== MÉTODOS DE DELEGACIÓN AL ESTADO ====================
    /**
     * Registra la primera actividad (transición a Parcialmente Realizada).
     */
    public void registrarPrimera() {
        this.estadoActual.registrarPrimera(this);
    }

    /**
     * Completa todas las actividades (transición a Completamente Realizada).
     */
    public void completarTodas() {
        this.estadoActual.completarTodas(this);
    }

    /**
     * Cierra la orden (transición a Cerrada).
     */
    public void cerrarOrden(LocalDateTime fechaCierre, String observacion) {
        this.estadoActual.cerrar(this, fechaCierre, observacion);
    }

    /**
     * Pone el sismografo de la estación fuera de servicio.
     * Parámetros:
     * - fechaActual: fecha/hora del cambio de estado
     * - motivos: List<Map<String, Object>> estructura flexible
     * Ejemplo: [{"tipo": "Avería", "comentario": "Vibración excesiva"}]
     */
    public void ponerSismografoFueraServicio(LocalDateTime fechaActual,
            List<Map<String, Object>> motivos) {
        this.estacion.fueraDeServicio(fechaActual, motivos);
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

    public String getIdSismografo() {
        return estacion.obtenerIdentificadorSismografo();
    }

    public LocalDateTime getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public void setEstadoActual(Estado nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    public void setObservacionCierreOrden(String observacionCierreOrden) {
        this.observacionCierreOrden = observacionCierreOrden;
    }

    public boolean sosDeEmpleado(Empleado empleado) {
        return this.empleado.equals(empleado);
    }

    public boolean sosCompletamenteRealizada() {
        return estadoActual.esCompletamenteRealizada();
    }

    public Map<String, Object> obtenerDatosOI() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("nroDeOrden", getNroDeOrden());
        datos.put("fechaFinalizacion", getFechaFinalizacion());
        datos.put("nombreEstacionSismologica", getNombreEstacionSismologica());
        datos.put("idSismografo", getIdSismografo());
        return datos;
    }

    public void cerrar(Estado estadoCerrado) {
        this.setFechaHoraCierre(LocalDateTime.now());
        this.setEstadoActual(estadoCerrado);
    }
}
