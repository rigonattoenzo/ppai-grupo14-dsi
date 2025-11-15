package model.estados;

import model.Sismografo;
import model.CambioDeEstado;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Clase abstracta que define el comportamiento de los estados del sismografo.
 * Implementa el patrón State para manejar transiciones de estado.
 */
public abstract class Estado {
    protected String ambito;
    protected String nombreEstado;

    // Constructor
    public Estado(String nombreEstado, String ambito) {
        this.nombreEstado = nombreEstado;
        this.ambito = ambito;
    }

    // ==================== TRANSICIONES SISMOGRAFO ====================
    public void enInstalacion(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede transicionar a 'En Instalación' desde " + this.nombreEstado);
    }

    public void disponibilizar(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede 'Disponibilizar' desde " + this.nombreEstado);
    }

    public void recibirCertificacion(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede 'Recibir Certificación' desde " + this.nombreEstado);
    }

    public void enLinea(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede transicionar a 'En Línea' desde " + this.nombreEstado);
    }

    public void reclamar(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede 'Reclamar' desde " + this.nombreEstado);
    }

    public void enEspera(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede transicionar a 'En Espera' desde " + this.nombreEstado);
    }

    public void incluirEnPlanConstruccion(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede 'Incluir en Plan de Construcción' desde " + this.nombreEstado);
    }

    public void iniciarPlanConstruccion(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede 'Iniciar Plan de Construcción' desde " + this.nombreEstado);
    }

    public void fueraServicio(Sismografo sismografo, LocalDateTime fechaActual,
            CambioDeEstado[] cambiosEstado,
            List<Map<String, Object>> motivos) {
        throw new UnsupportedOperationException(
                "No se puede transicionar a 'Fuera de Servicio' desde " + this.nombreEstado);
    }

    public void darDeBaja(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede 'Dar de Baja' desde " + this.nombreEstado);
    }

    public void inhabilitar(Sismografo sismografo) {
        throw new UnsupportedOperationException(
                "No se puede 'Inhabilitar' desde " + this.nombreEstado);
    }

    // ========== TRANSICIONES ORDEN DE INSPECCIÓN ==========
    /**
     * Transición: PendienteDeRealización → ParcialmenteRealizada
     * Se ejecuta cuando se registra el primer resultado de inspección.
     */
    public void registrarPrimera(Object orden) {
        throw new UnsupportedOperationException(
                "No se puede 'Registrar Primera Actividad' desde " + this.nombreEstado);
    }

    /**
     * Transición: ParcialmenteRealizada → CompletamenteRealizada
     * Se ejecuta cuando se registran todos los resultados.
     */
    public void completarTodas(Object orden) {
        throw new UnsupportedOperationException(
                "No se puede 'Completar Todas las Actividades' desde " + this.nombreEstado);
    }

    /**
     * Transición: CompletamenteRealizada → Cerrada
     * Se ejecuta cuando se cierra definitivamente la orden.
     */
    public void cerrar(Object orden, LocalDateTime fechaCierre,
            String observacionCierre) {
        throw new UnsupportedOperationException(
                "No se puede 'Cerrar Orden' desde " + this.nombreEstado);
    }

    // ==================== GETTERS ====================
    public String getNombreEstado() {
        return nombreEstado;
    }

    public String getAmbito() {
        return ambito;
    }

    // ==================== MÉTODOS DE CONSULTA DEL CU 37 ====================
    public boolean esCompletamenteRealizada() {
        return "Completamente Realizada".equalsIgnoreCase(nombreEstado);
    }

    public boolean sosAmbitoOrdenDeInspeccion() {
        return "Orden de Inspeccion".equalsIgnoreCase(ambito);
    }

    public boolean sosCerrada() {
        return "Cerrada".equalsIgnoreCase(nombreEstado);
    }

    public boolean sosAmbitoSismografo() {
        return "Sismografo".equalsIgnoreCase(ambito);
    }

    public boolean sosFueraDeServicio() {
        return "Fuera De Servicio".equalsIgnoreCase(nombreEstado);
    }

    public static Estado fromString(String nombre) {
        switch (nombre) {
            // Estados de OrdenDeInspeccion
            case "PendienteDeRealizacion":
            case "Pendiente De Realización":
                return new PendienteDeRealizacion();
            case "ParcialmenteRealizada":
            case "Parcialmente Realizada":
                return new ParcialmenteRealizada();
            case "CompletamenteRealizada":
            case "Completamente Realizada":
                return new CompletamenteRealizada();
            case "Cerrada":
                return new Cerrada();

            // Estados de Sismografo
            case "Disponible":
                return new Disponible();
            case "EnEsperaCertificacion":
            case "En Espera Certificación":
                return new EnEsperaCertificacion();
            case "IncluidoEnPlanConstruccion":
            case "Incluido En Plan Construcción":
                return new IncluidoEnPlanConstruccion();
            case "HabilitadoParaConstruccion":
            case "Habilitado Para Construcción":
                return new HabilitadoParaConstruccion();
            case "EnInstalacion":
            case "En Instalación":
                return new EnInstalacion();
            case "EnLinea":
            case "En Línea":
                return new EnLinea();
            case "InhabilitadoPorInspeccion":
            case "Inhabilitado Por Inspección":
                return new InhabilitadoPorInspeccion();
            case "FueraServicio":
            case "Fuera De Servicio":
                return new FueraServicio();
            case "Reclamado":
                return new Reclamado();
            case "DeBaja":
            case "De Baja":
                return new DeBaja();
            default:
                // Por defecto, para OrdenDeInspeccion y Sismografo
                return new PendienteDeRealizacion();
        }
    }

    // Método para representación
    @Override
    public String toString() {
        return nombreEstado + " (" + ambito + ")";
    }
}
