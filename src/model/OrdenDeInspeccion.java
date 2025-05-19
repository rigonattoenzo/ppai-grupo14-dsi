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
    private Estado estadoActual;

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

    //geters
    public int getNumeroOrden() {
        return numeroOrden;
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

    public LocalDateTime getFechaFinalizacion() {
        return fechaHoraFinalizacion;
    }

    // Este llama a la estación para pedir su nombre
    public String getNombreEstacionSismologica() {
        return estacion.getNombre();
    }

    public String getIdSismografo() {
        return estacion.obtenerIdentificadorSismografo();
    }

    //setters
    public void setEstado(Estado estado) {
        this.estadoActual = estado;
    }

    public void setFechaHoraCierre(LocalDateTime fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
    }

    public String getObservacionCierreOrden() {
        return observacionCierreOrden;
    }

    public void setObservacionCierreOrden(String observacionCierreOrden) {
        this.observacionCierreOrden = observacionCierreOrden;
    }

    public void ponerSismografoFueraServicio(Estado estado, Map<MotivoTipo, String> motivosYComentarios){
        this.estacion.ponerSismografoFueraServicio(estado, motivosYComentarios);
    }

    //metodos extra
    public boolean sosDeEmpleado(Empleado empleado) {
        return this.empleado.equals(empleado);
    }

    public boolean sosCompletamenteRealizada() {
        return estadoActual.esCompletamenteRealizada();
    }

    // Método principal para que el gestor llame y obtenga los datos
    public Map<String, Object> obtenerDatosOI() {
        Map<String, Object> datos = new HashMap<>();
        datos.put("nroDeOrden", getNumeroOrden());
        datos.put("fechaFinalizacion", getFechaFinalizacion());
        datos.put("nombreEstacionSismologica", getNombreEstacionSismologica());
        datos.put("idSismografo", getIdSismografo());
        return datos;
    }
}
