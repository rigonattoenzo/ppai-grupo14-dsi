package boundary;

import gestor.GestorCierreInspeccion;
// import model.*; --> creo que no hace falta

public class PantallaInspeccion {
    private GestorCierreInspeccion gestor;

    // Atributos (componentes de la interfaz), faltan agregar más
    private Boton botonSeleccionOrdenInspeccion;
    private CampoTexto campoObservacionCierreOrden;
    private ComboBox comboMotivoFueraServicio;
    private CampoTexto campoComentario;
    private Boton botonConfirmacionCierreOrden;

    // Constructor
    /* public PantallaInspeccion() {
        // Ver que hacer con este y opcionCerrarOrdenDeInspeccion
    } */

    // Métodos de la interfaz (boundary)
    public void opcionCerrarOrdenDeInspeccion() {
        // Método vacío -> Aca iría el constructor en realidad
    }

    public void habilitarVentana() {
        // this.gestor = new GestorCierreInspeccion(this);
        // Método vacío -> aca se inicializa el gestor
    }

    public void mostrarOrdCompRealizadas() {
        // Método vacío
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

    /*// Clases auxiliares simuladas para que compile (podés reemplazarlas por las reales)
    private class Boton {}
    private class CampoTexto {}
    private class ComboBox {}*/

}
