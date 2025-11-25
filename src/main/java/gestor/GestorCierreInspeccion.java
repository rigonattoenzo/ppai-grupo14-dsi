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

/**
 * Gestor para el Caso de Uso 37: Dar cierre a orden de inspección de ES
 * Implementa el patrón State para la transición de estados:
 * - OrdenDeInspeccion: CompletamenteRealizada → Cerrada
 * - Sismografo: InhabilitadoPorInspeccion → FueraDeServicio
 */
public class GestorCierreInspeccion {
    // ==================== BOUNDARY ====================
    private PantallaInspeccion pantalla;

    // ==================== ATRIBUTOS ====================
    private List<OrdenDeInspeccion> ordenesDeInspeccion;
    private String observacionCierreOrden;
    private Map<MotivoTipo, String> motivosYComentarios = new HashMap<>();
    private List<MotivoTipo> punterosMotivos = new ArrayList<>();
    private MotivoTipo ultimoMotivoSeleccionado = null;
    private LocalDateTime fechaHoraActual = LocalDateTime.now();
    private List<Map<String, Object>> ordenesFiltradasConDatos;
    private Map<String, Object> ordenSeleccionada;
    private OrdenDeInspeccion ordenEncontrada;

    // ==================== ATRIBUTOS REFERENCIALES ====================
    private Empleado empleadoLogueado;
    private Sesion sesion;
    private Sismografo sismografoEncontrado;
    private List<Sismografo> todosSismografos;
    // Ya no son necesarios por el rediseño ❗❗❗❗❗❗❗❗❗❗
    // private Estado estadoCerrado;
    // private Estado estadoFueraDeServicio;

    // ==================== BOUNDARY AUXILIARES ====================
    private InterfazNotificacionMail interfazMail;
    private List<MonitorCCRS> monitores;

    // ==================== SECCIÓN CONSTRUCTOR ====================
    public GestorCierreInspeccion(PantallaInspeccion pantalla) {
        this.pantalla = pantalla;
        this.sesion = Sesion.getInstancia();
        this.empleadoLogueado = sesion.getUsuario().getEmpleado();
        this.ordenesDeInspeccion = RepositorioDatos.obtenerOrdenes();
        this.todosSismografos = RepositorioDatos.obtenerSismografos();
        // Obtiene los monitores y la Interfaz de mail a la que enviará la notificación
        this.interfazMail = RepositorioDatos.getInterfazMail();
        this.monitores = RepositorioDatos.getMonitores();
    }

    // ==================== GETTERS Y SETTERS ====================
    public Map<String, Object> getOrdenSeleccionada() {
        return this.ordenSeleccionada;
    }

    public int getPunteroMotivoSize() {
        return this.punterosMotivos.size();
    }

    public void setOrdenesDeInspeccion(List<OrdenDeInspeccion> ordenes) {
        this.ordenesDeInspeccion = ordenes;
    }

    public List<Map<String, Object>> getOrdenesFiltradasConDatos() {
        return ordenesFiltradasConDatos;
    }

    public LocalDateTime getFechaHoraActual() {
        return LocalDateTime.now();
    }

    public Map<MotivoTipo, String> getMotivosYComentarios() {
        return motivosYComentarios;
    }

    public MotivoTipo getUltimoMotivoSeleccionado() {
        return this.ultimoMotivoSeleccionado;
    }

    public void agregarMotivoAlGestor(MotivoTipo motivo, String comentario) {
        this.motivosYComentarios.put(motivo, comentario);
    }

    public Sismografo getSismografoEncontrado() {
        return this.sismografoEncontrado;
    }

    // Convierte la estructura Map<MotivoTipo, String> a List<Map<String, Object>>
    private List<Map<String, Object>> convertirMotivoMapALista() {
        List<Map<String, Object>> listaMotivos = new ArrayList<>();

        for (Map.Entry<MotivoTipo, String> entry : motivosYComentarios.entrySet()) {
            Map<String, Object> motivo = new HashMap<>();
            motivo.put("tipo", entry.getKey());
            motivo.put("comentario", entry.getValue());
            listaMotivos.add(motivo);
        }

        return listaMotivos;
    }

    // ==================== SECCIÓN FLUJO DEL CASO DE USO 37 ====================
    /**
     * PASO 1: El RI selecciona la opción "Cerrar Orden de Inspección"
     */
    public void iniciarCierreOrdenInspeccion() {
        obtenerEmpleadoLogueado();
    }

    /**
     * PASO 2: Sistema busca órdenes completamente realizadas del RI logueado
     */
    public void obtenerEmpleadoLogueado() {
        this.empleadoLogueado = Sesion.getInstancia().getUsuario().getRiLogueado();
        buscarOrdenesDeInspeccionDeRI();
    }

    public void buscarOrdenesDeInspeccionDeRI() {
        Empleado empleado = this.empleadoLogueado;

        List<Map<String, Object>> ordenesFiltradas = new ArrayList<>();

        for (OrdenDeInspeccion orden : ordenesDeInspeccion) {
            // sosCompletamenteRealizada() se podría quitar por aplicación del state
            if (orden.sosDeEmpleado(empleado) && orden.sosCompletamenteRealizada()) {
                Map<String, Object> datosOrden = orden.obtenerDatosOI();
                ordenesFiltradas.add(datosOrden);
            }
        }

        this.ordenesFiltradasConDatos = ordenesFiltradas;
        // Alternativa 1: Sin órdenes
        if (ordenesFiltradas.isEmpty()) {
            pantalla.mostrarOrdCompRealizadas(this.ordenesFiltradasConDatos);
        } else {
            ordenarPorFechaDeFinalizacion();
            pantalla.mostrarOrdCompRealizadas(this.ordenesFiltradasConDatos);
        }
    }

    public void ordenarPorFechaDeFinalizacion() {
        if (ordenesFiltradasConDatos == null || ordenesFiltradasConDatos.isEmpty()) {
            pantalla.mostrarMensaje("No hay órdenes para ordenar");
            return;
        }

        // Comparator es una interfaz que permite definir una lógica de comparación
        // personalizada para ordenar objetos de forma flexible
        // En este caso se utiliza para ordenar las OI por fecha
        ordenesFiltradasConDatos.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                LocalDateTime fecha1 = (LocalDateTime) o1.get("fechaFinalizacion");
                LocalDateTime fecha2 = (LocalDateTime) o2.get("fechaFinalizacion");

                if (fecha1 == null && fecha2 == null)
                    return 0;
                if (fecha1 == null)
                    return -1;
                if (fecha2 == null)
                    return 1;

                return fecha1.compareTo(fecha2);
            }
        });
    }

    public void pedirSelecOrdenInspeccion(Map<String, Object> orden) {
        pantalla.pedirSelecOrdenInspeccion(orden);
    }

    /**
     * PASO 3: El RI selecciona una orden de inspección
     */
    public void tomarOrdenInspeccionSelec(Map<String, Object> ordenSeleccionada) {
        this.ordenSeleccionada = ordenSeleccionada;
        // Busca el sismografo de esta orden
        buscarSismografoDeOrden();

        // Agregar sismografo al Map después de encontrarlo
        if (this.sismografoEncontrado != null) {
            this.ordenSeleccionada.put("idSismografo",
                    this.sismografoEncontrado.getIdentificadorSismografo());
            System.out.println("✓ Sismografo agregado al Map: "
                    + this.sismografoEncontrado.getIdentificadorSismografo());
        } else {
            this.ordenSeleccionada.put("idSismografo", "NO ENCONTRADO");
            System.out.println("✗ No se encontró sismografo para esta orden");
        }

        // Mostrar la orden seleccionada con el sismografo ya cargado
        pantalla.mostrarOrdenSeleccionada(this.ordenSeleccionada);

        // Luego pedimos la observación
        pedirObservacionCierreOrden();
    }

    private void buscarSismografoDeOrden() {
        // Obtener código de estación desde la orden seleccionada
        String codigoEstacion = (String) this.ordenSeleccionada.get("codigoEstacionSismologica");

        if (codigoEstacion == null || codigoEstacion.isEmpty()) {
            System.err.println("ERROR: No se encontró código de estación en la orden");
            this.sismografoEncontrado = null;
            return;
        }

        // Pregunta a TODOS los sismógrafos si pertenecen a esta estación
        this.sismografoEncontrado = null;

        if (this.todosSismografos != null) {
            for (Sismografo sismoActual : this.todosSismografos) {
                // Flujo del diagrama: Gestor a Sismografo: *esTuEstacionSismologica()
                if (sismoActual.esTuEstacionSismologica(codigoEstacion)) {
                    this.sismografoEncontrado = sismoActual;
                    System.out.println("✓ Sismografo encontrado: "
                            + sismoActual.getIdentificadorSismografo()
                            + " para estación: " + codigoEstacion);
                    break;
                }
            }
        }

        if (this.sismografoEncontrado == null) {
            System.out.println("✗ No se encontró sismografo para estación: " + codigoEstacion);
            pantalla.mostrarError("Advertencia",
                    "No se encontró sismografo asociado a la estación. El cierre podría fallar.");
        }
    }

    /**
     * PASO 4: Sistema permite ingresar observación de cierre
     */
    public void pedirObservacionCierreOrden() {
        pantalla.pedirObservacionCierreOrden();
    }

    /**
     * PASO 5: El RI ingresa la observación de cierre
     */
    public void tomarObservacionCierreOrden(String observacion) {
        this.observacionCierreOrden = observacion;
        buscarTiposMotivosFueraServicio();
    }

    /**
     * PASO 6: Sistema muestra tipos de motivos para Fuera de Servicio
     */
    public void buscarTiposMotivosFueraServicio() {
        // El repositorio de datos es el que directamente accede a la BD para guardar y
        // obtener los datos
        this.punterosMotivos = RepositorioDatos.obtenerMotivos();
        // Lista para guardar solo las descripciones
        List<String> descMotivos = new ArrayList<>();

        for (MotivoTipo motivo : this.punterosMotivos) {
            // Extrae y agrega cada descripción
            descMotivos.add(motivo.getDescripcion());
        }

        pantalla.mostrarMotivosTipoFueraServicio(descMotivos, this.punterosMotivos);
    }

    /**
     * PASO 7: El RI selecciona uno o varios motivos con comentarios
     */
    public void tomarMotivoTipoFueraServicio(int motivoNum) {
        if (motivoNum <= 0 || motivoNum > punterosMotivos.size()) {
            // Fin de selección
            return;
        }

        this.ultimoMotivoSeleccionado = this.punterosMotivos.get(motivoNum - 1);
    }

    public void tomarComentario(String comentario) {
        if (this.ultimoMotivoSeleccionado != null) {
            this.motivosYComentarios.put(this.ultimoMotivoSeleccionado, comentario);
            this.ultimoMotivoSeleccionado = null;
        }
    }

    // EMPEZAR A MOSTRAR DESDE ACA PARA NO HACERLO LARGO ❗❗❗❗❗❗❗❗
    /**
     * PASO 8: Sistema solicita confirmación para cerrar la orden
     */
    public void pedirConfirmacionCierreOrden() {
        pantalla.pedirConfirmacionCierreOrden();
    }

    // ========== PATRÓN STATE - MÉTODO DE ENGANCHE ==========
    /**
     * PASO 9: El RI confirma el cierre
     */
    // 2) ❗❗
    public void tomarConfirmacionCierreOrden(boolean confirmacionCierre) {
        // PASO 10: Sistema valida datos
        // 3) ❗❗
        if (!validarExistenciaObservacion()) {
            return;
        }

        // 4) ❗❗
        if (!validarExistenciaMotivoSeleccionado()) {
            return;
        }

        // Validaciones exitosas: proceder con cierre
        // 5) ❗❗
        cerrarOrdenInspeccion();

        // Se eliminan en el rediseño: buscarFueraServicio() y buscarEstadoCerrado()
        // buscarEstadoCerrado();
        // buscarFueraServicio();
    }

    /**
     * PASO 10: Sistema valida que exista observación y motivos
     */
    // 3) ❗❗
    private boolean validarExistenciaObservacion() {
        if (observacionCierreOrden == null || observacionCierreOrden.trim().isEmpty()) {
            pantalla.mostrarError("Validación Fallida", "Debe ingresar una observación de cierre");
            return false;
        }
        return true;
    }

    // 4) ❗❗
    public boolean validarExistenciaMotivoSeleccionado() {
        // Flujo alternativo A3: Datos faltantes
        if (this.motivosYComentarios == null || motivosYComentarios.isEmpty()) {
            pantalla.mostrarError("Validación Fallida",
                    "Debe seleccionar AL MENOS UN motivo para cerrar la orden");
            return false;
        }
        return true;
    }

    /**
     * PASO 11: Sistema actualiza orden a cerrada y sismografo a fuera de servicio
     */
    /*
     * MÉTODOS ELIMINADOS POR APLICACIÓN DEL STATE ❗❗❗❗❗❗❗❗❗
     * public void buscarEstadoCerrado() {
     * List<Estado> estados = RepositorioDatos.getEstados(); // obtiene todos los
     * estados
     * Estado estadoCerrado = null;
     * 
     * for (Estado estado : estados) {
     * if ("CERRADO".equalsIgnoreCase(estado.getNombreEstado()) &&
     * estado.sosAmbitoOrdenDeInspeccion()) {
     * estadoCerrado = estado;
     * break;
     * }
     * }
     * 
     * if (estadoCerrado != null) {
     * this.estadoCerrado = estadoCerrado;
     * // pantalla.mostrarEstadoCerrado(estadoCerrado);
     * } else {
     * pantalla.mostrarMensaje("No se encontró el estado Cerrado");
     * }
     * }
     * 
     * public void buscarFueraServicio() {
     * List<Estado> estados = RepositorioDatos.getEstados(); // obtiene todos los
     * estados
     * Estado estadoFueraDeServicio = null;
     * 
     * for (Estado estado : estados) {
     * if ("FUERA DE SERVICIO".equalsIgnoreCase(estado.getNombreEstado()) &&
     * estado.sosFueraDeServicio()) {
     * estadoFueraDeServicio = estado;
     * break;
     * }
     * }
     * if (estadoFueraDeServicio != null) {
     * this.estadoFueraDeServicio = estadoFueraDeServicio;
     * // pantalla.mostrarEstadoFueraDeServicio(estadoFueraDeServicio);
     * } else {
     * pantalla.mostrarMensaje("No se encontró el estado Fuera de Servicio");
     * }
     * }
     */

    // 5) ❗❗
    public void cerrarOrdenInspeccion() {
        // Buscar la orden completa en la lista
        String nroSeleccionado = String.valueOf(this.ordenSeleccionada.get("nroDeOrden"));

        // Recorre todas las ordenes de inspección buscando el puntero a la seleccionada
        for (OrdenDeInspeccion orden : this.ordenesDeInspeccion) {
            if (orden.getNroDeOrden() == Integer.parseInt(nroSeleccionado)) {
                this.ordenEncontrada = orden;
                break;
            }
        }

        // Valida si encontró algo
        if (this.ordenEncontrada == null) {
            pantalla.mostrarMensaje("ERROR: No se encontró la orden de inspección");
            return;
        }

        // 6) ❗❗
        this.fechaHoraActual = getFechaHoraActual();

        try {
            // 7) ❗❗
            ordenEncontrada.cerrarOrden(this.fechaHoraActual, this.observacionCierreOrden);
        } catch (Exception e) {
            e.printStackTrace();
            pantalla.mostrarMensaje("Error al cerrar la orden: " + e.getMessage());
            return;
        }

        // Poner sismografo fuera de servicio
        ponerSismografoFueraServicio(); // 12) ❗❗
    }

    /**
     * PASO 12: Poner sismografo fuera de servicio con motivos
     */
    public void ponerSismografoFueraServicio() { // 12) ❗❗
        List<Map<String, Object>> listaMotivos = convertirMotivoMapALista();

        if (this.ordenEncontrada == null) {
            System.out.println("ERROR: ordenEncontrada es null, abortando ponerSismografoFueraServicio");
            return;
        }

        try {
            // 13) ❗❗
            this.sismografoEncontrado.fueraDeServicio(this.fechaHoraActual, listaMotivos, this.empleadoLogueado);

        } catch (Exception e) {
            e.printStackTrace();
            pantalla.mostrarMensaje("Error al poner sismógrafo fuera de servicio: " + e.getMessage());
            return;
        }

        // Guardar sismógrafo con sus datos actualizados
        try {
            RepositorioDatos.guardarSismografo(this.sismografoEncontrado);
        } catch (Exception e) {
            e.printStackTrace();
            pantalla.mostrarMensaje("Error al guardar el sismografo: " + e.getMessage());
            return;
        }

        // Guardar la orden con sus datos actualizados
        try {
            RepositorioDatos.guardarOrden(this.ordenEncontrada);
        } catch (Exception e) {
            e.printStackTrace();
            pantalla.mostrarMensaje("Error al guardar la orden: " + e.getMessage());
            return;
        }

        // Continúa con el caso de uso ❗❗ - FIN DE APLICACIÓN DEL PATRÓN STATE
        buscarResponsableReparacion();
    }

    /**
     * PASO 13: Notificaciones a los empleados y publicación en los monitores CCRS
     */
    public void buscarResponsableReparacion() {
        List<Empleado> todosLosEmpleados = RepositorioDatos.getResponsablesReparacion();
        List<String> mails = new ArrayList<>();

        for (Empleado e : todosLosEmpleados) {
            String mail = e.obtenerMail();
            mails.add(mail);
        }

        if (mails.isEmpty()) {
            // Flujo alternativo A6: Solo pantallas
            pantalla.mostrarMensaje("A6: No hay responsables de reparación. Solo publicando en monitores");
            publicarEnMonitores();
        } else {
            enviarCorreoYPublicar(mails);
        }
    }

    // Envía correos a responsables y publica en monitores
    public void enviarCorreoYPublicar(List<String> mails) {
        String mensaje = generarMensajeNotificacion();

        // Flujo alternativo A4: Solo mail
        if (mails.size() > 0 && monitores.isEmpty()) {
            for (String mail : mails) {
                interfazMail.enviarNotificacion(mail, mensaje);
            }
        } else {
            // Enviar correos
            for (String mail : mails) {
                interfazMail.enviarNotificacion(mail, mensaje);
            }
            // Publicar en monitores
            publicarEnMonitores();
        }

        finCU();
    }

    // Publica notificación en monitores del CCRS
    private void publicarEnMonitores() {
        String mensaje = generarMensajeNotificacion();

        if (monitores != null && !monitores.isEmpty()) {
            for (MonitorCCRS monitor : monitores) {
                monitor.publicarNotificacion(mensaje);
            }
        }
    }

    /**
     * Observación 2: La notificación debe incluir identificación del sismografo,
     * nombre del estado, fecha/hora, motivos y comentarios
     */
    public String generarMensajeNotificacion() {
        // Obtener ID del sismografo encontrado por el gestor
        String idSismografo = (this.sismografoEncontrado != null)
                ? this.sismografoEncontrado.getIdentificadorSismografo()
                : "N/A";

        // Nombre del nuevo estado
        String nombreEstado = "Fuera De Servicio";

        // Fecha y hora del cambio (Observación 2)
        String fechaHora = fechaHoraActual.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        // Construir motivos y comentarios
        StringBuilder motivosTexto = new StringBuilder();
        for (Map.Entry<MotivoTipo, String> entry : motivosYComentarios.entrySet()) {
            String motivo = entry.getKey().getDescripcion();
            String comentario = entry.getValue();
            motivosTexto.append("  - ").append(motivo);
            if (comentario != null && !comentario.isBlank()) {
                motivosTexto.append(": ").append(comentario);
            }
            motivosTexto.append("\n");
        }

        // Armar mensaje completo (Observación 2)
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("=========================================================\n");
        mensaje.append("NOTIFICACION: CAMBIO DE ESTADO DE SISMOGRAFO\n");
        mensaje.append("=========================================================\n\n");
        mensaje.append("Identificacion del Sismografo: ").append(idSismografo).append("\n");
        mensaje.append("Nuevo Estado: ").append(nombreEstado).append("\n");
        mensaje.append("Fecha y Hora de Cambio: ").append(fechaHora).append("\n\n");
        mensaje.append("MOTIVOS Y COMENTARIOS:\n");
        mensaje.append(motivosTexto);
        mensaje.append("\n=========================================================\n");

        return mensaje.toString();
    }

    /**
     * FIN CU: Caso de uso completado exitosamente
     */
    public void finCU() {
        pantalla.mostrarMensaje("Caso de uso ejecutado exitosamente!");
        limpiarDatos();
    }

    // Limpia los datos después al finalizar el CU
    private void limpiarDatos() {
        this.observacionCierreOrden = null;
        this.motivosYComentarios.clear();
        this.punterosMotivos.clear();
        this.ultimoMotivoSeleccionado = null;
        this.ordenSeleccionada = null;
        this.ordenEncontrada = null;
        this.sismografoEncontrado = null;
    }
}
