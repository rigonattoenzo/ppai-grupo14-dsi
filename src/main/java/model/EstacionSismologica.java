package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Representa una Estación Sismológica.
@Entity
@Table(name = "estacion_sismologica")
public class EstacionSismologica {
    @Id
    @Column(name = "codigo_estacion", nullable = false, length = 100)
    private String codigoEstacion;

    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "latitud")
    private double latitud;

    @Column(name = "longitud")
    private double longitud;

    @Column(name = "documento_certificacion_adq", length = 255)
    private String documentoCertificacionAdq;

    @Column(name = "fecha_solicitud_certificacion")
    private LocalDateTime fechaSolicitudCertificacion;

    @Column(name = "nro_certificacion_adquisicion", length = 50)
    private String nroCertificacionAdquisicion;

    // Constructor sin parámetros
    public EstacionSismologica() {
    }

    // Constructor
    public EstacionSismologica(String codigoEstacion,
            String documentoCertificacionAdq,
            LocalDateTime fechaSolicitudCertificacion,
            double latitud,
            double longitud,
            String nombre,
            String nroCertificacionAdquisicion) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertificacionAdq = documentoCertificacionAdq;
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
    }

    // ==================== GETTERS ====================
    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumentoCertificacionAdq() {
        return documentoCertificacionAdq;
    }

    public LocalDateTime getFechaSolicitudCertificacion() {
        return fechaSolicitudCertificacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getNroCertificacionAdquisicion() {
        return nroCertificacionAdquisicion;
    }

    // ==================== SETTERS ====================
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /*
     * // toString para impresión
     * 
     * @Override
     * public String toString() {
     * return "EstacionSismologica{" +
     * "codigo='" + codigoEstacion + '\'' +
     * ", nombre='" + nombre + '\'' +
     * ", lat=" + latitud +
     * ", lon=" + longitud +
     * ", sismografo=" + (sismografo != null ?
     * sismografo.getIdentificadorSismografo() : "N/A") +
     * '}';
     * }
     */
}
