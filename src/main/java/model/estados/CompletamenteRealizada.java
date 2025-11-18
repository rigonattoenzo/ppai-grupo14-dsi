package model.estados;

import model.OrdenDeInspeccion;

/**
 * Estado: Completamente Realizada
 * Se han registrado todos los resultados, pendiente de cierre definitivo.
 */
public class CompletamenteRealizada extends Estado {

    public CompletamenteRealizada() {
        super("Completamente Realizada", "Orden de Inspeccion");
    }

    /**
     * Transición: Completamente Realizada → Cerrada
     * Se ejecuta cuando se cierra definitivamente la orden.
     */
    @Override // 10) ❗❗
    public void cerrar(Object orden) {
        if (orden instanceof OrdenDeInspeccion) {
            OrdenDeInspeccion oi = (OrdenDeInspeccion) orden;
            System.out.println("Transición: Completamente Realizada → Cerrada");
            // 11) ❗❗
            oi.setEstadoActual(new Cerrada());
        }
    }

    @Override
    public String toString() {
        return "Estado: Completamente Realizada - Todos los resultados registrados";
    }
}
