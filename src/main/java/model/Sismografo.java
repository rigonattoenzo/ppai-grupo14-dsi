package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.estados.Estado;
import model.estados.Disponible;
import model.estados.EnLinea;

/**
 * Representa un Sismógrafo.
 * Implementa el patrón State delegando transiciones al estado actual.
 */
@Entity
@Table(name = "sismografo")
public class Sismografo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_sismografo", unique = true, nullable = false, length = 100)
    private String identificadorSismografo;

    @Column(name = "numero_serie", length = 100)
    private String nroSerie;

    @Column(name = "fecha_adquisicion")
    private LocalDateTime fechaAdquisicion;

    // Estación donde está instalado
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estacion_codigo", nullable = false, unique = true)
    private EstacionSismologica estacionSismologica;

    // Historial de cambios de estado
    @OneToMany(mappedBy = "sismografo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CambioDeEstado> cambiosDeEstado = new ArrayList<>();

    @Column(name = "estado")
    private String estado; // <-- Este campo se guarda en la BD

    // Estado actual -> Patrón State
    @Transient
    private Estado estadoActual;

    // Constructor -> Equivalente al new()
    public Sismografo() {
        if (estado == null) {
            this.estadoActual = new Disponible();
            this.estado = "Disponible";
        } else {
            this.estadoActual = Estado.fromString(estado);
        }
    }

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
        this.estado = "EnLinea"; // Para persistencia
    }

    @PostLoad
    private void postLoad() {
        // Reconstruir el estado transient a partir del String persistido
        this.estadoActual = Estado.fromString(this.estado);
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
    public void fueraDeServicio(LocalDateTime fechaActual, List<Map<String, Object>> motivos) {
        try {
            CambioDeEstado[] cambiosArray = this.cambiosDeEstado.toArray(new CambioDeEstado[0]);
            this.estadoActual.fueraServicio(this, fechaActual, cambiosArray, motivos);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
        this.estadoActual = nuevoEstado;
        this.estado = nuevoEstado.getClass().getSimpleName();
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
     * Verifica si este sismografo pertenece a una estación específica
     */
    public boolean esTuEstacionSismologica(String codEstacion) {
        return this.estacionSismologica != null
                && this.estacionSismologica.getCodigoEstacion().equals(codEstacion);
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
}
