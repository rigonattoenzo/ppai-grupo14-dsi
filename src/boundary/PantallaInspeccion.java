package boundary;

import gestor.GestorCierreInspeccion;
import model.*;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.time.format.DateTimeFormatter;

/**
 * Pantalla para el Caso de Uso 37: Cierre de Orden de Inspección
 * 
 * PRINCIPIOS DE DISEÑO APLICADOS:
 * - Nielsen #1: Visibilidad del estado (progress bar, títulos claros)
 * - Nielsen #3: Libertad y control (botón cancelar siempre visible)
 * - Nielsen #4: Consistencia (colores, fuentes, espaciado)
 * - Nielsen #5: Prevención de errores (validaciones antes de avanzar)
 * - Nielsen #8: Diseño minimalista (sin elementos innecesarios)
 * - Shneiderman: Coherencia visual, realimentación inmediata
 */
public class PantallaInspeccion {
    
    // ========== GESTOR ==========
    private GestorCierreInspeccion gestor;
    
    // ========== COMPONENTES UI ==========
    private VBox root;
    private BorderPane mainLayout;
    private ProgressBar progressBar;
    private Label labelEstado;
    
    // ========== DATOS ==========
    private List<Map<String, Object>> ordenesActuales;
    private List<String> descripcionesMotivos;
    private TextField campoObservacion;
    
    // ========== PALETA DE COLORES ==========
    // Colores consistentes en toda la aplicación (Nielsen #4)
    private static final String COLOR_FONDO = "#f5f1ed";
    private static final String COLOR_PRIMARIO = "#8b6f47";
    private static final String COLOR_SECUNDARIO = "#d4a574";
    private static final String COLOR_EXITO = "#7cb342";
    private static final String COLOR_PELIGRO = "#e53935";
    private static final String COLOR_ADVERTENCIA = "#fb8c00";
    private static final String COLOR_INFO = "#1976d2";
    
    // ========== FUENTES ==========
    private static final Font FUENTE_TITULO = Font.font("Segoe UI", FontWeight.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = Font.font("Segoe UI", FontWeight.BOLD, 14);
    private static final Font FUENTE_NORMAL = Font.font("Segoe UI", 12);
    private static final Font FUENTE_PEQUEÑA = Font.font("Segoe UI", 10);

    // ==================== CONSTRUCTOR ====================
    
    public PantallaInspeccion() {}

    public void setRoot(VBox root) {
        this.root = root;
    }

    // ==================== MÉTODOS AUXILIARES DE DISEÑO ====================
    
    /**
     * Crea el layout principal con header (progreso + estado) y contenido
     */
    private void inicializarLayout(int pasoActual, int totalPasos) {
        root.getChildren().clear();
        root.setStyle("-fx-background-color: " + COLOR_FONDO);
        root.setSpacing(0);
        root.setPadding(Insets.EMPTY);
        
        // Header con progreso
        VBox header = crearHeader(pasoActual, totalPasos);
        root.getChildren().add(header);
        
        // Contenido principal
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(30));
        contenido.setStyle("-fx-background-color: " + COLOR_FONDO);
        
        root.getChildren().add(contenido);
    }
    
    /**
     * Crea el header con barra de progreso y título
     * Nielsen #1: Visibilidad del estado del sistema
     */
    private VBox crearHeader(int pasoActual, int totalPasos) {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: " + COLOR_PRIMARIO);
        
        // Título de la aplicación
        Label titulo = new Label("Cierre de Orden de Inspección");
        titulo.setFont(FUENTE_TITULO);
        titulo.setTextFill(Color.WHITE);
        
        // Estado actual (Nielsen #1)
        labelEstado = new Label("Paso " + pasoActual + " de " + totalPasos);
        labelEstado.setFont(FUENTE_PEQUEÑA);
        labelEstado.setTextFill(Color.web("#d4a574"));
        
        // Barra de progreso (Nielsen #1)
        progressBar = new ProgressBar((double) pasoActual / totalPasos);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-accent: " + COLOR_EXITO);
        
        header.getChildren().addAll(titulo, progressBar, labelEstado);
        return header;
    }
    
    /**
     * Crea un panel de contenido estándar
     */
    private VBox crearPanelContenido() {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-border-color: " + COLOR_SECUNDARIO + "; -fx-border-width: 1; -fx-border-radius: 5;");
        panel.setPadding(new Insets(20));
        panel.setStyle(panel.getStyle() + "; -fx-background-color: white;");
        return panel;
    }
    
    /**
     * Crea un botón primario (acción principal)
     */
    private Button crearBotonPrimario(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
            "-fx-background-color: " + COLOR_PRIMARIO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 30;" +
            "-fx-border-radius: 5;" +
            "-fx-cursor: hand"
        );
        btn.setPrefWidth(250);
        return btn;
    }
    
    /**
     * Crea un botón secundario (acciones alternativas)
     */
    private Button crearBotonSecundario(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
            "-fx-background-color: " + COLOR_SECUNDARIO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-border-radius: 3;" +
            "-fx-cursor: hand"
        );
        btn.setPrefWidth(150);
        return btn;
    }
    
    /**
     * Crea un botón de cancelación (Nielsen #3: libertad y control)
     */
    private Button crearBotonCancelar() {
        Button btn = new Button("✕ Cancelar");
        btn.setStyle(
            "-fx-background-color: " + COLOR_PELIGRO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 20;" +
            "-fx-border-radius: 3;" +
            "-fx-cursor: hand"
        );
        btn.setOnAction(e -> cancelarCasoUso());
        return btn;
    }
    
    /**
     * Crea un Label con información
     */
    private Label crearLabel(String texto, Font fuente, Color color) {
        Label lbl = new Label(texto);
        lbl.setFont(fuente);
        lbl.setTextFill(color);
        lbl.setWrapText(true);
        return lbl;
    }
    
    /**
     * Crea un HBox con botones alineados (Nielsen #4: consistencia)
     */
    private HBox crearBarraBotones(Button... botones) {
        HBox barra = new HBox(15);
        barra.setAlignment(Pos.CENTER);
        barra.setPadding(new Insets(20, 0, 0, 0));
        for (Button btn : botones) {
            barra.getChildren().add(btn);
        }
        return barra;
    }

    // ==================== MÉTODOS DEL CASO DE USO ====================

    /**
     * PASO 1: Iniciar el cierre
     */
    public void opcionCerrarOrdenDeInspeccion() {
        root.getChildren().clear();
        habilitarVentana();
    }

    public void habilitarVentana() {
        this.gestor = new GestorCierreInspeccion(this);
        gestor.iniciarCierreOrdenInspeccion();
    }

    /**
     * PASO 2: Mostrar órdenes completamente realizadas
     * Nielsen #8: Minimalista - solo muestra lo necesario
     * Nielsen #1: Visibilidad - estado claro
     */
    public void mostrarOrdCompRealizadas() {
        List<Map<String, Object>> ordenes = gestor.getOrdenesFiltradasConDatos();
        this.ordenesActuales = ordenes;

        inicializarLayout(2, 9);
        
        VBox contenido = (VBox) root.getChildren().get(1);
        
        if (ordenes.isEmpty()) {
            // Caso: Sin órdenes (Flujo alternativo A1)
            VBox panel = crearPanelContenido();
            panel.setStyle(panel.getStyle() + "; -fx-border-color: " + COLOR_PELIGRO);
            
            Label icono = new Label("⚠");
            icono.setFont(new Font(40));
            
            Label titulo = crearLabel(
                "No hay órdenes para procesar",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PELIGRO)
            );
            
            Label descripcion = crearLabel(
                "Actualmente no posee órdenes de inspección en estado Completamente Realizada. " +
                "Por favor, verifique el estado de sus órdenes.",
                FUENTE_NORMAL,
                Color.web("#666")
            );
            
            panel.getChildren().addAll(icono, titulo, descripcion);
            contenido.getChildren().add(panel);
            
            Button btnCancelar = crearBotonCancelar();
            contenido.getChildren().add(btnCancelar);
            
        } else {
            // Caso: Órdenes disponibles
            Label titulo = crearLabel(
                "Seleccione una orden de inspección",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PRIMARIO)
            );
            contenido.getChildren().add(titulo);
            
            Label descripcion = crearLabel(
                "Se muestran las órdenes completamente realizadas ordenadas por fecha de finalización.",
                FUENTE_PEQUEÑA,
                Color.web("#999")
            );
            contenido.getChildren().add(descripcion);
            
            // Separador visual
            Separator sep = new Separator();
            contenido.getChildren().add(sep);
            
            // Mostrar órdenes como botones
            for (Map<String, Object> o : ordenes) {
                Button btnOrden = crearBotonOrden(o);
                contenido.getChildren().add(btnOrden);
            }
            
            // Barra de botones (Nielsen #3)
            Button btnCancelar = crearBotonCancelar();
            contenido.getChildren().add(crearBarraBotones(btnCancelar));
        }
    }
    
    /**
     * Crea un botón para cada orden de inspección
     * Nielsen #6: Reconocer antes que recordar
     */
    private Button crearBotonOrden(Map<String, Object> orden) {
        String nro = String.valueOf(orden.get("nroDeOrden"));
        String estacion = String.valueOf(orden.get("nombreEstacionSismologica"));
        String sismo = String.valueOf(orden.get("idSismografo"));
        
        VBox contenidoBoton = new VBox(5);
        contenidoBoton.setPadding(new Insets(12));
        
        Label lblOrden = new Label("Orden #" + nro);
        lblOrden.setFont(FUENTE_SUBTITULO);
        lblOrden.setTextFill(Color.web(COLOR_PRIMARIO));
        
        Label lblEstacion = new Label("📍 Estación: " + estacion);
        lblEstacion.setFont(FUENTE_NORMAL);
        
        Label lblSismo = new Label("📡 Sismógrafo: " + sismo);
        lblSismo.setFont(FUENTE_NORMAL);
        
        contenidoBoton.getChildren().addAll(lblOrden, lblEstacion, lblSismo);
        
        Button btn = new Button();
        btn.setGraphic(contenidoBoton);
        btn.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
            "-fx-border-width: 1;" +
            "-fx-padding: 10;" +
            "-fx-text-alignment: left;" +
            "-fx-cursor: hand"
        );
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setPrefHeight(80);
        
        btn.setOnAction(evt -> tomarOrdenInspeccionSelec(orden));
        
        return btn;
    }

    /**
     * PASO 3: Tomar orden seleccionada
     */
    public void tomarOrdenInspeccionSelec(Map<String, Object> ordenSeleccionada) {
        gestor.tomarOrdenInspeccionSelec(ordenSeleccionada);
    }

    /**
     * PASO 4-5: Pedir observación de cierre
     * Nielsen #7: Flexibilidad y eficiencia
     * Nielsen #8: Minimalista
     */
    public void pedirObservacionCierreOrden() {
        inicializarLayout(3, 9);
        
        VBox contenido = (VBox) root.getChildren().get(1);
        
        // Panel principal
        VBox panel = crearPanelContenido();
        
        Label titulo = crearLabel(
            "Observación de cierre",
            FUENTE_SUBTITULO,
            Color.web(COLOR_PRIMARIO)
        );
        
        Label descripcion = crearLabel(
            "Ingrese una observación sobre el cierre de esta orden de inspección. " +
            "Este campo es obligatorio.",
            FUENTE_PEQUEÑA,
            Color.web("#666")
        );
        
        // Campo de texto mejorado
        TextArea campoObservacion = new TextArea();
        campoObservacion.setPromptText("Escriba sus observaciones aquí...");
        campoObservacion.setWrapText(true);
        campoObservacion.setPrefRowCount(5);
        campoObservacion.setStyle(
            "-fx-control-inner-background: #fafafa;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 12px;" +
            "-fx-border-color: " + COLOR_SECUNDARIO
        );
        
        // TextArea es mejor para observaciones largas
        TextArea areaObservacion = new TextArea();
        areaObservacion.setPromptText("Escriba sus observaciones aquí...");
        areaObservacion.setWrapText(true);
        areaObservacion.setPrefRowCount(5);
        areaObservacion.setStyle(
            "-fx-control-inner-background: #fafafa;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 12px;" +
            "-fx-border-color: " + COLOR_SECUNDARIO
        );
        
        panel.getChildren().addAll(titulo, descripcion, areaObservacion);
        contenido.getChildren().add(panel);
        
        // Botones
        Button btnEnviar = crearBotonPrimario("Continuar");
        btnEnviar.setOnAction(evt -> {
            if (areaObservacion.getText().trim().isEmpty()) {
                mostrarError("La observación es obligatoria", "Por favor, ingrese una observación antes de continuar.");
                return;
            }
            tomarObservacionCierreOrden(areaObservacion.getText());
        });
        
        Button btnCancelar = crearBotonCancelar();
        
        contenido.getChildren().add(crearBarraBotones(btnEnviar, btnCancelar));
    }

    /**
     * PASO 5: Tomar observación
     */
    public void tomarObservacionCierreOrden(String observacion) {
        gestor.tomarObservacionCierreOrden(observacion);
    }

    /**
     * PASO 6: Mostrar motivos para fuera de servicio
     */
    public void mostrarMotivosTipoFueraServicio(List<String> descripciones) {
        this.descripcionesMotivos = descripciones;

        inicializarLayout(4, 9);
        
        VBox contenido = (VBox) root.getChildren().get(1);
        
        VBox panel = crearPanelContenido();
        
        Label titulo = crearLabel(
            "Motivos de Fuera de Servicio",
            FUENTE_SUBTITULO,
            Color.web(COLOR_PRIMARIO)
        );
        
        Label descripcion = crearLabel(
            "Seleccione uno o varios motivos por los cuales el sismógrafo se pone fuera de servicio. " +
            "Para cada motivo debe ingresar un comentario.",
            FUENTE_PEQUEÑA,
            Color.web("#666")
        );
        
        panel.getChildren().addAll(titulo, descripcion);
        
        // Separador
        Separator sep = new Separator();
        panel.getChildren().add(sep);
        
        // Lista de motivos con numeración
        for (int i = 0; i < descripciones.size(); i++) {
            Label lblMotivo = new Label((i + 1) + ". " + descripciones.get(i));
            lblMotivo.setFont(FUENTE_NORMAL);
            lblMotivo.setTextFill(Color.web("#333"));
            panel.getChildren().add(lblMotivo);
        }
        
        contenido.getChildren().add(panel);
        
        // Botón para continuar
        Button btnSeleccionar = crearBotonPrimario("Seleccionar motivos");
        btnSeleccionar.setOnAction(e -> pedirSelecMotivosFueraServicio());
        
        Button btnCancelar = crearBotonCancelar();
        
        contenido.getChildren().add(crearBarraBotones(btnSeleccionar, btnCancelar));
    }

    /**
     * PASO 7: Pedir selección de motivos
     * Nielsen #5: Prevención de errores
     */
    public void pedirSelecMotivosFueraServicio() {
        int motivoNum = tomarMotivoTipoFueraServicio();
    }

    public int tomarMotivoTipoFueraServicio() {
        int num = -1;

        do {
            List<String> opciones = IntStream
                    .rangeClosed(0, descripcionesMotivos.size())
                    .mapToObj(i -> i == 0
                            ? "0: Finalizar selección"
                            : String.format("%d: %s", i, descripcionesMotivos.get(i - 1)))
                    .toList();

            Map<MotivoTipo, String> seleccionados = gestor.getMotivosYComentarios();
            String resumen = seleccionados.entrySet().stream()
                    .map(e -> "✓ " + e.getKey().getDescripcion() + ": \"" + e.getValue() + "\"")
                    .collect(Collectors.joining("\n"));
            if (resumen.isEmpty()) {
                resumen = "(aún no hay motivos seleccionados)";
            }

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Seleccionar Motivo");
            dialog.setHeaderText("MOTIVOS SELECCIONADOS:\n" + resumen);

            DialogPane pane = dialog.getDialogPane();
            pane.setStyle(
                "-fx-padding: 20;" +
                "-fx-background-color: " + COLOR_FONDO + ";" +
                "-fx-font-size: 12px"
            );

            // Botones
            ButtonType okButtonType = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            pane.getButtonTypes().addAll(okButtonType, cancelButtonType);

            // ComboBox para seleccionar
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(opciones);
            comboBox.getSelectionModel().selectFirst();
            comboBox.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
                "-fx-padding: 8"
            );
            comboBox.setPrefWidth(400);
            pane.setContent(comboBox);

            // Estilo botones
            Button okButton = (Button) pane.lookupButton(okButtonType);
            okButton.setStyle(
                "-fx-background-color: " + COLOR_EXITO + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold"
            );
            Button cancelButton = (Button) pane.lookupButton(cancelButtonType);
            cancelButton.setStyle(
                "-fx-background-color: " + COLOR_PELIGRO + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold"
            );

            AtomicBoolean fueCancelado = new AtomicBoolean(false);

            cancelButton.addEventFilter(ActionEvent.ACTION, event -> {
                fueCancelado.set(true);
                event.consume();
                dialog.setResult(null);
                dialog.close();
            });

            dialog.setResultConverter(button -> {
                if (button == okButtonType) {
                    return comboBox.getValue();
                }
                return null;
            });

            Optional<String> resultado = dialog.showAndWait();

            if (resultado.isEmpty()) {
                if (fueCancelado.get()) {
                    cancelarCasoUso();
                    return -1;
                } else {
                    opcionCerrarOrdenDeInspeccion();
                }
                return 0;
            }

            String elegido = resultado.get();
            num = Integer.parseInt(elegido.split(":")[0]);

            if (num == 0 && gestor.getMotivosYComentarios().isEmpty()) {
                mostrarError(
                    "Selección incompleta",
                    "Debe seleccionar al menos un motivo antes de finalizar."
                );
                continue;
            }

            if (num != 0) {
                gestor.tomarMotivoTipoFueraServicio(num);
                pedirComentario();
            }

            break;
        } while (true);

        return num;
    }

    /**
     * PASO 7b: Pedir comentario para el motivo
     */
    public void pedirComentario() {
        tomarComentario();
    }

    public void tomarComentario() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Comentario del Motivo");
        dialog.setHeaderText("Ingrese un comentario para el motivo seleccionado:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle(
            "-fx-padding: 20;" +
            "-fx-background-color: " + COLOR_FONDO
        );

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(
            "-fx-background-color: " + COLOR_EXITO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold"
        );

        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        cancelButton.setStyle(
            "-fx-background-color: " + COLOR_PELIGRO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold"
        );

        TextField textField = dialog.getEditor();
        textField.setStyle(
            "-fx-control-inner-background: white;" +
            "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
            "-fx-padding: 8;" +
            "-fx-font-size: 12px"
        );

        Optional<String> resultado = dialog.showAndWait();
        resultado.ifPresent(comentario -> gestor.tomarComentario(comentario));
    }

    /**
     * PASO 8: Pedir confirmación
     * Nielsen #1: Visibilidad
     * Nielsen #3: Libertad y control
     */
    public void pedirConfirmacionCierreOrden() {
        inicializarLayout(8, 9);
        
        VBox contenido = (VBox) root.getChildren().get(1);
        
        VBox panel = crearPanelContenido();
        panel.setStyle(panel.getStyle() + "; -fx-border-color: " + COLOR_ADVERTENCIA);
        
        Label icono = new Label("⚠");
        icono.setFont(new Font(30));
        
        Label titulo = crearLabel(
            "Confirmar cierre de orden",
            FUENTE_SUBTITULO,
            Color.web(COLOR_PRIMARIO)
        );
        
        Label descripcion = crearLabel(
            "¿Está seguro de que desea cerrar esta orden de inspección? " +
            "Esta acción pondrá el sismógrafo fuera de servicio.",
            FUENTE_NORMAL,
            Color.web("#666")
        );
        
        panel.getChildren().addAll(icono, titulo, descripcion);
        contenido.getChildren().add(panel);
        
        // Botones
        Button btnConfirmar = crearBotonPrimario("✓ Confirmar cierre");
        btnConfirmar.setStyle(
            btnConfirmar.getStyle() +
            "; -fx-background-color: " + COLOR_EXITO
        );
        btnConfirmar.setOnAction(e -> tomarConfirmacionCierreOrden("SI"));
        
        Button btnCancelar = crearBotonCancelar();
        
        contenido.getChildren().add(crearBarraBotones(btnConfirmar, btnCancelar));
    }

    /**
     * PASO 9: Tomar confirmación
     */
    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        gestor.tomarConfirmacionCierreOrden(confirmacionCierre);

        inicializarLayout(9, 9);
        
        VBox contenido = (VBox) root.getChildren().get(1);
        
        VBox panel = crearPanelContenido();
        panel.setStyle(panel.getStyle() + "; -fx-border-color: " + COLOR_EXITO);
        
        Label icono = new Label("✓");
        icono.setFont(new Font(50));
        icono.setTextFill(Color.web(COLOR_EXITO));
        
        Label titulo = crearLabel(
            "¡Cierre completado exitosamente!",
            FUENTE_SUBTITULO,
            Color.web(COLOR_EXITO)
        );
        
        // ✅ AGREGAR ESTA VALIDACIÓN:
        Map<String, Object> datos = gestor.getOrdenSeleccionada();
        if (datos == null) {
            Label error = crearLabel(
                "Error al recuperar los datos de la orden",
                FUENTE_NORMAL,
                Color.web(COLOR_PELIGRO)
            );
            panel.getChildren().addAll(icono, titulo, error);
            contenido.getChildren().add(panel);
            
            Button btnCerrar = crearBotonPrimario("Cerrar");
            btnCerrar.setOnAction(e -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            });
            contenido.getChildren().add(crearBarraBotones(btnCerrar));
            return;  // ✅ SALIR AQUÍ
        }
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaCierre = gestor.getFechaHoraActual().format(fmt);
        
        VBox resumen = new VBox(10);
        resumen.setPadding(new Insets(15));
        resumen.setStyle("-fx-border-color: #eee; -fx-border-width: 1; -fx-background-color: #fafafa");
        
        // ✅ AHORA datos NO ES NULL, así que es seguro usar:
        resumen.getChildren().addAll(
            crearLabel("📋 Orden #" + datos.get("nroDeOrden"), FUENTE_NORMAL, Color.web(COLOR_PRIMARIO)),
            crearLabel("📍 Estación: " + datos.get("nombreEstacionSismologica"), FUENTE_NORMAL, Color.web("#333")),
            crearLabel("📡 Sismógrafo: " + datos.get("idSismografo"), FUENTE_NORMAL, Color.web("#333")),
            crearLabel("🕐 Cierre: " + fechaCierre, FUENTE_NORMAL, Color.web("#333"))
        );
        
        panel.getChildren().addAll(icono, titulo, resumen);
        contenido.getChildren().add(panel);
        
        // Botón de cierre
        Button btnContinuar = crearBotonPrimario("Cerrar");
        btnContinuar.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });
        
        contenido.getChildren().add(crearBarraBotones(btnContinuar));
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Muestra un mensaje de error
     * Nielsen #9: Ayuda a reconocer errores
     */
    public void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        
        DialogPane pane = alert.getDialogPane();
        pane.setStyle(
            "-fx-background-color: " + COLOR_FONDO + ";" +
            "-fx-padding: 20"
        );
        
        Button okButton = (Button) pane.lookupButton(ButtonType.OK);
        okButton.setStyle(
            "-fx-background-color: " + COLOR_EXITO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold"
        );
        
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje informativo
     */
    public void mostrarMensaje(String mensaje) {
        VBox contenido = (VBox) root.getChildren().get(1);
        Label lbl = crearLabel(mensaje, FUENTE_PEQUEÑA, Color.web("#666"));
        contenido.getChildren().add(lbl);
    }

    /**
     * Cancela el caso de uso completamente
     * Nielsen #3: Libertad y control
     */
    public void cancelarCasoUso() {
        root.getChildren().clear();
        root.setSpacing(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + COLOR_FONDO);

        VBox panel = new VBox(20);
        panel.setAlignment(Pos.CENTER);
        panel.setStyle(
            "-fx-border-color: " + COLOR_ADVERTENCIA + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-color: white;" +
            "-fx-padding: 30"
        );

        Label icono = new Label("⚠");
        icono.setFont(new Font(40));
        icono.setTextFill(Color.web(COLOR_ADVERTENCIA));

        Label titulo = crearLabel(
            "Cierre cancelado",
            FUENTE_SUBTITULO,
            Color.web(COLOR_ADVERTENCIA)
        );

        Label descripcion = crearLabel(
            "La operación de cierre ha sido cancelada. " +
            "No se realizaron cambios en el sistema.",
            FUENTE_NORMAL,
            Color.web("#666")
        );

        panel.getChildren().addAll(icono, titulo, descripcion);
        root.getChildren().add(panel);

        Button btnCerrar = crearBotonPrimario("Cerrar ventana");
        btnCerrar.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });
        root.getChildren().add(btnCerrar);
    }
}
