package model;

import java.time.LocalDateTime;

/**
 * Historial de cambios de estado para un objeto.
 */
public class CambioDeEstado {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    // Asociaciones
    private Estado estado;                           // Estado asociado
    private Empleado empleado;                       // Quién realizó el cambio
    private MotivoFueraDeServicio motivo;            // Motivo si aplica

    public CambioDeEstado(Estado estado,
                          Empleado empleado,
                          LocalDateTime inicio,
                          MotivoFueraDeServicio motivo) {
        this.estado = estado;
        this.empleado = empleado;
        this.fechaHoraInicio = inicio;
        this.motivo = motivo;
    }

    public boolean esEstadoActual() {
        return fechaHoraFin == null;
    }

    // getters y seters
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public Estado getEstado() {
        return estado;
    }


    public Empleado getEmpleado() {
        return empleado;
    }

    public MotivoFueraDeServicio getMotivo() {
        return motivo;
    }

    // Getter para fechaHoraInicio
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    // Getter para fechaHoraFin
    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }


    // Método toString para facilitar impresión
    @Override
    public String toString() {
        return "CambioDeEstado{" +
                "estado=" + estado.getNombreEstado() +
                ", empleado=" + empleado.getNombreCompleto() +
                ", inicio=" + fechaHoraInicio +
                ", fin=" + fechaHoraFin +
                ", motivo=" + (motivo != null ? motivo.getTipo().getDescripcion() : "N/A") +
                '}';
    }
}
