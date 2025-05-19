package gestor;

// Import del boundary, modelos y datos
import boundary.PantallaInspeccion;
import datos.RepositorioDatos;
import model.*;

// Import de utilidades de Java
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

public class GestorCierreInspeccion {
    // Pantalla
    private PantallaInspeccion pantalla;

    // Atributos --> Seguro faltan más
    // Atributos locales
    private List<OrdenDeInspeccion> ordenesDeInspeccion;
    private String observacionCierreOrden;
    private List<String> descripciones;
    private Map<String, String> motivosYComentarios = new HashMap<>();
    private MotivoTipo ultimoMotivoSeleccionado = null;

    private String confirmacionCierreOrden;
    private LocalDateTime fechaHoraActual;
    private String mailResponsable;
    private String correoAEnviar;
    private List<Map<String, Object>> ordenesFiltradasConDatos;
    private Map<String,Object> ordenSeleccionada;

    // Atributos referenciales o punteros
    private Empleado empleadoLogueado;
    private Sesion sesion;
    private Empleado responsableReparacion;
    private List<MotivoFueraDeServicio> motivosFueraServicio;
    private Estado estadoCerrado;
    private Estado estadoFueraDeServicio;

    // Atributos referenciales de los boundary auxiliares
    // private InterfazNotificacionMail interfazMail;
    // private MonitorCCRS monitorCCRS;


    // Métodos
    public GestorCierreInspeccion(PantallaInspeccion pantalla) {
        this.pantalla = pantalla;
        this.empleadoLogueado = Sesion.getInstancia().getUsuario().getEmpleado();
        this.ordenesDeInspeccion = RepositorioDatos.obtenerOrdenes();
    } // Constructor

    public void setOrdenesDeInspeccion(List<OrdenDeInspeccion> ordenes) {
        this.ordenesDeInspeccion = ordenes;
    }

    public List<Map<String,Object>> getOrdenesFiltradasConDatos() {
        return ordenesFiltradasConDatos;
    }

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
        pantalla.pedirSelecOrdenInspeccion(this.ordenesFiltradasConDatos);
    }

    public void tomarOrdenInspeccionSelec(Map<String,Object> ordenSeleccionada) {
        this.ordenSeleccionada = ordenSeleccionada;
        pedirObservacionCierreOrden();
    }

    public void pedirObservacionCierreOrden() {
        pantalla.pedirObservacionCierreOrden();
    }

    public void tomarObservacionCierreOrden(String observacion) {
        this.observacionCierreOrden = observacion;
        buscarTiposMotivosFueraServicio();
    }

    public void buscarTiposMotivosFueraServicio() {
        List<MotivoTipo> motivos = RepositorioDatos.getMotivos();  // obtiene todos los MotivoTipo
        List<String> descMotivos = new ArrayList<>();        // lista para guardar solo las descripciones

        for (MotivoTipo motivo : motivos) {
            descMotivos.add(motivo.getDescripcion());        // extrae y agrega cada descripción
        }

        this.descripciones = descMotivos;

        pantalla.mostrarMotivosTipoFueraServicio(descripciones);
        pedirSelecMotivosFueraServicio();
    }

    public void pedirSelecMotivosFueraServicio() { // falta la validación
        int motivoNum = -1;

        while (motivoNum != 0) {
            motivoNum = pantalla.pedirSelecMotivoTipo();

            // si no es 0, pedir comentario y guardar
            if (motivoNum != 0) {
                String motivo = descripciones.get(motivoNum - 1);
                String comentario = pantalla.pedirComentario();
                motivosYComentarios.put(motivo, comentario);
            }
        }

        pantalla.mostrarMensaje("Selección de motivos finalizada.");

        System.out.println("AQQQQQQQQQQQQQQQQQQQQQQQQ");

        pedirConfirmacionCierreOrden();
    }

    public void tomarMotivoTipoFueraServicio(int motivoNum) {
        /*if (motivoNum >= 1 && motivoNum <= descripciones.size()) {
            String motivo = descripciones.get(motivoNum - 1);
            this.ultimoMotivoSeleccionado = motivo;
            // pantalla.mostrarMensaje("Motivo agregado: " + motivo.getDescripcion());
        } else {
            System.out.println("Número fuera de rango.");
        }*/

        // buscarTiposMotivosFueraServicio();
    }

    public void tomarComentario(String comentario) {

    }

    public void pedirConfirmacionCierreOrden() {
        pantalla.pedirConfirmacionCierreOrden();
    }

    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        this.confirmacionCierreOrden = confirmacionCierre;
        validarExistenciaObservacion();
    }

    public void validarExistenciaObservacion() {
        if (observacionCierreOrden != null && !observacionCierreOrden.trim().isEmpty()) {
            System.out.println("Observacion validada exitosamente!");
        } else {
            System.out.println("ERROR! La observacion de cierre es obligatoria para ingresar!!");
        };
        validarExistenciaMotivoSeleccionado();
    }

    public void validarExistenciaMotivoSeleccionado() {
        if (motivosFueraServicio != null && !motivosFueraServicio.isEmpty()) {
            System.out.println("Motivo Seleccionado validado exitosamente!");
        } else {
            System.out.println("ERROR! El motivo se debe seleccionar obligatoriamente");
        }
        buscarEstadoCerrado();
        buscarFueraServicio();
    }

    public void buscarEstadoCerrado() {
        List<Estado> estados = RepositorioDatos.getEstados(); // obtiene todos los estados
        Estado estadoCerrado = null;

        for (Estado estado : estados) {
            if ("CERRADO".equalsIgnoreCase(estado.getNombreEstado())) {
                estadoCerrado = estado;
                break;
            }
        }

        if (estadoCerrado != null) {
            this.estadoCerrado = estadoCerrado; // guarda en un atributo si lo tenés declarado
            pantalla.mostrarEstadoCerrado(estadoCerrado); // muestra en pantalla
        }  else {
            pantalla.mostrarErrorEstadoNoEncontrado("CERRADO");
        }
    }

    public void getFechaHoraActual() {
        fechaHoraActual = LocalDateTime.now();
    }

    public void buscarFueraServicio() {
        List<Estado> estados = RepositorioDatos.getEstados(); // obtiene todos los estados
        Estado estadoFueraDeServicio = null;

        for (Estado estado : estados) {
            if ("FUERA DE SERVICIO".equalsIgnoreCase(estado.getNombreEstado())) {
                estadoFueraDeServicio = estado;
                break;
            }
        }
        if (estadoFueraDeServicio != null) {
            this.estadoFueraDeServicio = estadoFueraDeServicio;
            pantalla.mostrarEstadoFueraDeServicio(estadoFueraDeServicio); // muestra en pantalla
        } else {
            pantalla.mostrarErrorEstadoNoEncontrado("FUERA DE SERVICIO");
        }
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
