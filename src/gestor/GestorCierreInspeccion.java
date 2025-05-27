package gestor;

// Import del boundary, modelos y datos
import boundary.*;
import datos.RepositorioDatos;
import model.*;

// Import de utilidades de Java
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.time.format.DateTimeFormatter;

public class GestorCierreInspeccion {
    // Pantalla
    private PantallaInspeccion pantalla;

    // Atributos
    private List<OrdenDeInspeccion> ordenesDeInspeccion;
    private String observacionCierreOrden;
    private Map<MotivoTipo, String> motivosYComentarios = new HashMap<>();
    private List<MotivoTipo> punterosMotivos = new ArrayList<>();
    private MotivoTipo ultimoMotivoSeleccionado = null;
    private String confirmacionCierreOrden;
    private LocalDateTime fechaHoraActual = LocalDateTime.now();
    private String mailResponsable;
    private String correoAEnviar;
    private List<Map<String, Object>> ordenesFiltradasConDatos;
    private Map<String,Object> ordenSeleccionada;

    // Atributos referenciales o punteros
    private Empleado empleadoLogueado;
    private Sesion sesion;
    private Empleado responsableReparacion;
    private Estado estadoCerrado;
    private Estado estadoFueraDeServicio;

    // Atributos referenciales de los boundary auxiliares
    private InterfazNotificacionMail interfazMail;
    private MonitorCCRS monitorCCRS;

    // Getters y métodos extra del gestor
    public Map<String,Object> getOrdenSeleccionada(){
        return this.ordenSeleccionada;
    }

    public int getPunteroMotivoSize() {
        return this.punterosMotivos.size();
    }

    public String asString(Map<String, Object> datosOrden) {
        String nro = String.valueOf(datosOrden.get("nroDeOrden"));
        String estacion = String.valueOf(datosOrden.get("nombreEstacionSismologica"));
        String idSismografo = String.valueOf(datosOrden.get("idSismografo"));
        String fechaFin = String.valueOf(datosOrden.get("fechaFinalizacion"));

        return String.format(
                "Orden #%s - Estación: %s | Sismógrafo: %s | Finalizada: %s",
                nro, estacion, idSismografo, fechaFin
        );
    }

    public void setOrdenesDeInspeccion(List<OrdenDeInspeccion> ordenes) {
        this.ordenesDeInspeccion = ordenes;
    }

    public List<Map<String,Object>> getOrdenesFiltradasConDatos() {
        return ordenesFiltradasConDatos;
    }

    public GestorCierreInspeccion(PantallaInspeccion pantalla) {
        this.pantalla = pantalla;
        this.empleadoLogueado = Sesion.getInstancia().getUsuario().getEmpleado();
        this.ordenesDeInspeccion = RepositorioDatos.obtenerOrdenes();
    } // Constructor

    public void setFechaHoraActual() {
        this.fechaHoraActual = LocalDateTime.now();
    }

    // Métodos del Caso de Uso 37
    // PASO 1
    public void iniciarCierreOrdenInspeccion() {
        obtenerEmpleadoLogueado();
    }

    // PASO 2
    public void obtenerEmpleadoLogueado() {
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

        if (ordenesFiltradas.isEmpty()) {
            pantalla.mostrarOrdCompRealizadas();
        } else {
            ordenarPorFechaDeFinalizacion();
            pantalla.mostrarOrdCompRealizadas();
            pedirSelecOrdenInspeccion();
        }
    }

    public void ordenarPorFechaDeFinalizacion() {
        if (ordenesFiltradasConDatos == null) {
            pantalla.mostrarMensaje("No hay órdenes para ordenar");
            return;
        }

        ordenesFiltradasConDatos.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                // Si las fechas son LocalDateTime
                LocalDateTime fecha1 = (LocalDateTime) o1.get("fechaFinalizacion");
                LocalDateTime fecha2 = (LocalDateTime) o2.get("fechaFinalizacion");

                if (fecha1 == null && fecha2 == null) return 0;
                if (fecha1 == null) return -1;
                if (fecha2 == null) return 1;

                return fecha1.compareTo(fecha2);
            }
        });
    }

    public void pedirSelecOrdenInspeccion() {
        pantalla.pedirSelecOrdenInspeccion(this.ordenesFiltradasConDatos);
    }

    //PASO 3
    public void tomarOrdenInspeccionSelec(Map<String,Object> ordenSeleccionada) {
        this.ordenSeleccionada = ordenSeleccionada;
        pedirObservacionCierreOrden();
    }

    //PASO 4
    public void pedirObservacionCierreOrden() {
        pantalla.pedirObservacionCierreOrden();
    }

    //PASO 5
    public void tomarObservacionCierreOrden(String observacion) {
        this.observacionCierreOrden = observacion;
        buscarTiposMotivosFueraServicio();
    }

    //PASO 6
    public void buscarTiposMotivosFueraServicio() {
        this.punterosMotivos = RepositorioDatos.getMotivos();  // obtiene todos los MotivoTipo
        List<String> descMotivos = new ArrayList<>();        // lista para guardar solo las descripciones

        for (MotivoTipo motivo : this.punterosMotivos) {
            descMotivos.add(motivo.getDescripcion());        // extrae y agrega cada descripción
        }

        pantalla.mostrarMotivosTipoFueraServicio(descMotivos);
        pedirSelecMotivosFueraServicio();
    }

    public void pedirSelecMotivosFueraServicio() {
            int motivoNum = -1;

            while (motivoNum != 0) {
                motivoNum = pantalla.tomarMotivoTipoFueraServicio();

                if (motivoNum == -1) {
                    // el usuario canceló
                    return; // salir del método y no seguir con el flujo
                }

                if (motivoNum != 0) {
                    if (!motivosYComentarios.containsKey(this.punterosMotivos.get(motivoNum - 1))) {
                        // Llama a la pantalla para pedir el comentario
                        pantalla.pedirComentario();
                    } else {
                        pantalla.mostrarMensaje("Error! ese motivo ya fue seleccionado...");
                    }
                } else {
                    pantalla.mostrarMensaje("Selección de motivos finalizada.");
                }
            }
            pedirConfirmacionCierreOrden(); // < no se debería ejecutar si hubo cancelación
        }

    //PASO 7
    public void tomarMotivoTipoFueraServicio(int motivoNum) {
        if (motivoNum <= 0 || motivoNum > punterosMotivos.size()) {
            // Motivo inválido o cancelación: no hacer nada, o lanzar excepción controlada si querés.
            pantalla.mostrarMensaje("Motivo inválido o cancelación recibida: " + motivoNum);
            return;
        }
        MotivoTipo motivoSelecc = this.punterosMotivos.get(motivoNum - 1);
        this.ultimoMotivoSeleccionado = motivoSelecc;
    }

    public Map<MotivoTipo,String> getMotivosYComentarios() {
        return motivosYComentarios;
    }

    public void tomarComentario(String comentario) {
        motivosYComentarios.put(this.ultimoMotivoSeleccionado, comentario);
    }

    //PASO 8
    public void pedirConfirmacionCierreOrden() {
        pantalla.pedirConfirmacionCierreOrden();
    }

    //PASO 9
    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        this.confirmacionCierreOrden = confirmacionCierre;
        validarExistenciaObservacion();
    }

    //PASO 10
    public void validarExistenciaObservacion() {
        if (observacionCierreOrden != null && !observacionCierreOrden.trim().isEmpty()) {
            pantalla.mostrarMensaje("Observacion validada exitosamente!");
        } else {
            pantalla.mostrarMensaje("ERROR! La observacion de cierre es obligatoria para ingresar!!");
        };
        validarExistenciaMotivoSeleccionado();
    }

    public void validarExistenciaMotivoSeleccionado() {
        if (this.motivosYComentarios != null && !motivosYComentarios.isEmpty()) {
            pantalla.mostrarMensaje("Motivo Seleccionado validado exitosamente!");
        } else {
            pantalla.mostrarMensaje("ERROR! El motivo se debe seleccionar obligatoriamente");
        }

        buscarEstadoCerrado();
        buscarFueraServicio();
        cerrarOrdenInspeccion();
    }

    //PASO 11
    public void buscarEstadoCerrado() {
        List<Estado> estados = RepositorioDatos.getEstados(); // obtiene todos los estados
        Estado estadoCerrado = null;

        for (Estado estado : estados) {
            if ("CERRADO".equalsIgnoreCase(estado.getNombreEstado()) && estado.sosAmbitoOrdenDeInspeccion()) {
                estadoCerrado = estado;
                break;
            }
        }

        if (estadoCerrado != null) {
            this.estadoCerrado = estadoCerrado;
            // pantalla.mostrarEstadoCerrado(estadoCerrado);
        }  else {
            pantalla.mostrarMensaje("No se encontró el estado Cerrado");
        }
    }

    public LocalDateTime getFechaHoraActual() {
        return this.fechaHoraActual;
    }

    public void buscarFueraServicio() {
        List<Estado> estados = RepositorioDatos.getEstados(); // obtiene todos los estados
        Estado estadoFueraDeServicio = null;

        for (Estado estado : estados) {
            if ("FUERA DE SERVICIO".equalsIgnoreCase(estado.getNombreEstado()) && estado.sosFueraDeServicio()) {
                estadoFueraDeServicio = estado;
                break;
            }
        }
        if (estadoFueraDeServicio != null) {
            this.estadoFueraDeServicio = estadoFueraDeServicio;
            // pantalla.mostrarEstadoFueraDeServicio(estadoFueraDeServicio);
        } else {
            pantalla.mostrarMensaje("No se encontró el estado Fuera de Servicio");
        }
    }

    public void cerrarOrdenInspeccion() {
        OrdenDeInspeccion ordenEncontrada = null;
        String nroSeleccionado = String.valueOf(this.ordenSeleccionada.get("nroDeOrden"));

        for (OrdenDeInspeccion orden : this.ordenesDeInspeccion) {
            String nroOrden = String.valueOf(orden.getNroDeOrden());

            if (nroOrden.equals(nroSeleccionado)) {
                ordenEncontrada = orden;
                break;
            }
        }
        ordenEncontrada.cerrar(this.estadoCerrado);
        ponerSismografoFueraServicio(ordenEncontrada);
    }

    //PASO 12
    public void ponerSismografoFueraServicio(OrdenDeInspeccion ordenEncontrada) {
        ordenEncontrada.ponerSismografoFueraServicio(this.estadoFueraDeServicio, this.motivosYComentarios);
        buscarResponsableReparacion();
    }

    //PASO 13
    public void buscarResponsableReparacion() {
        List<Empleado> todosLosEmpleados = RepositorioDatos.getEmpleados(); // asumido
        List<String> mails = new ArrayList<>();

        for (Empleado e : todosLosEmpleados) {
            if (e.esResponsableReparacion()) { // llama a Empleado -> Rol
                String mail = e.obtenerMail();
                mails.add(mail);
            }
        }

        enviarCorreo(mails);
    }

    public void enviarCorreo(List<String> mails) {
        // Obtener las dependencias del repositorio
        InterfazNotificacionMail interfazMail = RepositorioDatos.getInterfazMail();
        List<MonitorCCRS> monitores = RepositorioDatos.getMonitores();

        // Generar el mensaje que se enviará y publicará
        String mensaje = generarMensajeNotificacion();

        // Enviar mail a cada destinatario
        for (String mail : mails) {
            interfazMail.enviarNotificacion(mail, mensaje);
        }

        // Publicar en cada monitor
        for (MonitorCCRS monitor : monitores) {
            monitor.publicarNotificacion(mensaje);
        }

        finCU();
    }

    // Este método no se incluyó en el diagrama de secuencia ya que no lo solicitaba la consigna
    // Se encarga de generar el mensaje que se mostrará por pantalla para los monitores y los mails
    public String generarMensajeNotificacion() {
        // Obtener la identificación del sismógrafo
        String identificacionSismografo = "";
        if (ordenSeleccionada != null) {
            Object sismo = ordenSeleccionada.get("sismografo");
            if (sismo instanceof Sismografo) {
                identificacionSismografo = ((Sismografo) sismo).getIdentificadorSismografo();
            }
        }

        // Nombre del estado "Fuera de Servicio"
        String nombreEstado = estadoFueraDeServicio != null ? estadoFueraDeServicio.getNombreEstado() : "Fuera de Servicio";

        // Fecha y hora actual del cambio de estado
        String fechaHora = getFechaHoraActual().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Construir texto de motivos y comentarios
        StringBuilder motivosTexto = new StringBuilder();
        for (Map.Entry<MotivoTipo, String> entry : motivosYComentarios.entrySet()) {
            String motivo = entry.getKey().getDescripcion();
            String comentario = entry.getValue();
            motivosTexto.append("- ").append(motivo);
            if (comentario != null && !comentario.isBlank()) {
                motivosTexto.append(": ").append(comentario);
            }
            motivosTexto.append("\n");
        }

        // Armar mensaje completo
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Notificación de Cambio de Estado del Sismógrafo\n\n");
        mensaje.append("Sismógrafo: ").append(identificacionSismografo).append("\n");
        mensaje.append("Nuevo estado: ").append(nombreEstado).append("\n");
        mensaje.append("Fecha y hora de cambio: ").append(fechaHora).append("\n\n");
        mensaje.append("Motivos y comentarios:\n").append(motivosTexto);

        return mensaje.toString();
    }

    public void finCU() {
        pantalla.mostrarMensaje("Caso de uso ejecutado exitosamente.");
    }
}
