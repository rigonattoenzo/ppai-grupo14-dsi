package model.estados;

import model.Sismografo;

/**
 * Estado: Incluido en Plan de Construcción - Sismografo en plan de obra.
 */
public class IncluidoEnPlanConstruccion extends Estado {

    public IncluidoEnPlanConstruccion() {
        super("Incluido En Plan Construcción", "Sismografo");
    }

    @Override
    public void enInstalacion(Sismografo sismografo) {
        System.out.println("Transición: Incluido en Plan -> En Instalación");
        sismografo.setEstadoActual(new EnInstalacion());
    }

    @Override
    public void disponibilizar(Sismografo sismografo) {
        System.out.println("Transición: Incluido en Plan -> Disponible");
        sismografo.setEstadoActual(new Disponible());
    }

    @Override
    public String toString() {
        return "Estado: Incluido en Plan de Construcción";
    }
}
