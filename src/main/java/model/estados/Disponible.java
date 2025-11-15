package model.estados;

import model.Sismografo;

/**
 * Estado: Disponible - Sismografo adquirido, listo para instalación.
 */
public class Disponible extends Estado {

    public Disponible() {
        super("Disponible", "Sismografo");
    }

    @Override
    public void enEspera(Sismografo sismografo) {
        System.out.println("Transición: Disponible -> En Espera de Certificación");
        sismografo.setEstadoActual(new EnEsperaCertificacion());
    }

    @Override
    public void incluirEnPlanConstruccion(Sismografo sismografo) {
        System.out.println("Transición: Disponible -> Incluido en Plan de Construcción");
        sismografo.setEstadoActual(new IncluidoEnPlanConstruccion());
    }

    @Override
    public String toString() {
        return "Estado: Disponible - Sismografo adquirido, listo para instalación";
    }
}
