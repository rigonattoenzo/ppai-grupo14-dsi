package model.estados;

import model.Sismografo;

/**
 * Estado: En Instalación - Sismografo siendo instalado en la estación.
 */
public class EnInstalacion extends Estado {

    public EnInstalacion() {
        super("En Instalación", "Sismografo");
    }

    @Override
    public void enLinea(Sismografo sismografo) {
        System.out.println("Transición: En Instalación -> En Línea");
        sismografo.setEstadoActual(new EnLinea());
    }

    @Override
    public void reclamar(Sismografo sismografo) {
        System.out.println("Transición: En Instalación -> Reclamado (Falla detectada)");
        sismografo.setEstadoActual(new Reclamado());
    }

    @Override
    public String toString() {
        return "Estado: En Instalación";
    }
}
