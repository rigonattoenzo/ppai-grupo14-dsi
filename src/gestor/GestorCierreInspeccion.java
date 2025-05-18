package gestor;

import boundary.PantallaInspeccion;
import model.*;

// Imports necesarios
import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;

public class GestorCierreInspeccion {
    private PantallaInspeccion pantalla;

    // Atributos, seguro faltan más
    private Empleado empleadoLogueado;
    private List<OrdenInspeccion> ordenesDeInspeccion;
    private String observacionCierreOrden;
    private List<MotivoFueraServicio> motivosFueraServicio;
    private Map<MotivoFueraServicio, String> comentarioMotivo;
    private EstadoOrden estadoCerrado;
    private LocalDateTime fechaHoraActual;
    private Empleado responsableReparacion;
    private String mailResponsable;
    private String correoAEnviar;

    // Métodos
    public GestorCierreInspeccion(PantallaInspeccion pantalla) {
        // this.pantalla = pantalla;
    } // revisar este

    public void iniciarCierreOrdenInspeccion() {
        // Método vacío -> Se inicializa el gestor
    }

    public void obtenerEmpleadoLogueado() {
        // Método vacío
    }

    public void buscarOrdenesDeInspeccionDeRI() {
        // Método vacío
    }

    public void ordenarPorFechaDeFinalizacion() {
        // Método vacío
    }

    public void tomarOrdenInspeccionSelec(OrdenInspeccion orden) {
        // Método vacío
    }

    public void pedirObservacionCierreOrden() {
        // Método vacío
    }

    public void tomarObservacionCierreOrden(String observacion) {
        // Método vacío
    }

    public void buscarTiposMotivosFueraServicio() {
        // Método vacío
    }

    public void pedirSelecMotivosFueraServicio() {
        // Método vacío
    }

    public void tomarMotivoTipoFueraServicio(MotivoFueraServicio motivo) {
        // Método vacío
    }

    public void tomarComentario(MotivoFueraServicio motivo, String comentario) {
        // Método vacío
    }

    public void pedirConfirmacionCierreOrden() {
        // Método vacío
    }

    public void tomarConfirmacionCierreOrden() {
        // Método vacío
    }

    public void validarExistenciaObservacion() {
        // Método vacío
    }

    public void validarExistenciaMotivoSeleccionado() {
        // Método vacío
    }

    public void buscarEstadoCerrado() {
        // Método vacío
    }

    public void getFechaHoraActual() {
        fechaHoraActual = LocalDateTime.now();
    }

    public void buscarFueraServicio() {
        // Método vacío
    }

    public void cerrarOrdenInspeccion() {
        // Método vacío
    }

    public void ponerSismografoFueraServicio() {
        // Método vacío
    }

    public void buscarResponsableReparacion() {
        // Método vacío
    }

    public void enviarCorreo() {
        // Método vacío
    }

    public void finCU() {
        System.out.println("Caso de uso ejecutado exitosamente.");
    }

    /*
    // Clases auxiliares simuladas (para que compile aunque no existan aún)
    private class Empleado {}
    private class OrdenInspeccion {}
    private class MotivoFueraServicio {}
    private class EstadoOrden {}
    */
}
