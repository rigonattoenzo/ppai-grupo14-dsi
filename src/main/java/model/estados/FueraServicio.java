package model.estados;

import model.Sismografo;

/**
 * Estado: Fuera de Servicio - El sismografo requiere reparación.
 * Transiciones posibles:
 * - A En Línea (después de completar la reparación)
 * - A De Baja (si es irreparable)
 */
public class FueraServicio extends Estado {

    public FueraServicio() {
        super("Fuera De Servicio", "Sismografo");
    }

    /**
     * Transición: Fuera de Servicio -> En Línea
     * Se ejecuta cuando se completa la reparación.
     */
    @Override
    public void enLinea(Sismografo sismografo) {
        System.out.println("Transición: Fuera de Servicio -> En Línea (Reparación completada)");
        sismografo.setEstadoActual(new EnLinea());
    }

    /**
     * Transición: Fuera de Servicio -> De Baja
     * Se ejecuta si el sismografo es irreparable.
     */
    @Override
    public void darDeBaja(Sismografo sismografo) {
        System.out.println("Transición: Fuera de Servicio -> De Baja (Irreparable)");
        sismografo.setEstadoActual(new DeBaja());
    }

    @Override
    public String toString() {
        return "Estado: Fuera de Servicio - Requiere reparación técnica";
    }
}
