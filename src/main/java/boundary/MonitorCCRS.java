package boundary;

public class MonitorCCRS {
    private String id;
    private String nombre;

    public MonitorCCRS(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void publicarNotificacion(String mensaje) {
        // Simula mostrar un mensaje en los monitores
        System.out.println("\nMonitor CCRS:");
        System.out.println(mensaje);
        System.out.println("-------------------------");
    }
}
