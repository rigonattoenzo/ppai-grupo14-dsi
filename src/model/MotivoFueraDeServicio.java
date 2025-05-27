package model;

import java.util.Map;

/**
 * Motivo específico de fuera de servicio, con comentario.
 */
public class MotivoFueraDeServicio {
    private String comentario;

    // Asociación
    private MotivoTipo motivoTipo;

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
