import model.*;
import gestor.GestorCierreInspeccion;
import boundary.PantallaInspeccion;
import datos.RepositorioDatos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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
