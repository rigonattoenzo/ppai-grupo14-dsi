package boundary;

public class MonitorCCRS {
    private String mensaje;

    public MonitorCCRS() {
    }

    public void publicarNotificacion(String mensaje) {
        // Simula mostrar un mensaje en los monitores
        System.out.println("\nMonitor CCRS:");
        System.out.println(mensaje);
        System.out.println("-------------------------");
    } // a esto va a haber que cambiarlo
}
