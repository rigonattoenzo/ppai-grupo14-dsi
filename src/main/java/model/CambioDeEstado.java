package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import model.estados.Estado;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sismografo_id")
    private Sismografo sismografo;

    // Asociación Many-to-One con Empleado
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empleado_id", nullable = true)
    private Empleado empleado;

    // Asociación One-to-Many con MotivoFueraDeServicio
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cambio_estado_id")
    private List<MotivoFueraDeServicio> motivos = new ArrayList<>();

    // Estado asociado (NO persiste, es referencial)
    @Transient
    private Estado estado;

    // Constructor vacío
    public CambioDeEstado() {
    }

    // Constructor con sismografo (para CREAR un nuevo cambio)
    public CambioDeEstado(Estado estado, LocalDateTime fechaHoraInicio, Sismografo sismografo) {
        this.estado = estado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.sismografo = sismografo;
        this.fechaHoraFin = null; // Null porque está abierto
        this.empleado = null; // Null porque no se cerró aún
    }

    // Constructor completo para facilitar inicialización
    public CambioDeEstado(Estado estado, LocalDateTime inicio) {
        this.estado = estado;
        this.fechaHoraInicio = inicio;
    }

    // Métodos
    public boolean esEstadoActual() {
        return this.fechaHoraFin == null;
    }

    // 20) ❗❗
    public void crearMotivoFueraServicio(Map<MotivoTipo, String> motivosYComentarios) {
        // En este bucle se crean todos los MotivoFueraServicio
        // Cada MotivoFueraServicio tendrá 1 MotivoTipo
        for (Map.Entry<MotivoTipo, String> motv : motivosYComentarios.entrySet()) {
            MotivoTipo tipo = motv.getKey();
            String comentario = motv.getValue();

            // 21) ❗❗ --> Volver a InhabilitadoPorInspeccion
            MotivoFueraDeServicio motivo = new MotivoFueraDeServicio(comentario, tipo);
            this.motivos.add(motivo);
        }
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
    }

    // Getters
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
}
