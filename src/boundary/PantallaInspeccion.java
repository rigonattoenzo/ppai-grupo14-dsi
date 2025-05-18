package boundary;

import gestor.GestorCierreInspeccion;
import java.util.Map;
import java.util.List;
import model.*;
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
    public PantallaInspeccion() {
        // Inicializar todos los atributos, para la interfaz gráfica
    }

    public GestorCierreInspeccion getGestor() {
        return gestor;
    }

    // Métodos de la interfaz (boundary)
    public void opcionCerrarOrdenDeInspeccion() {
        habilitarVentana();
    }

    public void habilitarVentana() {
        this.gestor = new GestorCierreInspeccion(this); // Se crea el gestor
        gestor.iniciarCierreOrdenInspeccion();          // comienza el flujo del caso de uso
    }

    public void mostrarOrdCompRealizadas() {
        // El gestor ya debe tener la lista completa de órdenes y el empleado logueado seteados
        gestor.buscarOrdenesDeInspeccionDeRI();  // Filtra las órdenes completadas del RI

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

    // Clases auxiliares simuladas para que compile (podés reemplazarlas por las reales)
    private class Boton {}
    private class CampoTexto {}
    private class ComboBox {}

}
