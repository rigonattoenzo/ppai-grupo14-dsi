package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representa una Estación Sismológica.
 */
public class EstacionSismologica {
    private String codigoEstacion;
    private String documentoCertificacionAdq;
    private LocalDateTime fechaSolicitudCertificacion;
    private double latitud;
    private double longitud;
    private String nombre;
    private String nroCertificacionAdquisicion;

    // Asociación // ***
    private Sismografo sismografo;

    // Constructor
    public EstacionSismologica(String codigoEstacion,
                               String documentoCertificacionAdq,
                               LocalDateTime fechaSolicitudCertificacion,
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

    // Métodos de la realización de caso de uso
    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String obtenerIdentificadorSismografo() {
        return sismografo.getIdentificadorSismografo();
    }

    public void ponerSismografoFueraServicio(Estado estado, Map<MotivoTipo, String> motivosYComentarios){
        this.sismografo.fueraDeServicio(estado, motivosYComentarios);
    }

    // Métodos extra (no se utilizan, pero los implementamos por si acaso)
    public Sismografo getSismografo() {
        return sismografo;
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

    public void setSismografo(Sismografo sismografo) {
        this.sismografo = sismografo;
    }

    /*
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
    } */
}
