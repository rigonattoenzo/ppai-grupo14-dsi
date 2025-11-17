package boundary;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor CENTRAL de monitores (singleton)
 * Responsable de mantener la lista de monitores disponibles
 */
public class MonitorManager {
    private static MonitorManager instancia;
    private List<MonitorCCRS> monitores = new ArrayList<>();

    private MonitorManager() {
        // Inicializar con monitores de prueba
        monitores.add(new MonitorCCRS("MON-001", "Sala de control principal"));
        monitores.add(new MonitorCCRS("MON-002", "Sala de control secundaria"));
        monitores.add(new MonitorCCRS("MON-003", "Centro de operaciones"));
    }

    public static MonitorManager getInstancia() {
        if (instancia == null) {
            instancia = new MonitorManager();
        }
        return instancia;
    }

    public List<MonitorCCRS> getMonitores() {
        return new ArrayList<>(monitores); // Retorna copia para evitar modificaciones
    }

    public void agregarMonitor(MonitorCCRS monitor) {
        monitores.add(monitor);
    }

    public void eliminarMonitor(String id) {
        monitores.removeIf(m -> m.getId().equals(id));
    }

    public MonitorCCRS obtenerMonitor(String id) {
        return monitores.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void publicarEnTodos(String mensaje) {
        for (MonitorCCRS monitor : monitores) {
            monitor.publicarNotificacion(mensaje);
        }
    }
}
