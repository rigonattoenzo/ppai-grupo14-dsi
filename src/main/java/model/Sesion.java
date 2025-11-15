package model;

import java.time.LocalDateTime;

/**
 * Representa una Sesión de usuario.
 * NOTA: Esta clase NO se persiste en BD (es para manejo en memoria).
 */
public class Sesion {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private static Sesion instancia;
    private Usuario usuario;

    private Sesion() {
    }

    // Métodos del CU
    public Empleado getRiLogueado() {
        return usuario.getEmpleado();
    }

    public void iniciarSesion(Usuario usuario, LocalDateTime fechaHoraInicio) {
        this.usuario = usuario;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public void cerrarSesion(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public boolean estaActiva() {
        return fechaHoraFin == null;
    }

    // Singleton
    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    // Getters
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
