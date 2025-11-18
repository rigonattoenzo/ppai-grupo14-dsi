package model.estados;

import model.Sismografo;
import model.CambioDeEstado;
import model.Empleado;
import model.MotivoTipo;
import datos.RepositorioDatos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InhabilitadoPorInspeccion extends Estado {

    public InhabilitadoPorInspeccion() {
        super("Inhabilitado Por Inspección", "Sismografo");
    }

    @Override
    public void enLinea(Sismografo sismografo) {
        System.out.println("Transición: Inhabilitado por Inspección -> En Línea");
        sismografo.setEstadoActual(new EnLinea());
    }

    @Override // 14) ❗❗
    public void fueraServicio(Sismografo sismografo, LocalDateTime fechaActual,
            CambioDeEstado[] cambiosEstado, List<Map<String, Object>> motivos, Empleado empleadoActual) {

        if (cambiosEstado != null && cambiosEstado.length > 0) {
            // 15) ❗❗
            for (CambioDeEstado c : cambiosEstado) {
                if (esEstadoActual(c)) {
                    // 16) ❗❗
                    c.setFechaHoraFin(fechaActual);
                    // Con esto se persiste la fecha hora fin
                    RepositorioDatos.guardarCambioDeEstado(c);
                    System.out.println("✓ Cambio de estado anterior cerrado con fecha: " + fechaActual);

                    break;
                }
            }
        }

        // 17) ❗❗
        ejecutarCambioEstado(sismografo, fechaActual, motivos, empleadoActual);
    }

    private Boolean esEstadoActual(CambioDeEstado cambio) {
        return (cambio != null && cambio.getFechaHoraFin() == null);
    }

    private void ejecutarCambioEstado(Sismografo sismografo, LocalDateTime fechaActual,
            List<Map<String, Object>> motivos, Empleado empleadoActual) {

        // 18) ❗❗
        FueraServicio estadoFueraServicio = new FueraServicio();
        // 19) ❗❗
        CambioDeEstado nuevo = new CambioDeEstado(estadoFueraServicio, fechaActual, sismografo, empleadoActual);

        Map<MotivoTipo, String> motivosMap = convertirMotivosDesdeDescripciones(motivos);
        // 20) ❗❗
        nuevo.crearMotivoFueraServicio(motivosMap);

        // Actualizar estado
        sismografo.setEstadoActual(estadoFueraServicio); // 22) ❗❗
        sismografo.setCambioEstado(nuevo); // 23) ❗❗ --> Vuelve al gestor
    }

    /**
     * Convierte List<Map<String, Object>> a Map<MotivoTipo, String>
     * CLAVE: Busca los MotivoTipo en BD por descripción
     */
    private Map<MotivoTipo, String> convertirMotivosDesdeDescripciones(List<Map<String, Object>> motivos) {
        Map<MotivoTipo, String> resultado = new HashMap<>();

        // Obtener TODOS los MotivoTipo de BD
        List<MotivoTipo> motivosTiposEnBD = RepositorioDatos.obtenerMotivos();

        if (motivos != null) {
            for (Map<String, Object> mapa : motivos) {
                String descripcion = extraerDescripcion(mapa);
                String comentario = extraerComentario(mapa);

                if (descripcion != null && !descripcion.isEmpty()) {
                    // 🔑 BUSCAR en BD, no crear nuevo
                    MotivoTipo tipoEnBD = buscarMotivoPorDescripcion(descripcion, motivosTiposEnBD);

                    if (tipoEnBD != null) {
                        resultado.put(tipoEnBD, comentario != null ? comentario : "");
                        System.out.println("    ✓ MotivoTipo encontrado en BD: " + descripcion);
                    } else {
                        System.err.println("    ❌ MotivoTipo NO encontrado en BD: " + descripcion);
                    }
                }
            }
        }

        return resultado;
    }

    /**
     * Busca un MotivoTipo en la lista por descripción
     */
    private MotivoTipo buscarMotivoPorDescripcion(String descripcion, List<MotivoTipo> motivosTipos) {
        for (MotivoTipo tipo : motivosTipos) {
            if (tipo.getDescripcion().equalsIgnoreCase(descripcion)) {
                return tipo;
            }
        }
        return null;
    }

    private String extraerDescripcion(Map<String, Object> mapa) {
        Object valor = mapa.get("tipo");
        if (valor == null)
            valor = mapa.get("descripcion");
        if (valor == null)
            valor = mapa.get("motivo");
        if (valor == null)
            valor = mapa.get("description");

        return valor != null ? valor.toString() : null;
    }

    private String extraerComentario(Map<String, Object> mapa) {
        Object valor = mapa.get("comentario");
        if (valor == null)
            valor = mapa.get("comment");
        if (valor == null)
            valor = mapa.get("detalle");
        if (valor == null)
            valor = mapa.get("detail");

        return valor != null ? valor.toString() : "";
    }

    @Override
    public String toString() {
        return "Estado: Inhabilitado por Inspección - Sismografo bajo revisión técnica";
    }
}
