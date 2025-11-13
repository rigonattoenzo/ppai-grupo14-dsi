package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import model.estados.Estado;

/**
 * Historial de cambios de estado para un objeto.
 */
public class CambioDeEstado {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    // Asociaciones
    private Estado estado; // Estado asociado
    private Empleado empleado; // Quién realizó el cambio
    private List<MotivoFueraDeServicio> motivos = new ArrayList<>(); // Motivo si aplica

    // Constructor -> Equivalente al new()
    public CambioDeEstado(Estado estado, LocalDateTime inicio) {
        this.estado = estado;
        this.fechaHoraInicio = inicio;
        this.fechaHoraFin = null;
    }

    // Métodos de la realización de caso de uso
    public boolean esEstadoActual() {
        return this.fechaHoraFin != null;
    }

    public void crearMotivoFueraServicio(Map<MotivoTipo, String> motivosYComentarios) {
        for (Map.Entry<MotivoTipo, String> motv : motivosYComentarios.entrySet()) {
            MotivoTipo tipo = motv.getKey();
            String comentario = motv.getValue();

            MotivoFueraDeServicio motivo = new MotivoFueraDeServicio(comentario, tipo);
            this.motivos.add(motivo);
        }
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    // Método toString para facilitar impresión
    /*
     * @Override
     * public String toString() {
     * return "CambioDeEstado{" +
     * "estado=" + estado.getNombreEstado() +
     * ", empleado=" + empleado.getNombreCompleto() +
     * ", inicio=" + fechaHoraInicio +
     * ", fin=" + fechaHoraFin +
     * //", motivo=" + (motivo != null ? motivo.getTipo().getDescripcion() : "N/A")
     * +
     * '}';
     * }
     */
}
