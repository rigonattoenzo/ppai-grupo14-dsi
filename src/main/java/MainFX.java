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
import datos.persistence.LocalEntityManagerProvider;
import model.Usuario;
import model.Sesion;
import java.time.LocalDateTime;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // ✅ Obtener usuario desde BD
            Usuario usuario = RepositorioDatos.getUsuario();
            Sesion.getInstancia().iniciarSesion(usuario, LocalDateTime.now());

            String nombreRI = Sesion.getInstancia().getUsuario().perfilLogueado();
            Label labelBienvenida = new Label("Bienvenido al sistema " + nombreRI + "!");
            Button btnCerrarOrden = new Button("Generar Cierre Orden de Inspección");
            btnCerrarOrden.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #b27e4d;");

            VBox root = new VBox(20);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));
            root.setStyle("-fx-background-color: #e7c6a6;");

            PantallaInspeccion pantalla = new PantallaInspeccion();
            pantalla.setRoot(root);

            btnCerrarOrden.setOnAction(event -> {
                System.out.println("Botón 'Generar Cierre Orden de Inspección' presionado");
                pantalla.opcionCerrarOrdenDeInspeccion();
            });

            root.getChildren().addAll(labelBienvenida, btnCerrarOrden);

            Button btnCancelar = new Button("Cancelar cierre");
            btnCancelar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
            btnCancelar.setOnAction(e -> pantalla.cancelarCasoUso());
            root.getChildren().add(btnCancelar);

            Scene scene = new Scene(root, 500, 400);
            primaryStage.setTitle("Menú Principal - Gestión de Inspecciones");
            primaryStage.setScene(scene);

            primaryStage.setOnCloseRequest(e -> {
                LocalEntityManagerProvider.close();
                System.out.println("✅ Base de datos cerrada");
            });

            primaryStage.show();
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
