package model;

import java.time.LocalDateTime;

/**
 * Representa una Sesión de usuario.
 */
public class Sesion {
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private static Sesion instancia; // Única instancia (empleando el patrón Singleton)
    private Usuario usuario; // Usuario logueado actualmente

    private Sesion() {
    }

    //getters
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

    // Getter
    public Usuario getUsuario() {
        return usuario;
    }

    // Seteo del usuario (ej: al hacer login)
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Metodos del diagrama de Secuencia
    public Empleado getRiLogueado() {
        return usuario.getEmpleado();
    }

    //metodos extra
    public void iniciarSesion(Usuario usuario, LocalDateTime fechaHoraInicio) {
        this.usuario = usuario;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public void cerrarSesion(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    // Método que indica si la sesión está activa (sin fecha de fin)
    public boolean estaActiva() {
        return fechaHoraFin == null;
    }
}
