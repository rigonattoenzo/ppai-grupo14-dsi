package model;

import java.time.LocalDateTime;

/**
 * Representa una Sesión de usuario.
 */
public class Sesion {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    private static Sesion instancia; // Única instancia (empleando el patrón Singleton)

    // Asociación
    private Usuario usuario; // Usuario logueado actualmente

    private Sesion() {}

    // Métodos de la realización de caso de uso
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

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

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
