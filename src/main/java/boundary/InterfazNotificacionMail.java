package boundary;

public class InterfazNotificacionMail {
    private static InterfazNotificacionMail instancia;

    private InterfazNotificacionMail() {
    }

    public static InterfazNotificacionMail getInstancia() {
        if (instancia == null) {
            instancia = new InterfazNotificacionMail();
        }
        return instancia;
    }

    // ✅ Método correcto (sin parámetros repetidos)
    public void enviarNotificacion(String correo, String mensaje) {
        System.out.println("\n📧 Email enviado a: " + correo);
        System.out.println("─────────────────────────────");
        System.out.println(mensaje);
        System.out.println("─────────────────────────────");
    }
}
