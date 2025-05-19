import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import boundary.PantallaInspeccion;
import datos.RepositorioDatos;
import java.time.LocalDateTime;
import model.*;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear label
        Label labelBienvenida = new Label("Bienvenido al sistema");
        Button btnCerrarOrden = new Button("Generar Cierre Orden de Inspección");

        // Layout vertical
        VBox root = new VBox(10); // separación vertical

        // Instancio mi pantalla y le paso el root
        PantallaInspeccion pantalla = new PantallaInspeccion();
        pantalla.setRoot(root);

        btnCerrarOrden.setOnAction(event -> {
            System.out.println("Botón 'Generar Cierre Orden de Inspección' presionado");
            pantalla.opcionCerrarOrdenDeInspeccion();  // acá arranca el caso de uso
        });

        root.getChildren().addAll(labelBienvenida, btnCerrarOrden);

        // Escena
        Scene scene = new Scene(root, 400, 400);

        primaryStage.setTitle("Menú Principal - Gestión de Inspecciones");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Usuario usuario = RepositorioDatos.getUsuario();
        Sesion.getInstancia().iniciarSesion(usuario, LocalDateTime.now());

        launch(args);
    }
}