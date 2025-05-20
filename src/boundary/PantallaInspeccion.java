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

    // Scanner o "input"
    private Scanner scanner = new Scanner(System.in);

    // Atributos de la pantalla
    private VBox root;
    private List<Map<String,Object>> ordenesActuales;
    // Para la entrada de texto
    private TextField campoObservacion;
    // Botón para confirmar la observación
    private Button btnEnviarObservacion;
    // lista de descripciones proveniente de mostrarMotivosTipoFueraServicio(...)
    private List<String> descripcionesMotivos;

    // Métodos de la pantalla
    // Constructor
    public PantallaInspeccion() {}

    /** setter para inyectar el VBox desde MainFX */
    public void setRoot(VBox root) {
        this.root = root;
    }

    public GestorCierreInspeccion getGestor() {
        return gestor;
    }

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
    //PASO 1
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
        // Limpio todo antes de volcar las órdenes
        root.getChildren().clear();

        root.getChildren().add(new Label("Órdenes Completamente Realizadas del Empleado:"));
        for (Map<String, Object> datosOrden : gestor.getOrdenesFiltradasConDatos()) {
            root.getChildren().add(new Label(gestor.asString(datosOrden)));
        }
    }

    /**
     * Muestra un botón por cada orden y espera al click
     */
    public void pedirSelecOrdenInspeccion(List<Map<String,Object>> ordenes) {
        // 1) Muestro la lista de órdenes
        // mostrarOrdCompRealizadas();

        // 2) Añado un separador o un título para los botones
        root.setStyle("-fx-background-color: #e7c6a6;");
        root.getChildren().add(new Label("Seleccione una orden de inspección:"));

        // 3) Por cada orden, un botón de selección
        for (Map<String,Object> o : ordenes) {
            String nro = String.valueOf(o.get("nroDeOrden"));
            String sismo = String.valueOf(o.get("idSismografo"));
            Button btn = new Button("Orden #" + nro + " (" + sismo + ")");
            btn.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-background-color: #b27e4d;");
            btn.setOnAction(evt -> gestor.tomarOrdenInspeccionSelec(o));
            root.getChildren().add(btn);
        }

        // 4) Botón de cancelar
        Button btnCancelar = new Button("Cancelar cierre");
        btnCancelar.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: #c70039;");
        btnCancelar.setOnAction(e -> cancelarCasoUso());
        root.getChildren().add(btnCancelar);
    }

    //PASO 3
    public void tomarOrdenInspeccionSelec(Map<String,Object> ordenSeleccionada) {
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
        // 1) Genero la lista de opciones numeradas
        List<String> opciones = IntStream
                .rangeClosed(0, descripcionesMotivos.size())
                .mapToObj(i -> i == 0
                        ? "0: Terminar selección"
                        : String.format("%d: %s", i, descripcionesMotivos.get(i - 1))
                )
                .toList();

        // 2) Preparo el texto de resumen de lo ya seleccionado
        Map<MotivoTipo,String> seleccionados = gestor.getMotivosYComentarios();
        String resumen = seleccionados.entrySet().stream()
                .map(e -> "- " + e.getKey().getDescripcion() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));
        if (resumen.isEmpty()) {
            resumen = "(aún no hay motivos seleccionados)";
        }

        // 3) Construyo el diálogo con el resumen en el header
        ChoiceDialog<String> dialog = new ChoiceDialog<>(opciones.get(0), opciones);
        dialog.setTitle("Seleccionar motivo");
        dialog.setHeaderText("YA SELECCIONADOS:\n" + resumen);
        dialog.setContentText("Seleccione el número del motivo (0 para terminar):");

        //Para poder cambiar el color de la ventana
        DialogPane pane = dialog.getDialogPane();
        pane.setStyle("-fx-padding: 20; -fx-background-color: #e7c6a6;");

        //Cambio el color del boton ok
        Button okButton = (Button) pane.lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #8ee580; -fx-font-weight: bold;");

        //Cambio el color/estilo del boton cancelar
        Button cancelButton = (Button) pane.lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle("-fx-background-color: #d95555; -fx-font-weight: bold; -fx-text-fill: white;");

        // Buscar el ComboBox dentro del DialogPane y cambiarle el fondo
        pane.lookupAll(".combo-box").forEach(node -> {
            node.setStyle("-fx-background-color: #fae4cd;");
        });

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            String elegido = resultado.get();
            int num = Integer.parseInt(elegido.split(":")[0]);
            if (num != 0) {
                gestor.tomarMotivoTipoFueraServicio(num);
            }
            return num;
        } else {
            return 0;
        }
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

        //Para cambiar el color de fondo de la ventana
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-padding: 20; -fx-background-color: #e7c6a6;");

        //Cambio el color del boton ok
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle("-fx-background-color: #8ee580; -fx-font-weight: bold;");

        //Cambio el color/estilo del boton cancelar
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle("-fx-background-color: #d95555; -fx-font-weight: bold; -fx-text-fill: white;");

        //Cambiar el estilo de la caja de texto
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

        // 4) Botón 'CANCELAR' (opcional: vuelve al menú o fin)
        Button btnCancelar = new Button("CANCELAR");
        btnCancelar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #d95555; -fx-text-fill: white;");
        btnCancelar.setOnAction(e -> {
            // Por ejemplo, volvemos al inicio del CU:
            opcionCerrarOrdenDeInspeccion();
        });

        // 5) Agrego botones al layout
        VBox contenedorBotones = new VBox(10, btnSi, btnCancelar);
        root.getChildren().add(contenedorBotones);
    }

    /**
     * Muestra en la misma ventana un resumen de los motivos y comentarios
     * que el usuario ya ha seleccionado en el gestor, y un botón para confirmar.
     *//*
    public void mostrarResumenMotivosSeleccionados() {
        // 1) Limpiar todo lo anterior
        root.getChildren().clear();

        // 2) Título
        root.getChildren().add(new Label("Resumen de motivos seleccionados:"));

        // 3) Listar cada par motivo→comentario
        for (Map.Entry<MotivoTipo, String> entry : gestor.getMotivosYComentarios().entrySet()) {
            String texto = entry.getKey().getDescripcion()
                    + ": "
                    + entry.getValue();
            root.getChildren().add(new Label(texto));
        }

        // 4) Botón para confirmar el cierre
        Button btnConfirmar = new Button("Confirmar Cierre de Orden");
        btnConfirmar.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #b27e4d;");
        btnConfirmar.setOnAction(e -> {
            // Llamar al método de gestor que cierra la orden
            gestor.tomarConfirmacionCierreOrden("SI");
        });
        root.getChildren().add(btnConfirmar);
    }*/


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
