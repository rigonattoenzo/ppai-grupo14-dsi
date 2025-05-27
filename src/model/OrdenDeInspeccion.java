package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    private Empleado empleado;                  // RI responsable
    private EstacionSismologica estacion;       // Estación relacionada
    private Estado estado;                // Estado

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
    }

    // Métodos de la realización de caso de uso
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

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void ponerSismografoFueraServicio(Estado estado, Map<MotivoTipo, String> motivosYComentarios){
        this.estacion.ponerSismografoFueraServicio(estado, motivosYComentarios);
    }

    public boolean sosDeEmpleado(Empleado empleado) {
        return this.empleado.equals(empleado);
    }

    public boolean sosCompletamenteRealizada() {
        return estado.esCompletamenteRealizada();
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
        this.setEstado(estadoCerrado);
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public Empleado getEmpleado() {
        return empleado;
    }

    public EstacionSismologica getEstacion() {
        return estacion;
    }

    public String getObservacionCierreOrden() {
        return observacionCierreOrden;
    }

    public void setObservacionCierreOrden(String observacionCierreOrden) {
        this.observacionCierreOrden = observacionCierreOrden;
    }
}
