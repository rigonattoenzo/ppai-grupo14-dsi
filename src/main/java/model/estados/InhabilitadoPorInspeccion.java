package model.estados;

import model.Sismografo;
import model.CambioDeEstado;
import model.Empleado;
import model.MotivoTipo;
import model.Sesion;
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

    @Override
    public void fueraServicio(Sismografo sismografo, LocalDateTime fechaActual,
            CambioDeEstado[] cambiosEstado,
            List<Map<String, Object>> motivos) {
        System.out.println(">>> InhabilitadoPorInspeccion.fueraServicio - sismografo: "
                + (sismografo == null ? "NULL" : sismografo.getIdentificadorSismografo()));
        System.out.println("    cambiosEstado.length=" + (cambiosEstado == null ? 0 : cambiosEstado.length)
                + " ; motivos.size=" + (motivos == null ? 0 : motivos.size()));
        finalizarEstadoActual(sismografo, cambiosEstado);
        ejecutarCambioEstado(sismografo, fechaActual, cambiosEstado, motivos);
        crearEstado(sismografo);
    }

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

    private void ejecutarCambioEstado(Sismografo sismografo,
            LocalDateTime fechaActual,
            CambioDeEstado[] cambiosEstado,
            List<Map<String, Object>> motivos) {
        List<CambioDeEstado> listaCambios = sismografo.getCambiosDeEstado();
        System.out.println("    ejecutarCambioEstado - listaCambios before.size="
                + (listaCambios == null ? 0 : listaCambios.size()));

        FueraServicio estadoFueraServicio = new FueraServicio();

        // 🔑 Obtener el empleado logueado actual
        Empleado empleadoActual = Sesion.getInstancia().getUsuario().getEmpleado();

        CambioDeEstado nuevo = new CambioDeEstado(estadoFueraServicio, fechaActual, sismografo, empleadoActual);

        // 🔑 BUSCAR MotivoTipos en BD, no crearlos nuevos
        Map<MotivoTipo, String> motivosMap = convertirMotivosDesdeDescripciones(motivos);
        System.out.println("    motivosMap creado con size=" + (motivosMap == null ? 0 : motivosMap.size()));

        nuevo.crearMotivoFueraServicio(motivosMap);
        listaCambios.add(nuevo);
        System.out.println("    ejecutarCambioEstado - nuevo CambioDeEstado añadido. listaCambios after.size="
                + listaCambios.size());
    }

    private void crearEstado(Sismografo sismografo) {
        FueraServicio nuevoEstado = new FueraServicio();
        sismografo.setEstadoActual(nuevoEstado);
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
