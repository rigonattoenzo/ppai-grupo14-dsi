import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;


import boundary.PantallaInspeccion;
import datos.RepositorioDatos;
import java.time.LocalDateTime;
import model.*;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear label
        String nombreRI = Sesion.getInstancia().getUsuario().perfilLogueado();
        Label labelBienvenida = new Label("Bienvenido al sistema " + nombreRI + "!");
        Button btnCerrarOrden = new Button("Generar Cierre Orden de Inspección");
        btnCerrarOrden.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #b27e4d;");

        // Layout vertical
        VBox root = new VBox(20); // separación vertical

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #e7c6a6;");

        // Instancio mi pantalla y le paso el root
        PantallaInspeccion pantalla = new PantallaInspeccion();
        pantalla.setRoot(root);

        btnCerrarOrden.setOnAction(event -> {
            System.out.println("Botón 'Generar Cierre Orden de Inspección' presionado");
            pantalla.opcionCerrarOrdenDeInspeccion();  // acá arranca el caso de uso
        });

        root.getChildren().addAll(labelBienvenida, btnCerrarOrden);

        Button btnCancelar = new Button("Cancelar cierre");
        btnCancelar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
        btnCancelar.setOnAction(e -> pantalla.cancelarCasoUso());
        root.getChildren().add(btnCancelar);

        // Escena
        Scene scene = new Scene(root, 500, 400);

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