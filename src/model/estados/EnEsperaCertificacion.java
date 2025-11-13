package model.estados;

import model.Sismografo;

/**
 * Estado: En Espera de Certificación - Sismografo en proceso de certificación.
 */
public class EnEsperaCertificacion extends Estado {

    public EnEsperaCertificacion() {
        super("En Espera Certificación", "Sismografo");
    }

    @Override
    public void recibirCertificacion(Sismografo sismografo) {
        System.out.println("Transición: En Espera Certificación -> Habilitado para Construcción");
        sismografo.setEstadoActual(new HabilitadoParaConstruccion());
    }

    @Override
    public String toString() {
        return "Estado: En Espera de Certificación";
    }
}
