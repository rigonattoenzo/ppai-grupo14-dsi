package boundary;

// Import del gestor y los modelos
import gestor.GestorCierreInspeccion;
import model.*;

// import de utilidades de Java
import java.util.Map;
import java.util.List;

public class PantallaInspeccion {
    // Gestor
    private GestorCierreInspeccion gestor;

    // Atributos (componentes de la interfaz) --> faltan agregar más
    private Boton botonSeleccionOrdenInspeccion;
    private CampoTexto campoObservacionCierreOrden;
    private ComboBox comboMotivoFueraServicio;
    private CampoTexto campoComentario;
    private Boton botonConfirmacionCierreOrden;

    // Métodos
    // Constructor
    public PantallaInspeccion() {
        // Inicializar todos los atributos, para la interfaz gráfica
    }

    public GestorCierreInspeccion getGestor() {
        return gestor;
    }

    // Métodos del caso de uso 37
    public void opcionCerrarOrdenDeInspeccion() {
        habilitarVentana();
    }

    public void habilitarVentana() {
        this.gestor = new GestorCierreInspeccion(this); // Se crea el gestor
        gestor.iniciarCierreOrdenInspeccion();          // comienza el flujo del caso de uso
    }

    public void mostrarOrdCompRealizadas() {
        System.out.println("Órdenes Completamente Realizadas del Empleado:");

        for (Map<String, Object> datosOrden : gestor.getOrdenesFiltradasConDatos()) {
            System.out.println(datosOrden);
        }
    }

    public void pedirSelecOrdenInspeccion() {
        // Método vacío
    }

    public void tomarOrdenInspeccionSelec() {
        // Método vacío
    }

    public void pedirObservacionCierreOrden() {
        // Método vacío
    }

    public void tomarObservacionCierreOrden() {
        // Método vacío
    }

    public void mostrarMotivosTipoFueraServicio() {
        // Método vacío
    }

    public void pedirSelecMotivoTipo() {
        // Método vacío
    }

    public void tomarMotivoTipoFueraServicio() {
        // Método vacío
    }

    public void pedirComentario() {
        // Método vacío
    }

    public void tomarComentario() {
        // Método vacío
    }

    public void tomarConfirmacionCierreOrden() {
        // Método vacío
    }


    // Clases auxiliares simuladas para que compile (después se reeplazan por las reales)
    private class Boton {}
    private class CampoTexto {}
    private class ComboBox {}

}
