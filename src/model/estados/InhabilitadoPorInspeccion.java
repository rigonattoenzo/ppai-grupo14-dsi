package model.estados;

import model.Sismografo;
import model.CambioDeEstado;
import model.MotivoTipo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Estado: Inhabilitado por Inspección - El sismografo está bajo inspección.
 * Este es el estado clave donde ocurre la transición a "Fuera de Servicio".
 * 
 * Transiciones posibles:
 * - A En Línea (si la inspección es satisfactoria)
 * - A Fuera de Servicio (si se detectan problemas)
 */
public class InhabilitadoPorInspeccion extends Estado {

    public InhabilitadoPorInspeccion() {
        super("Inhabilitado Por Inspección", "Sismografo");
    }

    /**
     * Transición: Inhabilitado por Inspección -> En Línea
     */
    @Override
    public void enLinea(Sismografo sismografo) {
        System.out.println("Transición: Inhabilitado por Inspección -> En Línea");
        sismografo.setEstadoActual(new EnLinea());
    }

    /**
     * Transición: Inhabilitado por Inspección -> Fuera de Servicio
     * Parámetro: List<Map<String, Object>>
     */
    @Override
    public void fueraServicio(Sismografo sismografo, LocalDateTime fechaActual,
                               CambioDeEstado[] cambiosEstado,
                               List<Map<String, Object>> motivos) {
        finalizarEstadoActual(sismografo, cambiosEstado);
        ejecutarCambioEstado(sismografo, fechaActual, cambiosEstado, motivos);
        crearEstado(sismografo);
    }

    /**
     * Finaliza el estado actual buscando el CambioDeEstado que no tiene fecha de fin.
     */
    private void finalizarEstadoActual(Sismografo sismografo, 
                                       CambioDeEstado[] cambiosEstado) {
        for (CambioDeEstado cambio : cambiosEstado) {
            if (cambio != null && cambio.getFechaHoraFin() == null) {
                cambio.setFechaHoraFin(LocalDateTime.now());
                System.out.println("Estado anterior finalizado: " + 
                                 cambio.getEstado().getNombreEstado());
                break;
            }
        }
    }

    /**
     * Ejecuta el cambio de estado creando un nuevo CambioDeEstado.
     */
    private void ejecutarCambioEstado(Sismografo sismografo, 
                                      LocalDateTime fechaActual,
                                      CambioDeEstado[] cambiosEstado, 
                                      List<Map<String, Object>> motivos) {
        List<CambioDeEstado> listaCambios = sismografo.getCambiosDeEstado();

        // Obtener el estado "Fuera de Servicio"
        FueraServicio estadoFueraServicio = new FueraServicio();

        CambioDeEstado nuevo = new CambioDeEstado(estadoFueraServicio, fechaActual);

        // Convertir List<Map<String, Object>> a Map<MotivoTipo, String>
        Map<MotivoTipo, String> motivosMap = convertirMotivos(motivos);

        nuevo.crearMotivoFueraServicio(motivosMap);
        listaCambios.add(nuevo);
    }

    /**
     * Crea el nuevo estado (Fuera de Servicio).
     */
    private void crearEstado(Sismografo sismografo) {
        FueraServicio nuevoEstado = new FueraServicio();
        sismografo.setEstadoActual(nuevoEstado);
    }

    /**
     * Convierte List<Map<String, Object>> a Map<MotivoTipo, String>
     */
    private Map<MotivoTipo, String> convertirMotivos(List<Map<String, Object>> motivos) {
        Map<MotivoTipo, String> resultado = new HashMap<>();

        if (motivos != null) {
            for (Map<String, Object> mapa : motivos) {
                String descripcion = extraerDescripcion(mapa);
                String comentario = extraerComentario(mapa);

                if (descripcion != null && !descripcion.isEmpty()) {
                    MotivoTipo tipo = new MotivoTipo(descripcion);
                    resultado.put(tipo, comentario != null ? comentario : "");
                }
            }
        }

        return resultado;
    }

    /**
     * Extrae la descripción del motivo de un Map flexible.
     */
    private String extraerDescripcion(Map<String, Object> mapa) {
        Object valor = mapa.get("tipo");
        if (valor == null) valor = mapa.get("descripcion");
        if (valor == null) valor = mapa.get("motivo");
        if (valor == null) valor = mapa.get("description");

        return valor != null ? valor.toString() : null;
    }

    /**
     * Extrae el comentario del motivo de un Map flexible.
     */
    private String extraerComentario(Map<String, Object> mapa) {
        Object valor = mapa.get("comentario");
        if (valor == null) valor = mapa.get("comment");
        if (valor == null) valor = mapa.get("detalle");
        if (valor == null) valor = mapa.get("detail");

        return valor != null ? valor.toString() : "";
    }

    @Override
    public String toString() {
        return "Estado: Inhabilitado por Inspección - Sismografo bajo revisión técnica";
    }
}
