package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

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

    // Asociación
    @OneToOne(mappedBy = "estacionSismologica", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Sismografo sismografo;

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
    /**
     * Pone el sismografo fuera de servicio con motivos.
     * Parámetros ajustados para el patrón State:
     * - fechaActual: fecha/hora del cambio
     * - motivos: matriz de [descripcion, comentario]
     */
    public void fueraDeServicio(LocalDateTime fechaActual,
            List<Map<String, Object>> motivos) {
        // System.out.println("EstacionSismologica: Llamando a
        // sismografo.fueraDeServicio()");
        this.sismografo.fueraDeServicio(fechaActual, motivos);
    }

    /*
     * public void ponerSismografoFueraServicio(Estado estado, Map<MotivoTipo,
     * String> motivosYComentarios){
     * this.sismografo.fueraDeServicio(estado, motivosYComentarios);
     * }
     */

    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String obtenerIdentificadorSismografo() {
        return sismografo.getIdentificadorSismografo();
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
