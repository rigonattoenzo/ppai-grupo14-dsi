package model.estados;

/**
 * Estado: De Baja - El sismografo ya no está en servicio (final).
 * No tiene transiciones posibles (estado terminal).
 */
public class DeBaja extends Estado {

    public DeBaja() {
        super("De Baja", "Sismografo");
    }

    @Override
    public String toString() {
        return "Estado: De Baja - Sismografo descontinuado (Terminal)";
    }
}
