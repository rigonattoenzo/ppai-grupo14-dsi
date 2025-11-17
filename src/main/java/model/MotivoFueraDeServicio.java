package model;

import jakarta.persistence.*;

// Motivo específico de fuera de servicio con comentario.
@Entity
@Table(name = "motivo_fuera_de_servicio")
public class MotivoFueraDeServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "motivo_id", nullable = true)
    private MotivoTipo motivoTipo;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = true)
    private OrdenDeInspeccion orden;

    // Constructor con parámetros
    public MotivoFueraDeServicio() {
    }

    // Constructor -> Equivalente al new()
    public MotivoFueraDeServicio(String comentario, MotivoTipo motivoTipo) {
        this.comentario = comentario;
        this.motivoTipo = motivoTipo;
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public String getComentario() {
        return comentario;
    }

    public MotivoTipo getTipo() {
        return motivoTipo;
    }
}
