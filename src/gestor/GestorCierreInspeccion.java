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
    private String confirmacionCierreOrden;
    private LocalDateTime fechaHoraActual = LocalDateTime.now();
    private String mailResponsable;
    private String correoAEnviar;
    private List<Map<String, Object>> ordenesFiltradasConDatos;
    private Map<String,Object> ordenSeleccionada;
    private OrdenDeInspeccion ordenEncontrada;

    // ==================== ATRIBUTOS REFERENCIALES ====================
    private Empleado empleadoLogueado;
    private Sesion sesion;
    private Empleado responsableReparacion;
    // Ya no son necesarios por el rediseño ❗❗❗❗❗❗❗❗❗❗
    // private Estado estadoCerrado;
    // private Estado estadoFueraDeServicio;

    // ==================== BOUNDARY AUXILIARES ====================
    private InterfazNotificacionMail interfazMail;
    private List<MonitorCCRS> monitores;

    // ==================== SECCIÓN CONSTRUCTOR Y GETTERS ====================
    public GestorCierreInspeccion(PantallaInspeccion pantalla) {
        this.pantalla = pantalla;
        this.sesion = Sesion.getInstancia();
        this.empleadoLogueado = sesion.getUsuario().getEmpleado();
        this.ordenesDeInspeccion = RepositorioDatos.obtenerOrdenes();
        // Obtiene los monitores y la Interfaz de mail a la que enviará la notificación
        this.interfazMail = RepositorioDatos.getInterfazMail();
        this.monitores = RepositorioDatos.getMonitores();
    } 

    public Map<String,Object> getOrdenSeleccionada(){
        return this.ordenSeleccionada;
    }

    public int getPunteroMotivoSize() {
        return this.punterosMotivos.size();
    }

    public void setOrdenesDeInspeccion(List<OrdenDeInspeccion> ordenes) {
        this.ordenesDeInspeccion = ordenes;
    }

    public List<Map<String,Object>> getOrdenesFiltradasConDatos() {
        return ordenesFiltradasConDatos;
    }

    public void setFechaHoraActual() {
        this.fechaHoraActual = LocalDateTime.now();
    }

    public Map<MotivoTipo, String> getMotivosYComentarios() {
        return motivosYComentarios;
    }

    // Para imprimir los datos de la orden desde el Map
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

    public LocalDateTime getFechaHoraActual() {
        return this.fechaHoraActual;
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

        // Lista de mapas con los datos de las órdenes filtradas
        List<Map<String, Object>> ordenesFiltradas = new ArrayList<>();

        // Filtrar órdenes completamente realizadas del RI
        for (OrdenDeInspeccion orden : ordenesDeInspeccion) {
            if (orden.sosDeEmpleado(empleado) && orden.sosCompletamenteRealizada()) {
                // Obtenemos el mapa con los datos
                Map<String, Object> datosOrden = orden.obtenerDatosOI();
                ordenesFiltradas.add(datosOrden);
            }
        }

        // Guardar las órdenes filtradas con datos
        this.ordenesFiltradasConDatos = ordenesFiltradas;

        // Flujo alternativo A1: No hay órdenes realizadas
        /*
         *  if (ordenesFiltradas.isEmpty()) {
                pantalla.mostrarMensaje("A1: No hay órdenes de inspección completamente realizadas");
                pantalla.mostrarOrdCompRealizadas();
                return;
            }
            // Ordenar por fecha de finalización (Observación 1)
            ordenarPorFechaDeFinalizacion();
        
            // Mostrar órdenes y pedir selección
            pantalla.mostrarOrdCompRealizadas();
            pedirSelecOrdenInspeccion();
         */
        if (ordenesFiltradas.isEmpty()) {
            pantalla.mostrarOrdCompRealizadas();
        } else {
            ordenarPorFechaDeFinalizacion();
            pantalla.mostrarOrdCompRealizadas();
            // pedirSelecOrdenInspeccion();
        }
    }

    public void ordenarPorFechaDeFinalizacion() {
        if (ordenesFiltradasConDatos == null || ordenesFiltradasConDatos.isEmpty()) {
            pantalla.mostrarMensaje("No hay órdenes para ordenar");
            return;
        }

        // Comparator es una interfaz que permite definir una lógica de comparación personalizada para ordenar objetos de forma flexible
        // En este caso se utiliza para ordenar las OI por fecha
        ordenesFiltradasConDatos.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                LocalDateTime fecha1 = (LocalDateTime) o1.get("fechaFinalizacion");
                LocalDateTime fecha2 = (LocalDateTime) o2.get("fechaFinalizacion");

                if (fecha1 == null && fecha2 == null) return 0;
                if (fecha1 == null) return -1;
                if (fecha2 == null) return 1;

                return fecha1.compareTo(fecha2);
            }
        });
    }

    /* 
    public void pedirSelecOrdenInspeccion() {
        pantalla.pedirSelecOrdenInspeccion(this.ordenesFiltradasConDatos);
    }*/

    /**
     * PASO 3: El RI selecciona una orden de inspección
     */
    public void tomarOrdenInspeccionSelec(Map<String,Object> ordenSeleccionada) {
        this.ordenSeleccionada = ordenSeleccionada;
        pedirObservacionCierreOrden();
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
        // Utilizamos un repositorio de Datos (que reemplazaría a una BD) para instanciar los objetos
        // Obtiene todos los MotivoTipo del repositorio de datos
        this.punterosMotivos = RepositorioDatos.getMotivos(); 
        // Lista para guardar solo las descripciones
        List<String> descMotivos = new ArrayList<>();

        for (MotivoTipo motivo : this.punterosMotivos) {
            // Extrae y agrega cada descripción
            descMotivos.add(motivo.getDescripcion());        
        }

        pantalla.mostrarMotivosTipoFueraServicio(descMotivos);
        pedirSelecMotivosFueraServicio();
    }

    public void pedirSelecMotivosFueraServicio() {
            int motivoNum = -1;

            while (motivoNum != 0) {
                motivoNum = pantalla.tomarMotivoTipoFueraServicio();

                // Flujo alternativo A7: El actor cancela el CU
                if (motivoNum == -1) {
                    // pantalla.mostrarMensaje("A7: Operación cancelada por el usuario");
                    return; // salir del método y no seguir con el flujo del CU
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
            pedirConfirmacionCierreOrden(); // No se debería ejecutar si hubo cancelación
        }

    /**
     * PASO 7: El RI selecciona uno o varios motivos con comentarios
     */
    public void tomarMotivoTipoFueraServicio(int motivoNum) {
        if (motivoNum <= 0 || motivoNum > punterosMotivos.size()) {
            pantalla.mostrarMensaje("Motivo inválido o cancelación recibida: " + motivoNum);
            return;
        }
        this.ultimoMotivoSeleccionado = this.punterosMotivos.get(motivoNum - 1);
    }

    public void tomarComentario(String comentario) {
        motivosYComentarios.put(this.ultimoMotivoSeleccionado, comentario);
        /*
         *  if (this.ultimoMotivoSeleccionado != null) {
                motivosYComentarios.put(this.ultimoMotivoSeleccionado, comentario);
            }
         */
    }

    /**
     * PASO 8: Sistema solicita confirmación para cerrar la orden
     */
    public void pedirConfirmacionCierreOrden() {
        pantalla.pedirConfirmacionCierreOrden();
    }

    /**
     * PASO 9: El RI confirma el cierre
     */
    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        this.confirmacionCierreOrden = confirmacionCierre;
        validarExistenciaObservacion();
    }

    /**
     * PASO 10: Sistema valida que exista observación y motivos
     */
    public void validarExistenciaObservacion() {
        if (observacionCierreOrden != null && !observacionCierreOrden.trim().isEmpty()) {
            pantalla.mostrarMensaje("Observacion validada exitosamente!");
        } else {
            pantalla.mostrarMensaje("ERROR! La observacion de cierre es obligatoria para ingresar!!");
            // return;  // Detener el flujo si no hay observación ❗❗❗❗❗❗❗❗❗❗❗❗
        };
        validarExistenciaMotivoSeleccionado();
    }

    /*
     *public void validarExistenciaObservacion() {
        if (observacionCierreOrden == null || observacionCierreOrden.trim().isEmpty()) {
            pantalla.mostrarMensaje("ERROR: La observación de cierre es obligatoria");
            return;
        }
        
        pantalla.mostrarMensaje("✓ Observación validada");
        validarExistenciaMotivoSeleccionado();
      }
     */

    public void validarExistenciaMotivoSeleccionado() {
        /*
         * // Flujo alternativo A3: Datos faltantes
        if (motivosYComentarios == null || motivosYComentarios.isEmpty()) {
            pantalla.mostrarMensaje("A3: ERROR - Al menos un motivo debe estar seleccionado");
            return;
        }

        pantalla.mostrarMensaje("✓ Motivos validados");
         */
        
        if (this.motivosYComentarios != null && !motivosYComentarios.isEmpty()) {
            pantalla.mostrarMensaje("Motivo Seleccionado validado exitosamente!");
        } else {
            pantalla.mostrarMensaje("ERROR! El motivo se debe seleccionar obligatoriamente");
            // return;  // Detener el flujo si no hay motivos ❗❗❗❗❗❗❗❗❗❗❗❗❗❗
        }

        // buscarFueraServicio() y buscarEstadoCerrado se eliminan en el rediseño
        // buscarEstadoCerrado();
        // buscarFueraServicio();
        // PASO 11: PUNTO DE ENGANCHE CON EL PATRÓN STATE
        cerrarOrdenInspeccion();
    }

    // ========== PATRÓN STATE - PUNTO DE ENGANCHE ==========
    /**
     * PASO 11: Sistema actualiza orden a cerrada y sismografo a fuera de servicio
     * 
     */ 

    /*  MÉTODOS ELIMINADOS POR APLICACIÓN DEL STATE ❗❗❗❗❗❗❗❗❗
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
    */

    public void cerrarOrdenInspeccion() {
        // Buscar la orden completa en la lista
        String nroSeleccionado = String.valueOf(this.ordenSeleccionada.get("nroDeOrden"));

        for (OrdenDeInspeccion orden : this.ordenesDeInspeccion) {
            if (orden.getNroDeOrden() == Integer.parseInt(nroSeleccionado)) {
                this.ordenEncontrada = orden;
                break;
            }
        }

        if (this.ordenEncontrada == null) {
            pantalla.mostrarMensaje("ERROR: No se encontró la orden de inspección");
            return;
        }

        // Obtener fecha/hora actual
        this.fechaHoraActual = LocalDateTime.now();

        // Cerrar la orden (CompletamenteRealizada → Cerrada)
        // System.out.println("11a. Cerrando orden de inspección #" + ordenEncontrada.getNroDeOrden());
        ordenEncontrada.cerrarOrden(this.fechaHoraActual, this.observacionCierreOrden);

        // Poner sismografo fuera de servicio
        // System.out.println("11b. Poniendo sismografo fuera de servicio");
        ponerSismografoFueraServicio();
    }

    /**
     * PASO 12: Poner sismografo fuera de servicio con motivos
     */
    public void ponerSismografoFueraServicio() {
        System.out.println("12. Ejecutando transición de sismografo");

        // Convertir Map<MotivoTipo, String> a List<Map<String, Object>>
        List<Map<String, Object>> listaMotivos = convertirMotivoMapALista();

        // Delegar al patrón State
        this.ordenEncontrada.ponerSismografoFueraServicio(
            this.fechaHoraActual,
            listaMotivos
        );

        // Continuar con notificaciones
        buscarResponsableReparacion();
    }

    // Convierte la estructura Map<MotivoTipo, String> a List<Map<String, Object>>
    private List<Map<String, Object>> convertirMotivoMapALista() {
        List<Map<String, Object>> listaMotivos = new ArrayList<>();

        for (Map.Entry<MotivoTipo, String> entry : motivosYComentarios.entrySet()) {
            Map<String, Object> motivo = new HashMap<>();
            motivo.put("tipo", entry.getKey().getDescripcion());
            motivo.put("comentario", entry.getValue());
            listaMotivos.add(motivo);
        }

        return listaMotivos;
    }

    /**
     * PASO 13: Notificaciones a los empleados y publicación en los monitores CCRS
     */
    public void buscarResponsableReparacion() {
        List<Empleado> todosLosEmpleados = RepositorioDatos.getEmpleados(); // asumido
        List<String> mails = new ArrayList<>();

        for (Empleado e : todosLosEmpleados) {
            if (e.esResponsableReparacion()) { // llama a Empleado -> Rol
                String mail = e.obtenerMail();
                mails.add(mail);
            }
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
        // System.out.println("   Enviando notificaciones por mail y monitores");
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

    // Genera el mensaje de notificación (Observación 2)
    // Incluye: identificación del sismografo, estado, fecha/hora, motivos y comentarios
    public String generarMensajeNotificacion() {
        // Obtener ID del sismografo
        String idSismografo = "";
        if (ordenSeleccionada != null) {
            Object id = ordenSeleccionada.get("idSismografo");
            if (id != null) {
                idSismografo = id.toString();
            }
        }

        // Nombre del nuevo estado
        String nombreEstado = "Fuera De Servicio";

        // Fecha y hora del cambio (Observación 2)
        String fechaHora = fechaHoraActual.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        );

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
        mensaje.append("═══════════════════════════════════════════════════════════\n");
        mensaje.append("NOTIFICACIÓN: CAMBIO DE ESTADO DE SISMOGRAFO\n");
        mensaje.append("═══════════════════════════════════════════════════════════\n\n");
        mensaje.append("Identificación del Sismografo: ").append(idSismografo).append("\n");
        mensaje.append("Nuevo Estado: ").append(nombreEstado).append("\n");
        mensaje.append("Fecha y Hora de Cambio: ").append(fechaHora).append("\n\n");
        mensaje.append("MOTIVOS Y COMENTARIOS:\n");
        mensaje.append(motivosTexto);
        mensaje.append("\n═══════════════════════════════════════════════════════════\n");

        return mensaje.toString();
    }

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
        this.confirmacionCierreOrden = null;
        this.ordenSeleccionada = null;
        this.ordenEncontrada = null;
    }
}
