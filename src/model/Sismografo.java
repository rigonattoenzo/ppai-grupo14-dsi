package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.estados.Estado;
import model.estados.EnLinea;

/**
 * Representa un Sismógrafo.
 * Implementa el patrón State delegando las transiciones al estado actual.
 */
public class Sismografo {
    private String identificadorSismografo;
    private String nroSerie;
    private LocalDateTime fechaAdquisicion;

    // Asociaciones
    private EstacionSismologica estacionSismologica; // Estación donde está instalado
    private List<CambioDeEstado> cambiosDeEstado = new ArrayList<>(); // Historial de cambios

    // Estado actual del sismografo (Patrón State)
    private Estado estadoActual;

    // Constructor -> Equivalente al new()
    public Sismografo(String identificador,
            String nroSerie,
            LocalDateTime fechaAdquisicion,
            EstacionSismologica estacion) {
        this.identificadorSismografo = identificador;
        this.nroSerie = nroSerie;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estacionSismologica = estacion;
        // Estado inicial: En Línea
        this.estadoActual = new EnLinea();
    }

    // ========== MÉTODOS DE DELEGACIÓN AL ESTADO ==========
    // Estos métodos delegan la responsabilidad al estado actual

    /**
     * Transición: En Línea -> Inhabilitado por Inspección
     */
    public void inhabilitar() {
        this.estadoActual.inhabilitar(this);
    }

    /**
     * Transición: Inhabilitado por Inspección -> Fuera de Servicio
     * Este es el método clave para el caso de uso 37.
     */
    public void fueraDeServicio(LocalDateTime fechaActual,
            List<Map<String, Object>> motivos) {
        CambioDeEstado[] cambiosArray = this.cambiosDeEstado.toArray(new CambioDeEstado[0]);
        this.estadoActual.fueraServicio(this, fechaActual, cambiosArray, motivos);
    }

    /**
     * Transición: Fuera de Servicio -> En Línea
     */
    public void enLinea() {
        this.estadoActual.enLinea(this);
    }

    /**
     * Transición: En Instalación -> Reclamado
     */
    public void reclamar() {
        this.estadoActual.reclamar(this);
    }

    /**
     * Transición: Disponible -> En Espera de Certificación
     */
    public void enEspera() {
        this.estadoActual.enEspera(this);
    }

    /**
     * Transición: Disponible -> Incluido en Plan de Construcción
     */
    public void incluirEnPlanConstruccion() {
        this.estadoActual.incluirEnPlanConstruccion(this);
    }

    /**
     * Transición: Habilitado para Construcción -> En Instalación
     */
    public void iniciarPlanConstruccion() {
        this.estadoActual.iniciarPlanConstruccion(this);
    }

    /**
     * Transición: En Instalación -> En Línea
     */
    public void enInstalacion() {
        this.estadoActual.enInstalacion(this);
    }

    /**
     * Transición: Incluido en Plan -> En Instalación
     * O Reclamado -> En Instalación (después de reparación)
     */
    public void disponibilizar() {
        this.estadoActual.disponibilizar(this);
    }

    /**
     * Transición: En Espera de Certificación -> Habilitado para Construcción
     */
    public void recibirCertificacion() {
        this.estadoActual.recibirCertificacion(this);
    }

    /**
     * Transición: Fuera de Servicio -> De Baja
     * O Reclamado -> De Baja (si es irreparable)
     */
    public void darDeBaja() {
        this.estadoActual.darDeBaja(this);
    }

    // ========== SETTERS Y GETTERS ==========

    /**
     * Cambia el estado actual del sismografo.
     * Usado internamente por los estados para transicionar.
     */
    public void setEstadoActual(Estado nuevoEstado) {
        /*
         * System.out.println("Sismografo " + identificadorSismografo +
         * " cambió de estado: " + this.estadoActual.getNombreEstado() +
         * " -> " + nuevoEstado.getNombreEstado());
         */
        this.estadoActual = nuevoEstado;
    }

    /**
     * Obtiene el estado actual del sismografo.
     */
    public Estado getEstadoActual() {
        return this.estadoActual;
    }

    /**
     * Agrega un cambio de estado al historial.
     */
    public void setCambioEstado(CambioDeEstado cambio) {
        this.cambiosDeEstado.add(cambio);
    }

    /**
     * Métodos getter existentes
     */
    public String getIdentificadorSismografo() {
        return identificadorSismografo;
    }

    public List<CambioDeEstado> getCambiosDeEstado() {
        return cambiosDeEstado;
    }

    public EstacionSismologica getEstacion() {
        return estacionSismologica;
    }

    public LocalDateTime getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setEstacion(EstacionSismologica estacion) {
        this.estacionSismologica = estacion;
    }

    public void setCambiosDeEstado(List<CambioDeEstado> cambiosDeEstado) {
        this.cambiosDeEstado = cambiosDeEstado;
    }

    /*
     * // Métodos de la realización de caso de uso
     * public String getIdentificadorSismografo() {
     * return identificadorSismografo;
     * }
     * 
     * public void fueraDeServicio(Estado estado, Map<MotivoTipo, String>
     * motivosYComentarios) {
     * CambioDeEstado actual = null;
     * for (CambioDeEstado cambio : cambiosDeEstado) {
     * if (cambio.esEstadoActual()) {
     * actual = cambio;
     * break;
     * }
     * }
     * 
     * if (actual != null) {
     * actual.setFechaHoraFin(LocalDateTime.now());
     * }
     * 
     * ejecutarCambioDeEstado(estado, motivosYComentarios);
     * }
     * 
     * public void ejecutarCambioDeEstado(Estado estado, Map<MotivoTipo, String>
     * motivosYComentarios) {
     * CambioDeEstado nuevo = new CambioDeEstado(estado, LocalDateTime.now());
     * nuevo.crearMotivoFueraServicio(motivosYComentarios);
     * }
     * 
     * // agregarCambioEstado está de más
     * 
     * // Métodos extra (no se utilizan, pero los implementamos por si acaso)
     * public List<CambioDeEstado> getCambiosDeEstado() {
     * return cambiosDeEstado;
     * }
     * 
     * public EstacionSismologica getEstacion() {
     * return estacionSismologica;
     * }
     * 
     * public LocalDateTime getFechaAdquisicion() {
     * return fechaAdquisicion;
     * }
     * 
     * public String getNroSerie() {
     * return nroSerie;
     * }
     * 
     * public void setEstacion(EstacionSismologica estacion) {
     * this.estacionSismologica = estacion;
     * }
     * 
     * public void setCambiosDeEstado(List<CambioDeEstado> cambiosDeEstado) {
     * this.cambiosDeEstado = cambiosDeEstado;
     * }
     */
}
