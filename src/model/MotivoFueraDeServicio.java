package model;

/**
 * Motivo específico de fuera de servicio, con comentario.
 */
public class MotivoFueraDeServicio {
    private String comentario;

    // Asociación
    private MotivoTipo tipo;

    public MotivoFueraDeServicio(String comentario, MotivoTipo tipo) {
        this.comentario = comentario;
        this.tipo = tipo;
    }

    //getters y setters
    public String getComentario() {
        return comentario;
    }

    public MotivoTipo getTipo() {
        return tipo;
    }
}
