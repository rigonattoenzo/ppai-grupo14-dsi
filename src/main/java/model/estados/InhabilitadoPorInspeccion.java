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
                if (c.esEstadoActual()) {
                    // 16) ❗❗
                    c.setFechaHoraFin(fechaActual);
                    c.setEmpleado(empleadoActual);

                    // Con esto se persisten los datos del cambio de estado anterior
                    RepositorioDatos.guardarCambioDeEstado(c);

                    break;
                }
            }
        }

        // 17) ❗❗
        ejecutarCambioEstado(sismografo, fechaActual, motivos);
    }

    private void ejecutarCambioEstado(Sismografo sismografo, LocalDateTime fechaActual,
            List<Map<String, Object>> motivos) {

        // 18) ❗❗
        FueraDeServicio estadoFueraServicio = new FueraDeServicio();
        // 19) ❗❗
        CambioDeEstado nuevo = new CambioDeEstado(estadoFueraServicio, fechaActual, sismografo);

        Map<MotivoTipo, String> motivosMap = convertirMotivosDesdeDescripciones(motivos);

        // 20) ❗❗
        nuevo.crearMotivoFueraServicio(motivosMap);

        RepositorioDatos.guardarCambioDeEstado(nuevo);
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

        if (motivos != null && !motivos.isEmpty()) {
            for (Map<String, Object> motivoData : motivos) {
                // INTENTAR OBTENER COMO OBJETO PRIMERO
                Object tipoObj = motivoData.get("tipo");
                MotivoTipo tipo = null;

                if (tipoObj instanceof MotivoTipo) {
                    // YA ES UN MotivoTipo
                    tipo = (MotivoTipo) tipoObj;
                } else if (tipoObj instanceof String) {
                    // ES UN String (descripción), buscar en BD
                    String descripcion = (String) tipoObj;
                    List<MotivoTipo> motivosTiposEnBD = RepositorioDatos.obtenerMotivos();
                    tipo = buscarMotivoPorDescripcion(descripcion, motivosTiposEnBD);
                }

                String comentario = (String) motivoData.get("comentario");

                if (tipo != null && comentario != null) {
                    resultado.put(tipo, comentario);
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

    /*
     * private String extraerDescripcion(Map<String, Object> mapa) {
     * Object valor = mapa.get("tipo");
     * if (valor == null)
     * valor = mapa.get("descripcion");
     * if (valor == null)
     * valor = mapa.get("motivo");
     * if (valor == null)
     * valor = mapa.get("description");
     * 
     * return valor != null ? valor.toString() : null;
     * }
     * 
     * private String extraerComentario(Map<String, Object> mapa) {
     * Object valor = mapa.get("comentario");
     * if (valor == null)
     * valor = mapa.get("comment");
     * if (valor == null)
     * valor = mapa.get("detalle");
     * if (valor == null)
     * valor = mapa.get("detail");
     * 
     * return valor!=null?valor.toString():"";
     * 
     * }
     */

    @Override
    public String toString() {
        return "Estado: Inhabilitado por Inspección - Sismografo bajo revisión técnica";
    }
}
