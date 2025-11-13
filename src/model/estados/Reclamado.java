package model.estados;

import model.Sismografo;

/**
 * Estado: Reclamado - Sismografo con falla, en reclamo al proveedor.
 */
public class Reclamado extends Estado {

    public Reclamado() {
        super("Reclamado", "Sismografo");
    }

    @Override
    public void enInstalacion(Sismografo sismografo) {
        System.out.println("Transición: Reclamado -> En Instalación (Reparado por proveedor)");
        sismografo.setEstadoActual(new EnInstalacion());
    }

    @Override
    public void darDeBaja(Sismografo sismografo) {
        System.out.println("Transición: Reclamado -> De Baja (Irreparable)");
        sismografo.setEstadoActual(new DeBaja());
    }

    @Override
    public String toString() {
        return "Estado: Reclamado - En revisión por proveedor";
    }
}
