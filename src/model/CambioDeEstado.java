package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

    public void crearMotivoFueraServicio(Map<MotivoTipo, String> motivosYComentarios){
        for (Map.Entry<MotivoTipo, String> motv : motivosYComentarios.entrySet()) {
            MotivoTipo tipo = motv.getKey();
            String comentario = motv.getValue();

            MotivoFueraDeServicio motivo = new MotivoFueraDeServicio(comentario, tipo);
            this.motivos.add(motivo);
        }
    }

    // Getters y Seters

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

    public List<MotivoFueraDeServicio> getMotivosFueraServicio() {
        return this.motivos;
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
