package boundary;

import java.util.List;
import java.util.ArrayList;

public class InterfazNotificacionMail {
    private String destinatario; // Correos electrónicos de los responsables
    private String asunto;
    private String mensaje;

    public InterfazNotificacionMail() {
    }

    public void setDestinatarios(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void enviarNotificacion(String destinatario, String mensaje) {
        System.out.println("\nEnviando mail a: " + destinatario);
        // System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
        System.out.println("-------------------------");
    }
}
