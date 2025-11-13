package model.estados;

/**
 * Estado: Cerrada
 * La orden de inspección está cerrada definitivamente.
 * No se pueden realizar más cambios.
 */
public class Cerrada extends Estado {

    public Cerrada() {
        super("Cerrada", "Orden de Inspeccion");
    }

    @Override
    public String toString() {
        return "Estado: Cerrada - Orden finalizada y cerrada";
    }
}
