package model.estados;

import model.Sismografo;

/**
 * Estado: Habilitado para Construcción - Aprobado, listo para iniciar
 * instalación.
 */
public class HabilitadoParaConstruccion extends Estado {

    public HabilitadoParaConstruccion() {
        super("Habilitado Para Construcción", "Sismografo");
    }

    @Override
    public void iniciarPlanConstruccion(Sismografo sismografo) {
        System.out.println("Transición: Habilitado -> En Instalación");
        sismografo.setEstadoActual(new EnInstalacion());
    }

    @Override
    public String toString() {
        return "Estado: Habilitado para Construcción";
    }
}
