package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una Estación Sismológica.
 */
public class EstacionSismologica {
    private String codigoEstacion;
    private String documentoCertificacionAdq;
    private LocalDate fechaSolicitudCertificacion;
    private double latitud;
    private double longitud;
    private String nombre;
    private String nroCertificacionAdquisicion;

    // Asociación
    private Sismografo sismografo;

    public EstacionSismologica(String codigoEstacion,
                               String documentoCertificacionAdq,
                               LocalDate fechaSolicitudCertificacion,
                               double latitud,
                               double longitud,
                               String nombre,
                               String nroCertificacionAdquisicion,
                               Sismografo sismografo) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertificacionAdq = documentoCertificacionAdq;
        this.fechaSolicitudCertificacion = fechaSolicitudCertificacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nroCertificacionAdquisicion = nroCertificacionAdquisicion;
        this.sismografo = sismografo;
    }

    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public String getNombre() {
        return nombre;
    }

    public Sismografo getSismografo() {
        return sismografo;
    }

    public String getDocumentoCertificacionAdq() {
        return documentoCertificacionAdq;
    }

    public LocalDate getFechaSolicitudCertificacion() {
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

    // Dentro de EstacionSismologica.java

    // Setter para cambiar el sismógrafo asociado
    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
    }

    // toString para impresión
    @Override
    public String toString() {
        return "EstacionSismologica{" +
                "codigo='" + codigoEstacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", lat=" + latitud +
                ", lon=" + longitud +
                ", sismografo=" + (sismografo != null ? sismografo.getIdentificadorSismografo() : "N/A") +
                '}';
    }
}
