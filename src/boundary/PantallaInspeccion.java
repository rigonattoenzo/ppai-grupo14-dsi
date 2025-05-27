package boundary;

// Import del gestor y los modelos
import gestor.GestorCierreInspeccion;
import model.*;

// Import de los botones y etiquetas
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;
import java.util.concurrent.atomic.AtomicBoolean;

// import de utilidades de Java
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.IntStream;
import java.time.format.DateTimeFormatter;

public class PantallaInspeccion {
    // Gestor
    private GestorCierreInspeccion gestor;
    // Atributos de la pantalla
    private VBox root;
    private List<Map<String, Object>> ordenesActuales;
    // Para la entrada de texto
    private TextField campoObservacion;
    // Botón para confirmar la observación
    private Button btnEnviarObservacion;
    // lista de descripciones proveniente de mostrarMotivosTipoFueraServicio(...)
    private List<String> descripcionesMotivos;

    // Métodos de la pantalla
    // Constructor
    public PantallaInspeccion() {}

    // Setter para inyectar el VBox desde MainFX
    public void setRoot(VBox root) {
        this.root = root;
    }

    // Getter para obtener el gestor
    /*public GestorCierreInspeccion getGestor() {
        return gestor;
    }*/

    // Método utilizado para cancelar el caso de uso
    public void cancelarCasoUso() {
        // 1) Limpiar todo
        root.getChildren().clear();

        // 2) Mensaje de cancelación
        root.getChildren().add(new Label("Cierre de orden cancelada..."));

        // 3) Botón para cerrar la ventana
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
        btnCerrar.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });
        root.getChildren().add(btnCerrar);
    }

    // Métodos del caso de uso 37
    // PASO 1
    public void opcionCerrarOrdenDeInspeccion() {
        // Limpiamos la UI y lanzamos el CU
        root.getChildren().clear();
        habilitarVentana();
    }

    public void habilitarVentana() {
        this.gestor = new GestorCierreInspeccion(this); // Se crea el gestor
        gestor.iniciarCierreOrdenInspeccion();          // comienza el flujo del caso de uso
    }

    //PASO 2
    public void mostrarOrdCompRealizadas() {
        //Obtener los datos
        List<Map<String, Object>> ordenes = gestor.getOrdenesFiltradasConDatos();
        this.ordenesActuales = ordenes;

        // Limpiar todo antes de volcar las órdenes
        root.getChildren().clear();

        // Verificar
        if (ordenes.isEmpty()) {
            // Caso: no hay ordenes realizadas
            Label mensaje = new Label("No tiene ordenes de inspeccion realizadas!");
            Button btnCancelar = new Button("Cancelar cierre");
            btnCancelar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
            btnCancelar.setOnAction(e -> cancelarCasoUso());

            root.getChildren().addAll(mensaje, btnCancelar);
        } else {
            // Caso: hay ordenes realizadas, todo ok
            root.getChildren().add(new Label("Órdenes Completamente Realizadas del Empleado:"));
            for (Map<String, Object> datosOrden : ordenes) {
                root.getChildren().add(new Label(gestor.asString(datosOrden)));
            }
        }
    };


    /**
     * Muestra un botón por cada orden y espera al click
     */
    public void pedirSelecOrdenInspeccion(List<Map<String, Object>> ordenes) {
        // 1) Añado un separador o un título para los botones
        root.setStyle("-fx-background-color: #e7c6a6;");
        root.getChildren().add(new Label("Seleccione una orden de inspección:"));

        // 2) Por cada orden, un botón de selección
        for (Map<String, Object> o : ordenes) {
            String nro = String.valueOf(o.get("nroDeOrden"));
            String sismo = String.valueOf(o.get("idSismografo"));
            Button btn = new Button("Orden #" + nro + " (" + sismo + ")");
            btn.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-background-color: #b27e4d;");
            btn.setOnAction(evt -> tomarOrdenInspeccionSelec(o));
            root.getChildren().add(btn);
        }

        // 3) Botón de cancelar
        Button btnCancelar = new Button("Cancelar cierre");
        btnCancelar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
        btnCancelar.setOnAction(e -> cancelarCasoUso());
        root.getChildren().add(btnCancelar);
    }

    //PASO 3
    public void tomarOrdenInspeccionSelec(Map<String, Object> ordenSeleccionada) {
        gestor.tomarOrdenInspeccionSelec(ordenSeleccionada);
    }

    //PASO 4
    public void pedirObservacionCierreOrden() {
        // Limpio la UI
        root.getChildren().clear();
        root.setSpacing(15);
        root.setStyle("-fx-padding: 20; -fx-background-color: #e7c6a6;");

        // Mensaje de instrucción
        root.getChildren().add(new Label(
                "Ingrese una observación de cierre a la orden de inspección seleccionada:"
        ));

        // Campo de texto
        campoObservacion = new TextField();
        campoObservacion.setPromptText("Escriba su observación aquí...");
        campoObservacion.setStyle("-fx-background-color: #fae4cd; -fx-font-style: italic;");

        // Botón de enviar
        btnEnviarObservacion = new Button("Enviar Observación");
        btnEnviarObservacion.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #b27e4d;");

        btnEnviarObservacion.setOnAction(evt -> {
            tomarObservacionCierreOrden(campoObservacion.getText());
        });

        // Agrego todo al layout
        root.getChildren().addAll(campoObservacion, btnEnviarObservacion);

        Button btnCancelar = new Button("Cancelar cierre");
        btnCancelar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
        btnCancelar.setOnAction(e -> cancelarCasoUso());
        root.getChildren().add(btnCancelar);
    }

    //PASO 5
    public void tomarObservacionCierreOrden(String observacionCierre) {
        // Delego al gestor
        gestor.tomarObservacionCierreOrden(observacionCierre);
    }

    //PASO 6
    public void mostrarMotivosTipoFueraServicio(List<String> descrip) {
        this.descripcionesMotivos = descrip;

        // 1) Limpio lo que había
        root.getChildren().clear();

        // 2) Título
        root.getChildren().add(new Label("Motivos Fuera de Servicio:"));

        // 3) Cada motivo como Label numerado
        for (int i = 0; i < descrip.size(); i++) {
            String texto = String.format("%d. %s", i + 1, descrip.get(i));
            root.getChildren().add(new Label(texto));
        }

        Button btnCancelar = new Button("Cancelar cierre");
        btnCancelar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
        btnCancelar.setOnAction(e -> cancelarCasoUso());
        root.getChildren().add(btnCancelar);
    }

    /**
     * Reemplaza el println por un Label en la UI.
     */
    public void mostrarMensaje(String mensaje) {
        root.getChildren().add(new Label(mensaje));
    }

    /**
     * Muestra un ChoiceDialog con las descripciones numeradas,
     * bloquea hasta que el usuario elija una opción (o cierre).
     * Devuelve el número elegido (0 para terminar).
     */
    public int pedirSelecMotivoTipo() {
        int numMotivo = tomarMotivoTipoFueraServicio();
        return numMotivo;
    }

    //PASO 7
    public int tomarMotivoTipoFueraServicio() {
        int num = -1;

        do {
            List<String> opciones = IntStream
                    .rangeClosed(0, descripcionesMotivos.size())
                    .mapToObj(i -> i == 0
                            ? "0: Terminar selección"
                            : String.format("%d: %s", i, descripcionesMotivos.get(i - 1)))
                    .toList();

            Map<MotivoTipo, String> seleccionados = gestor.getMotivosYComentarios();
            String resumen = seleccionados.entrySet().stream()
                    .map(e -> "- " + e.getKey().getDescripcion() + ": " + e.getValue())
                    .collect(Collectors.joining("\n"));
            if (resumen.isEmpty()) {
                resumen = "(aún no hay motivos seleccionados)";
            }

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Seleccionar motivo");
            dialog.setHeaderText("YA SELECCIONADOS:\n" + resumen);

            DialogPane pane = dialog.getDialogPane();
            pane.setStyle("-fx-padding: 20; -fx-background-color: #e7c6a6;");

            // Botones personalizados
            ButtonType okButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            pane.getButtonTypes().addAll(okButtonType, cancelButtonType);

            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(opciones);
            comboBox.getSelectionModel().selectFirst();
            comboBox.setStyle("-fx-background-color: #fae4cd;");
            pane.setContent(comboBox);

            Button okButton = (Button) pane.lookupButton(okButtonType);
            okButton.setStyle("-fx-background-color: #8ee580; -fx-font-weight: bold;");
            Button cancelButton = (Button) pane.lookupButton(cancelButtonType);
            cancelButton.setStyle("-fx-background-color: #d95555; -fx-font-weight: bold; -fx-text-fill: white;");

            AtomicBoolean fueCancelado = new AtomicBoolean(false);

            // Acción personalizada para el botón cancelar
            cancelButton.addEventFilter(ActionEvent.ACTION, event -> {
                //System.out.print("se cancelaaaa\n");
                fueCancelado.set(true);
                event.consume(); // detiene el cierre automático del diálogo
                dialog.setResult(null); //  deja el resultado en null
                dialog.close(); // cierra manualmente
            });


            dialog.setResultConverter(button -> {
                if (button == okButtonType) {
                    //System.out.print("Dice que toco ok\n");
                    return comboBox.getValue();
                }
                return null;
            });

            Optional<String> resultado = dialog.showAndWait();

            if (resultado.isEmpty()) {
                if (fueCancelado.get()) {
                    cancelarCasoUso();// el usuario apretó "Cancelar"
                    return -1;

                } else {
                    opcionCerrarOrdenDeInspeccion();// cerró con la X
                }
                return 0;
            }

            String elegido = resultado.get();
            num = Integer.parseInt(elegido.split(":")[0]);

            if (num == 0 && gestor.getMotivosYComentarios().isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR,
                        "Debes seleccionar al menos un motivo antes de terminar.",
                        ButtonType.OK);
                alerta.setHeaderText("Selección incompleta");

                DialogPane paneAl = alerta.getDialogPane();
                paneAl.setStyle("-fx-background-color: #e7c6a6; -fx-font-size: 14px; -fx-padding: 20;");
                Button okBtn = (Button) paneAl.lookupButton(ButtonType.OK);
                okBtn.setStyle("-fx-background-color: #8ee580; -fx-font-weight: bold;");

                alerta.showAndWait();
                continue;
            }

            if (num != 0) {
                gestor.tomarMotivoTipoFueraServicio(num);
            }

            break;
        } while (true);

        return num;
    }


    /**
     * Muestra un TextInputDialog para ingresar el comentario;
     * bloquea hasta que el usuario confirme.
     */
    public void pedirComentario() {
        tomarComentario();
    }

    public void tomarComentario() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Comentario de motivo");
        dialog.setHeaderText("Ingrese un comentario para el motivo seleccionado:");

        // Para cambiar el color de fondo de la ventana
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-padding: 20; -fx-background-color: #e7c6a6;");

        // Cambiar el color del boton ok
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #8ee580; -fx-font-weight: bold;");

        // Cambiar el color/estilo del boton cancelar
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle("-fx-background-color: #d95555; -fx-font-weight: bold; -fx-text-fill: white;");

        // Cambiar el estilo de la caja de texto
        TextField textField = dialog.getEditor();
        textField.setStyle("-fx-control-inner-background: #fae4cd;; -fx-font-style: italic;");

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(comentario -> gestor.tomarComentario(comentario));
    }

    //PASO 8
    /**
     * Muestra una pregunta en pantalla y dos botones: 'SI' y 'CANCELAR'.
     * Al hacer clic en 'SI' llama a tomarConfirmacionCierreOrden("SI").
     */
    public void pedirConfirmacionCierreOrden() {
        // 1) Limpio la pantalla
        root.getChildren().clear();
        root.setStyle("-fx-padding: 20; -fx-background-color: #e7c6a6;");

        // 2) Mensaje de confirmación
        root.getChildren().add(new Label("¿Cerrar Orden de Inspección?"));

        // 3) Botón 'SI'
        Button btnSi = new Button("SI");
        btnSi.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #8ee580;");
        btnSi.setOnAction(e -> tomarConfirmacionCierreOrden("SI"));

        // 4) Botón 'CANCELAR'
        Button btnCancelar = new Button("CANCELAR");
        btnCancelar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #d95555; -fx-text-fill: white;");
        btnCancelar.setOnAction(e -> {
            // Vuelve al inicio del CU
            opcionCerrarOrdenDeInspeccion();
        });

        // 5) Agregar botones al layout
        VBox contenedorBotones = new VBox(10, btnSi, btnCancelar);
        root.getChildren().add(contenedorBotones);
    }

    //PASO 9 (último de PantallaInspección)
    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        // 1) Llamada original al gestor para que haga el cierre
        gestor.tomarConfirmacionCierreOrden(confirmacionCierre);

        // 2) Limpiar la pantalla y mostrar mensaje de éxito
        root.getChildren().clear();
        root.getChildren().add(new Label("La orden de inspección ha sido cerrada exitosamente."));

        // 3) Mostrar detalles de la orden
        Map<String,Object> datos = gestor.getOrdenSeleccionada();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        root.getChildren().add(new Label("Número de orden: "  + datos.get("nroDeOrden")));
        root.getChildren().add(new Label("Estación: "        + datos.get("nombreEstacionSismologica")));
        root.getChildren().add(new Label("Sismógrafo: "      + datos.get("idSismografo")));
        // Fecha de cierre fue seteada en el gestor, la obtenemos:
        String fechaCierre = gestor.getFechaHoraActual().format(fmt);
        root.getChildren().add(new Label("Fecha de cierre: " + fechaCierre));

        // 4) Botón para cerrar la ventana
        Button btnContinuar = new Button("Continuar");
        btnContinuar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #7c9e3f;");
        btnContinuar.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });
        root.getChildren().add(btnContinuar);
    }
}
