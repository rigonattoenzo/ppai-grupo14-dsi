package model.estados;

import model.OrdenDeInspeccion;

/**
 * Estado: Parcialmente Realizada
 * Se han registrado algunos resultados, pero faltan otros.
 */
public class ParcialmenteRealizada extends Estado {

    public ParcialmenteRealizada() {
        super("Parcialmente Realizada", "Orden de Inspeccion");
    }

    /**
     * Transición: Parcialmente → Completamente Realizada
     * Se ejecuta cuando se registran todos los resultados.
     */
    @Override
    public void completarTodas(Object orden) {
        if (orden instanceof OrdenDeInspeccion) {
            OrdenDeInspeccion oi = (OrdenDeInspeccion) orden;
            System.out.println("Transición: Parcialmente Realizada → Completamente Realizada");
            oi.setEstadoActual(new CompletamenteRealizada());
        }
    }

    @Override
    public String toString() {
        return "Estado: Parcialmente Realizada - Algunos resultados registrados";
    }
}
