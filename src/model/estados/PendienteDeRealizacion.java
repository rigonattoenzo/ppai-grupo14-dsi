package model.estados;

import model.OrdenDeInspeccion;

/**
 * Estado: Pendiente De Realización
 * No se ha registrado ninguna actividad aún.
 * Puede transicionar a: CompletamenteRealizada (si se registran todas)
 *                      ParcialmenteRealizada (si se registran algunas)
 */
public class PendienteDeRealizacion extends Estado {

    public PendienteDeRealizacion() {
        super("Pendiente De Realización", "Orden de Inspeccion");
    }

    /**
     * Transición: Pendiente → Parcialmente Realizada
     * Se ejecuta cuando se registra el primer resultado.
     */
    @Override
    public void registrarPrimera(Object orden) {
        if (orden instanceof OrdenDeInspeccion) {
            OrdenDeInspeccion oi = (OrdenDeInspeccion) orden;
            // System.out.println("Transición: Pendiente De Realización → Parcialmente Realizada");
            oi.setEstadoActual(new ParcialmenteRealizada());
        }
    }

    /**
     * Transición: Pendiente → CompletamenteRealizada
     * Se ejecuta cuando se registran todos los resultados
     */
    @Override
    public void completarTodas(Object orden) {
        if (orden instanceof OrdenDeInspeccion) {
            OrdenDeInspeccion oi = (OrdenDeInspeccion) orden;
            // System.out.println("✓ Transición: PendienteDeRealizacion → CompletamenteRealizada");
            oi.setEstadoActual(new CompletamenteRealizada());
        }
    }

    @Override
    public String toString() {
        return "Estado: Pendiente de Realización - Sin resultados registrados";
    }
}
