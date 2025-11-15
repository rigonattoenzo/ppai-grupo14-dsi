package model.estados;

import model.Sismografo;

/**
 * Estado: En Línea - El sismografo está operativo y transmitiendo datos.
 * Transiciones posibles:
 * - A Inhabilitado por Inspección (inhabilitar)
 */
public class EnLinea extends Estado {

    public EnLinea() {
        super("En Línea", "Sismografo");
    }

    @Override
    public void inhabilitar(Sismografo sismografo) {
        System.out.println("Transición: En Línea -> Inhabilitado por Inspección");
        sismografo.setEstadoActual(new InhabilitadoPorInspeccion());
    }

    @Override
    public String toString() {
        return "Estado: En Línea - El sismografo está operativo";
    }
}
