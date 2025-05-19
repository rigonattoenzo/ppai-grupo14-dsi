/*
// Import del gestor, el boundary, modelos y datos
import model.*;
import gestor.GestorCierreInspeccion;
import boundary.PantallaInspeccion;
import datos.RepositorioDatos;

// Import de utilidades de java
import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;

// Main
public class Main {
    public static void main(String[] args) {
        // Se inicia la sesión
        Usuario usuario = RepositorioDatos.getUsuario();
        Sesion.getInstancia().iniciarSesion(usuario, LocalDateTime.now());

        // Creamos la pantalla
        PantallaInspeccion pantalla = new PantallaInspeccion();

        // Disparamos el caso de uso
        pantalla.opcionCerrarOrdenDeInspeccion();
    }
}
*/