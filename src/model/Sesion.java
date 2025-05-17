package model;

import java.time.LocalDateTime;

/**
 * Representa una Sesión de usuario.
 */
public class Sesion {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    public Sesion(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public void cerrarSesion(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    // Método que indica si la sesión está activa (sin fecha de fin)
    public boolean estaActiva() {
        return fechaHoraFin == null;
    }
}
