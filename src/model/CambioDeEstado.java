package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Historial de cambios de estado para un objeto.
 */
public class CambioDeEstado {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    // Asociaciones
    private Estado estado;                           // Estado asociado
    private Empleado empleado;                       // Quién realizó el cambio
    private List<MotivoFueraDeServicio> motivos = new ArrayList<>();       // Motivo si aplica

    public CambioDeEstado(Estado estado,
                          LocalDateTime inicio) {
        this.estado = estado;
        this.fechaHoraInicio = inicio;
    }

    public boolean esEstadoActual() {
        return this.fechaHoraFin != null;
    }

    // getters y seters

    public void crearMotivoFueraServicio(List<MotivoFueraDeServicio> comentarios){
        for (int i = 0; i < comentarios.length; i++){
            MotivoFueraDeServicio motivo = new MotivoFueraDeServicio(comentarios.get(i));
            motivos.add(motivo);
        }
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public Estado getEstado() {
        return estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

   /* public MotivoFueraDeServicio getMotivo() {
        return motivo;
    }
    */

    // Getter para fechaHoraInicio
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    // Getter para fechaHoraFin
    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }


    // Método toString para facilitar impresión
    /*@Override
    public String toString() {
        return "CambioDeEstado{" +
                "estado=" + estado.getNombreEstado() +
                ", empleado=" + empleado.getNombreCompleto() +
                ", inicio=" + fechaHoraInicio +
                ", fin=" + fechaHoraFin +
                //", motivo=" + (motivo != null ? motivo.getTipo().getDescripcion() : "N/A") +
                '}';
    }*/
}
