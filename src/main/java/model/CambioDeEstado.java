package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import model.estados.Estado;

// Historial de cambios de estado para un objeto.
@Entity
@Table(name = "cambio_de_estado")
public class CambioDeEstado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    // Asociación Many-to-One con Sismografo
    @ManyToOne
    @JoinColumn(name = "sismografo_id", nullable = true)
    private Sismografo sismografo;

    // Asociación Many-to-One con Empleado
    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = true)
    private Empleado empleado;

    // Asociación One-to-Many con MotivoFueraDeServicio
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cambio_estado_id")
    private List<MotivoFueraDeServicio> motivos = new ArrayList<>();

    // Estado asociado (NO persiste, es referencial)
    @Transient
    private Estado estado;

    // Constructor vacío
    public CambioDeEstado() {
    }

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

    public Sismografo getSismografo() {
        return sismografo;
    }

    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
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
