package gestor;

import boundary.PantallaInspeccion;
import model.*;

// Imports necesarios
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Comparator;

public class GestorCierreInspeccion {
    private PantallaInspeccion pantalla;

    // Atributos, seguro faltan más
    private Empleado empleadoLogueado;
    private List<OrdenDeInspeccion> ordenesDeInspeccion;
    private String observacionCierreOrden;
    private List<MotivoFueraDeServicio> motivosFueraServicio;
    private Map<MotivoFueraDeServicio, String> comentarioMotivo;
    private Estado estadoCerrado;
    private LocalDateTime fechaHoraActual;
    private Empleado responsableReparacion;
    private String mailResponsable;
    private String correoAEnviar;

    private List<Map<String, Object>> ordenesFiltradasConDatos;

    // Atributos referenciales
    // Referencias a entidades
    private Sesion sesion;

    /*
    // Referencias a boundary auxiliares
    private InterfazNotificacionMail interfazMail;
    private MonitorCCRS monitorCCRS;
    */

    // Métodos
    public void setOrdenesDeInspeccion(List<OrdenDeInspeccion> ordenes) {
        this.ordenesDeInspeccion = ordenes;
    }

    public List<Map<String,Object>> getOrdenesFiltradasConDatos() {
        return ordenesFiltradasConDatos;
    }

    public GestorCierreInspeccion(PantallaInspeccion pantalla) {
        this.pantalla = pantalla;
        // Falta inicializar el resto de los atributos
    } // Constructor

    public void iniciarCierreOrdenInspeccion() {
        obtenerEmpleadoLogueado();
    }

    public void obtenerEmpleadoLogueado() {
        // Ver de justificar este método agregándolo en el diagrama de secuencia (el getInstancia())
        this.empleadoLogueado = Sesion.getInstancia().getUsuario().getRiLogueado();

        // System.out.println(this.empleadoLogueado);

        buscarOrdenesDeInspeccionDeRI();
    }

    public void buscarOrdenesDeInspeccionDeRI() {
        Empleado empleado = this.empleadoLogueado;

        // Lista de mapas con los datos de las órdenes filtradas
        List<Map<String, Object>> ordenesFiltradas = new ArrayList<>();

        for (OrdenDeInspeccion orden : ordenesDeInspeccion) {
            if (orden.sosDeEmpleado(empleado) && orden.sosCompletamenteRealizada()) {
                // Obtenemos el mapa con los datos
                Map<String, Object> datosOrden = orden.obtenerDatosOI();
                ordenesFiltradas.add(datosOrden);
            }
        }

        // Guardar las órdenes filtradas con datos
        this.ordenesFiltradasConDatos = ordenesFiltradas; // crea esta variable en la clase

        ordenarPorFechaDeFinalizacion();
        pantalla.mostrarOrdCompRealizadas();
        pedirSelecOrdenInspeccion();
        // System.out.println("Órdenes válidas encontradas: " + ordenesFiltradas.size());
    }

    public void ordenarPorFechaDeFinalizacion() {
        if (ordenesFiltradasConDatos == null) {
            System.out.println("No hay órdenes para ordenar");
            return;
        }

        ordenesFiltradasConDatos.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                // Suponiendo que las fechas son Date
                LocalDateTime fecha1 = (LocalDateTime) o1.get("fechaFinalizacion");
                LocalDateTime fecha2 = (LocalDateTime) o2.get("fechaFinalizacion");

                if (fecha1 == null && fecha2 == null) return 0;
                if (fecha1 == null) return -1;
                if (fecha2 == null) return 1;

                return fecha1.compareTo(fecha2);
            }
        });

        // System.out.println("Órdenes ordenadas por fecha de finalización.");
    }

    public void pedirSelecOrdenInspeccion() {
        // Método vacío
    }

    public void tomarOrdenInspeccionSelec(OrdenDeInspeccion orden) {
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

    public void tomarMotivoTipoFueraServicio(MotivoFueraDeServicio motivo) {
        // Método vacío
    }

    public void tomarComentario(MotivoFueraDeServicio motivo, String comentario) {
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
