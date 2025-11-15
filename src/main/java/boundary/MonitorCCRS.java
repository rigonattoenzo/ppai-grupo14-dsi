package boundary;

public class MonitorCCRS {
    private String mensaje;

    private static MonitorCCRS instancia;

    private MonitorCCRS() {
    }

    public static MonitorCCRS getInstancia() {
        if (instancia == null) {
            instancia = new MonitorCCRS();
        }
        return instancia;
    }

    public void publicarNotificacion(String mensaje) {
        // Simula mostrar un mensaje en los monitores
        System.out.println("\nMonitor CCRS:");
        System.out.println(mensaje);
        System.out.println("-------------------------");
    } // a esto va a haber que cambiarlo
}
