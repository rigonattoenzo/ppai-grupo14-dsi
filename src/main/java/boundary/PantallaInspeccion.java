package boundary;

import gestor.GestorCierreInspeccion;
import model.*;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
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
    private ProgressBar progressBar;
    private Label labelEstado;
    private ScrollPane scrollActual;

    // ========== DATOS ==========
    private List<String> descripcionesMotivos;
    private List<MotivoTipo> punteroMotivos;
    private Map<MotivoTipo, String> motivosYComentariosLocal = new HashMap<>();
    private VBox listaMotivosSeleccionadosVBox;
    private Label lblVacioMotivos;
    private String comentarioTemporal;

    // ========== PALETA DE COLORES ==========
    private static final String COLOR_FONDO = "#faf6f1"; // Beige muy claro
    private static final String COLOR_PRIMARIO = "#8b6f47"; // Marrón oscuro
    private static final String COLOR_SECUNDARIO = "#d4a574"; // Marrón medio
    private static final String COLOR_EXITO = "#d4af37"; // Oro (prevención positiva)
    private static final String COLOR_PELIGRO = "#d97706"; // Ámbar oscuro (cuidado)
    private static final String COLOR_ADVERTENCIA = "#f59e0b"; // Ámbar claro (precaución)
    private static final String COLOR_RECHAZAR = "#ea580c";

    // ========== FUENTES ==========
    private static final Font FUENTE_TITULO = Font.font("Segoe UI", FontWeight.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = Font.font("Segoe UI", FontWeight.BOLD, 14);
    private static final Font FUENTE_NORMAL = Font.font("Segoe UI", 12);
    private static final Font FUENTE_PEQUEÑA = Font.font("Segoe UI", 10);

    // ==================== CONSTRUCTOR ====================

    public PantallaInspeccion() {
    }

    public void setRoot(VBox root) {
        this.root = root;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void inicializarLayout(int pasoActual, int totalPasos) {
        root.getChildren().clear();
        root.setStyle("-fx-background-color: " + COLOR_FONDO);
        root.setSpacing(0);
        root.setPadding(Insets.EMPTY);

        VBox header = crearHeader(pasoActual, totalPasos);
        root.getChildren().add(header);

        scrollActual = new ScrollPane();
        scrollActual.setStyle("-fx-background-color: " + COLOR_FONDO);
        scrollActual.setFitToWidth(true);
        VBox.setVgrow(scrollActual, javafx.scene.layout.Priority.ALWAYS);

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(30));
        contenido.setStyle("-fx-background-color: " + COLOR_FONDO);

        scrollActual.setContent(contenido);
        root.getChildren().add(scrollActual);
    }

    private VBox crearHeader(int pasoActual, int totalPasos) {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: " + COLOR_PRIMARIO);

        Label titulo = new Label("Cierre de Orden de Inspección");
        titulo.setFont(FUENTE_TITULO);
        titulo.setTextFill(Color.WHITE);

        labelEstado = new Label("Paso " + pasoActual + " de " + totalPasos);
        labelEstado.setFont(FUENTE_PEQUEÑA);
        labelEstado.setTextFill(Color.web("#d4a574"));

        progressBar = new ProgressBar((double) pasoActual / totalPasos);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-accent: " + COLOR_EXITO);

        header.getChildren().addAll(titulo, progressBar, labelEstado);
        return header;
    }

    private VBox crearPanelContenido() {
        VBox panel = new VBox(15);
        panel.setStyle(
                "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-color: white;" +
                        "-fx-padding: 20");
        return panel;
    }

    private Button crearBotonPrimario(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: " + COLOR_PRIMARIO + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 30;" +
                        "-fx-border-radius: 5;" +
                        "-fx-cursor: hand");
        btn.setPrefWidth(250);
        return btn;
    }

    private Button crearBotonSecundario(String texto) {
        Button btn = new Button(texto);
        btn.setStyle(
                "-fx-background-color: " + COLOR_SECUNDARIO + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8 20;" +
                        "-fx-border-radius: 3;" +
                        "-fx-cursor: hand");
        btn.setPrefWidth(150);
        return btn;
    }

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
                        "-fx-cursor: hand");
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setPrefHeight(80);

        btn.setOnAction(evt -> gestor.pedirSelecOrdenInspeccion(orden));

        return btn;
    }

    private Button crearBotonCancelar() {
        Button btn = new Button("✕ Cancelar");
        btn.setStyle(
                "-fx-background-color: " + COLOR_PELIGRO + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8 20;" +
                        "-fx-border-radius: 3;" +
                        "-fx-cursor: hand");
        btn.setOnAction(e -> cancelarCasoUso());
        return btn;
    }

    private Label crearLabel(String texto, Font fuente, Color color) {
        Label lbl = new Label(texto);
        lbl.setFont(fuente);
        lbl.setTextFill(color);
        lbl.setWrapText(true);
        return lbl;
    }

    private HBox crearBarraBotones(Button... botones) {
        HBox barra = new HBox(15);
        barra.setAlignment(Pos.CENTER);
        barra.setPadding(new Insets(20, 0, 0, 0));

        for (Button btn : botones) {
            barra.getChildren().add(btn);
        }

        return barra;
    }

    private VBox obtenerVBoxContenido() {
        ScrollPane scroll = (ScrollPane) root.getChildren().get(1);
        return (VBox) scroll.getContent();
    }

    // ==================== MÉTODOS DEL CASO DE USO ====================
    /**
     * PASO 1: El RI selecciona la opción "Cerrar Orden de Inspección"
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
     */
    public void mostrarOrdCompRealizadas(List<Map<String, Object>> ordenes) {
        inicializarLayout(2, 9);

        VBox contenido = obtenerVBoxContenido();

        if (ordenes.isEmpty()) {
            // ALTERNATIVA 1: Sin órdenes
            VBox panel = crearPanelContenido();
            panel.setStyle(panel.getStyle() + "; -fx-border-color: " + COLOR_PELIGRO);

            Label icono = new Label("⚠");
            icono.setFont(new Font(40));

            Label titulo = crearLabel(
                    "No hay órdenes para procesar",
                    FUENTE_SUBTITULO,
                    Color.web(COLOR_PELIGRO));

            Label descripcion = crearLabel(
                    "Actualmente no posee órdenes de inspección en estado Completamente Realizada.",
                    FUENTE_NORMAL,
                    Color.web("#666"));

            panel.getChildren().addAll(icono, titulo, descripcion);
            contenido.getChildren().add(panel);

            Button btnCancelar = crearBotonCancelar();
            contenido.getChildren().add(btnCancelar);

        } else {
            // FLUJO NORMAL: Mostrar órdenes para seleccionar
            Label titulo = crearLabel(
                    "Seleccione una orden de inspección",
                    FUENTE_SUBTITULO,
                    Color.web(COLOR_PRIMARIO));
            contenido.getChildren().add(titulo);

            Label descripcion = crearLabel(
                    "Se muestran las órdenes completamente realizadas ordenadas por fecha.",
                    FUENTE_PEQUEÑA,
                    Color.web("#999"));
            contenido.getChildren().add(descripcion);

            Separator sep = new Separator();
            contenido.getChildren().add(sep);

            // Crear botones de órdenes
            // Dentro del método de crearBotonOrden() se llama a pedirSelecOrdenInspeccion()
            // del gestor
            for (Map<String, Object> orden : ordenes) {
                Button btnOrden = crearBotonOrden(orden);
                contenido.getChildren().add(btnOrden);
            }

            Button btnCancelar = crearBotonCancelar();
            contenido.getChildren().add(crearBarraBotones(btnCancelar));
        }
    }

    public void pedirSelecOrdenInspeccion(Map<String, Object> orden) {
        tomarOrdenInspeccionSelec(orden);
    }

    public void tomarOrdenInspeccionSelec(Map<String, Object> ordenSeleccionada) {
        gestor.tomarOrdenInspeccionSelec(ordenSeleccionada);
    }

    /**
     * PASO 4: Pedir observación
     */
    public void pedirObservacionCierreOrden() {
        inicializarLayout(3, 9);

        VBox contenido = obtenerVBoxContenido();

        VBox panel = crearPanelContenido();

        Label titulo = crearLabel(
                "Observación de cierre",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PRIMARIO));

        Label descripcion = crearLabel(
                "Ingrese una observación sobre el cierre de esta orden de inspección. " +
                        "Este campo es obligatorio.",
                FUENTE_PEQUEÑA,
                Color.web("#666"));

        TextArea areaObservacion = new TextArea();
        areaObservacion.setPromptText("Escriba sus observaciones aquí...");
        areaObservacion.setWrapText(true);
        areaObservacion.setPrefRowCount(5);
        areaObservacion.setStyle(
                "-fx-control-inner-background: #fafafa;" +
                        "-fx-padding: 10;" +
                        "-fx-font-size: 12px;" +
                        "-fx-border-color: " + COLOR_SECUNDARIO);

        panel.getChildren().addAll(titulo, descripcion, areaObservacion);
        contenido.getChildren().add(panel);

        Button btnEnviar = crearBotonPrimario("Continuar");
        btnEnviar.setOnAction(evt -> {
            if (areaObservacion.getText().trim().isEmpty()) {
                mostrarError("La observación es obligatoria",
                        "Por favor, ingrese una observación antes de continuar.");
                return;
            }
            tomarObservacionCierreOrden(areaObservacion.getText());
        });

        Button btnCancelar = crearBotonCancelar();

        contenido.getChildren().add(crearBarraBotones(btnEnviar, btnCancelar));
    }

    public void tomarObservacionCierreOrden(String observacion) {
        gestor.tomarObservacionCierreOrden(observacion);
    }

    /**
     * PASO 6: Mostrar motivos
     */
    public void mostrarMotivosTipoFueraServicio(List<String> descripciones, List<MotivoTipo> punteros) {
        this.descripcionesMotivos = descripciones;
        this.punteroMotivos = punteros;
        this.motivosYComentariosLocal.clear();

        inicializarLayout(4, 9);

        VBox contenido = obtenerVBoxContenido();

        VBox panelMotivos = crearPanelContenido();

        Label titulo = crearLabel(
                "Seleccione Motivos de Fuera de Servicio",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PRIMARIO));

        Label descripcion = crearLabel(
                "Seleccione uno o varios motivos y agregue un comentario para cada uno. " +
                        "Debe seleccionar al menos un motivo.",
                FUENTE_PEQUEÑA,
                Color.web("#666"));

        panelMotivos.getChildren().addAll(titulo, descripcion);

        Separator sep1 = new Separator();
        panelMotivos.getChildren().add(sep1);

        Label lblMotivosTitulo = crearLabel(
                "Motivos disponibles:",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PRIMARIO));
        panelMotivos.getChildren().add(lblMotivosTitulo);

        VBox listaMotivosDisponibles = new VBox(8);
        listaMotivosDisponibles.setPadding(new Insets(10));
        listaMotivosDisponibles.setStyle("-fx-border-color: #eee; -fx-border-width: 1; -fx-background-color: #fafafa");

        for (int i = 0; i < descripciones.size(); i++) {
            Label lblMotivo = new Label((i + 1) + ". " + descripciones.get(i));
            lblMotivo.setFont(FUENTE_NORMAL);
            lblMotivo.setTextFill(Color.web(COLOR_PRIMARIO));
            listaMotivosDisponibles.getChildren().add(lblMotivo);
        }

        panelMotivos.getChildren().add(listaMotivosDisponibles);

        Separator sep2 = new Separator();
        panelMotivos.getChildren().add(sep2);

        Label lblSeleccionados = crearLabel(
                "Motivos seleccionados:",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PRIMARIO));
        panelMotivos.getChildren().add(lblSeleccionados);

        VBox listaMotivosSeleccionados = new VBox(10);
        listaMotivosSeleccionados.setPadding(new Insets(10));
        listaMotivosSeleccionados.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-color: white");

        Label lblVacio = new Label("(No hay motivos seleccionados aún)");
        lblVacio.setFont(FUENTE_PEQUEÑA);
        lblVacio.setTextFill(Color.web("#999"));
        listaMotivosSeleccionados.getChildren().add(lblVacio);

        panelMotivos.getChildren().add(listaMotivosSeleccionados);

        this.listaMotivosSeleccionadosVBox = listaMotivosSeleccionados;
        this.lblVacioMotivos = lblVacio;

        contenido.getChildren().add(panelMotivos);

        Button btnContinuar = crearBotonPrimario("Continuar");
        btnContinuar.setOnAction(e -> {
            if (motivosYComentariosLocal.isEmpty()) {
                mostrarError("Motivos requeridos",
                        "Debe seleccionar al least un motivo antes de continuar.");
                return;
            }
            // Continúa el caso de uso
            gestor.pedirConfirmacionCierreOrden();
        });

        Button btnCancelar = crearBotonCancelar();

        contenido.getChildren().add(crearBarraBotones(btnContinuar, btnCancelar));

        pedirSelecMotivoTipo();
    }

    /**
     * PASO 7: Pedir selección de motivos
     */
    public void pedirSelecMotivoTipo() {
        VBox contenido = obtenerVBoxContenido();

        VBox panelPrincipal = null;
        for (javafx.scene.Node node : contenido.getChildren()) {
            if (node instanceof VBox) {
                panelPrincipal = (VBox) node;
                break;
            }
        }

        if (panelPrincipal == null)
            return;

        VBox panelSeleccion = new VBox(15);
        panelSeleccion.setPadding(new Insets(15));
        panelSeleccion.setStyle(
                "-fx-border-color: " + COLOR_EXITO + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-color: #f0f8f0");
        panelSeleccion.setId("panelSeleccion");

        Label lblSeleccionar = crearLabel(
                "Seleccionar nuevo motivo:",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PRIMARIO));
        panelSeleccion.getChildren().add(lblSeleccionar);

        ComboBox<String> comboMotivos = new ComboBox<>();
        actualizarComboMotivos(comboMotivos, descripcionesMotivos,
                new ArrayList<>(motivosYComentariosLocal.keySet()));

        comboMotivos.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 12px");
        comboMotivos.setPrefWidth(Double.MAX_VALUE);

        panelSeleccion.getChildren().add(comboMotivos);

        Label lblComentario = crearLabel("Comentario:", FUENTE_NORMAL, Color.web(COLOR_PRIMARIO));
        panelSeleccion.getChildren().add(lblComentario);

        TextArea areaComentario = new TextArea();
        areaComentario.setPromptText("Ingrese un comentario descriptivo para este motivo...");
        areaComentario.setWrapText(true);
        areaComentario.setPrefRowCount(3);
        areaComentario.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-padding: 8;" +
                        "-fx-font-size: 12px;" +
                        "-fx-border-color: " + COLOR_SECUNDARIO);
        panelSeleccion.getChildren().add(areaComentario);

        Button btnAgregar = new Button("+ Agregar motivo");
        btnAgregar.setStyle(
                "-fx-background-color: " + COLOR_EXITO + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 15;" +
                        "-fx-border-radius: 3;" +
                        "-fx-cursor: hand");
        btnAgregar.setPrefWidth(200);

        HBox hboxAgregar = new HBox(10);
        hboxAgregar.setAlignment(Pos.CENTER_LEFT);
        hboxAgregar.getChildren().add(btnAgregar);
        panelSeleccion.getChildren().add(hboxAgregar);

        btnAgregar.setOnAction(e -> {
            String motivoSeleccionado = comboMotivos.getValue();
            String comentario = areaComentario.getText().trim();

            if (motivoSeleccionado == null || motivoSeleccionado.isEmpty()) {
                mostrarError("Seleccione un motivo",
                        "Por favor, seleccione un motivo de la lista.");
                return;
            }

            if (comentario.isEmpty()) {
                mostrarError("Comentario requerido",
                        "Por favor, ingrese un comentario para este motivo.");
                return;
            }

            int indiceMotivo = Integer.parseInt(motivoSeleccionado.split(":")[0]) - 1;
            // Continúa el caso de uso
            tomarMotivoTipoFueraServicio(indiceMotivo + 1, comentario);

            areaComentario.clear();

            List<MotivoTipo> nuevosSeleccionados = new ArrayList<>(motivosYComentariosLocal.keySet());
            actualizarComboMotivos(comboMotivos, descripcionesMotivos, nuevosSeleccionados);

            comboMotivos.getSelectionModel().selectFirst();
        });

        int indiceInsercion = panelPrincipal.getChildren().indexOf(listaMotivosSeleccionadosVBox);
        if (indiceInsercion > 0) {
            panelPrincipal.getChildren().add(indiceInsercion, panelSeleccion);
        }
    }

    public void tomarMotivoTipoFueraServicio(int indiceMotivo, String comentario) {
        this.comentarioTemporal = comentario;
        gestor.tomarMotivoTipoFueraServicio(indiceMotivo);
        pedirComentario();
    }

    public void pedirComentario() {
        if (this.comentarioTemporal != null && !this.comentarioTemporal.isEmpty()) {
            tomarComentario();
        }
    }

    public void tomarComentario() {
        if (this.comentarioTemporal != null && !this.comentarioTemporal.isEmpty()) {
            MotivoTipo ultimoMotivo = gestor.getUltimoMotivoSeleccionado();
            if (ultimoMotivo != null) {
                motivosYComentariosLocal.put(ultimoMotivo, this.comentarioTemporal);
            }
            this.comentarioTemporal = null;
            actualizarListaMotivosSelecionados();
        }
    }

    /**
     * PASO 8: Pedir confirmación
     */
    public void pedirConfirmacionCierreOrden() {
        inicializarLayout(8, 9);

        VBox contenido = obtenerVBoxContenido();

        VBox panel = crearPanelContenido();
        panel.setStyle(panel.getStyle() + "; -fx-border-color: " + COLOR_ADVERTENCIA);

        Label icono = new Label("⚠");
        icono.setFont(new Font(30));

        Label titulo = crearLabel(
                "Confirmar cierre de orden",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PRIMARIO));

        Label descripcion = crearLabel(
                "¿Está seguro de que desea cerrar esta orden de inspección? " +
                        "Esta acción pondrá el sismógrafo fuera de servicio.",
                FUENTE_NORMAL,
                Color.web("#666"));

        panel.getChildren().addAll(icono, titulo, descripcion);
        contenido.getChildren().add(panel);

        Button btnConfirmar = crearBotonPrimario("Confirmar cierre");
        btnConfirmar.setStyle(
                btnConfirmar.getStyle() + "; -fx-background-color: " + COLOR_EXITO);
        btnConfirmar.setOnAction(e -> tomarConfirmacionCierreOrden(true));

        Button btnRechazar = crearBotonSecundario("Rechazar");
        btnRechazar.setStyle(
                btnRechazar.getStyle() + "; -fx-background-color: " + COLOR_RECHAZAR);
        btnRechazar.setOnAction(e -> mostrarConfirmacionRechazo());

        contenido.getChildren().add(crearBarraBotones(btnConfirmar, btnRechazar));
    }

    // EMPEZAR A MOSTRAR DESDE ACA PARA NO HACERLO LARGO ❗❗❗❗❗❗❗❗
    /**
     * PASO 9: Tomar confirmación
     */
    public void tomarConfirmacionCierreOrden(boolean confirmacionCierre) {

        if (!confirmacionCierre) {
            cancelarCasoUso();
            return;
        }

        Map<String, Object> datos = gestor.getOrdenSeleccionada();

        // Agregar motivos al gestor
        for (Map.Entry<MotivoTipo, String> entry : motivosYComentariosLocal.entrySet()) {
            gestor.agregarMotivoAlGestor(entry.getKey(), entry.getValue());
        }

        // Confirmar en gestor
        // ENGANCHE CON EL ANÁLISIS ❗❗❗❗❗❗❗❗❗❗
        gestor.tomarConfirmacionCierreOrden(confirmacionCierre);

        // Mostrar pantalla de éxito
        inicializarLayout(9, 9);

        VBox contenido = obtenerVBoxContenido();

        VBox panel = crearPanelContenido();
        panel.setStyle(panel.getStyle() + "; -fx-border-color: " + COLOR_EXITO);

        Label icono = new Label("✓");
        icono.setFont(new Font(50));
        icono.setTextFill(Color.web(COLOR_EXITO));

        Label titulo = crearLabel(
                "¡Cierre completado exitosamente!",
                FUENTE_SUBTITULO,
                Color.web(COLOR_EXITO));

        if (datos == null) {
            Label error = crearLabel(
                    "Error al recuperar los datos de la orden",
                    FUENTE_NORMAL,
                    Color.web(COLOR_PELIGRO));
            panel.getChildren().addAll(icono, titulo, error);
            contenido.getChildren().add(panel);
            return;
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaCierre = gestor.getFechaHoraActual().format(fmt);

        VBox resumen = new VBox(10);
        resumen.setPadding(new Insets(15));
        resumen.setStyle("-fx-border-color: #eee; -fx-border-width: 1; -fx-background-color: #fafafa");

        String idSismografo = "N/A";
        Sismografo sismoEncontrado = gestor.getSismografoEncontrado();
        if (sismoEncontrado != null) {
            idSismografo = sismoEncontrado.getIdentificadorSismografo();
        }

        resumen.getChildren().addAll(
                crearLabel("📋 Orden #" + datos.get("nroDeOrden"), FUENTE_NORMAL, Color.web(COLOR_PRIMARIO)),
                crearLabel("📍 Estación: " + datos.get("nombreEstacionSismologica"), FUENTE_NORMAL, Color.web("#333")),
                crearLabel("📡 Sismógrafo: " + datos.get("idSismografo"), FUENTE_NORMAL, Color.web("#333")),
                crearLabel("🕐 Cierre: " + fechaCierre, FUENTE_NORMAL, Color.web("#333")));

        panel.getChildren().addAll(icono, titulo, resumen);
        contenido.getChildren().add(panel);

        Button btnContinuar = crearBotonPrimario("Finalizar");
        btnContinuar.setOnAction(e -> {
            try {
                if (root != null && root.getScene() != null && root.getScene().getWindow() != null) {
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.close();
                } else {
                    Platform.exit();
                }
            } catch (Exception ex) {
                System.err.println("Error cerrando ventana: " + ex.getMessage());
                Platform.exit();
            }
        });

        contenido.getChildren().add(crearBarraBotones(btnContinuar));
    }

    // ==================== MÉTODOS AUXILIARES ====================

    private void actualizarComboMotivos(ComboBox<String> comboBox, List<String> todasLasDescripciones,
            List<MotivoTipo> motivosSeleccionados) {
        comboBox.getItems().clear();

        Set<Integer> indicesSeleccionados = motivosSeleccionados.stream()
                .map(m -> punteroMotivos.indexOf(m))
                .collect(Collectors.toSet());

        for (int i = 0; i < todasLasDescripciones.size(); i++) {
            if (!indicesSeleccionados.contains(i)) {
                comboBox.getItems().add((i + 1) + ": " + todasLasDescripciones.get(i));
            }
        }

        if (!comboBox.getItems().isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
        }
    }

    private void actualizarListaMotivosSelecionados() {
        if (listaMotivosSeleccionadosVBox == null)
            return;

        listaMotivosSeleccionadosVBox.getChildren().clear();

        if (motivosYComentariosLocal.isEmpty()) {
            listaMotivosSeleccionadosVBox.getChildren().add(lblVacioMotivos);
            return;
        }

        int contador = 1;
        for (Map.Entry<MotivoTipo, String> entry : motivosYComentariosLocal.entrySet()) {
            VBox itemMotivo = crearItemMotivoSeleccionado(
                    contador,
                    entry.getKey().getDescripcion(),
                    entry.getValue(),
                    entry.getKey());
            listaMotivosSeleccionadosVBox.getChildren().add(itemMotivo);
            contador++;
        }
    }

    private VBox crearItemMotivoSeleccionado(int numero, String descripcion, String comentario,
            MotivoTipo motivo) {
        VBox item = new VBox(5);
        item.setPadding(new Insets(10));
        item.setStyle(
                "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 3;" +
                        "-fx-background-color: #f9f9f9");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblNumero = new Label(numero + ".");
        lblNumero.setFont(FUENTE_SUBTITULO);
        lblNumero.setTextFill(Color.web(COLOR_PRIMARIO));

        Label lblDescripcion = crearLabel(
                descripcion,
                FUENTE_NORMAL,
                Color.web(COLOR_PRIMARIO));
        lblDescripcion.setPrefWidth(300);

        Button btnEliminar = new Button("✕");
        btnEliminar.setStyle(
                "-fx-background-color: " + COLOR_PELIGRO + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 2 8;" +
                        "-fx-font-size: 12px;" +
                        "-fx-cursor: hand");

        btnEliminar.setOnAction(e -> {
            motivosYComentariosLocal.remove(motivo);
            actualizarListaMotivosSelecionados();
            actualizarComboBoxMotivosDisponibles();
        });

        header.getChildren().addAll(lblNumero, lblDescripcion, btnEliminar);

        Label lblComentarioTitulo = crearLabel(
                "Comentario:",
                FUENTE_PEQUEÑA,
                Color.web("#666"));

        Label lblComentarioTexto = crearLabel(
                "\"" + comentario + "\"",
                FUENTE_PEQUEÑA,
                Color.web(COLOR_PRIMARIO));
        lblComentarioTexto.setWrapText(true);
        lblComentarioTexto.setStyle("-fx-font-style: italic");

        item.getChildren().addAll(header, lblComentarioTitulo, lblComentarioTexto);

        return item;
    }

    private void actualizarComboBoxMotivosDisponibles() {
        VBox contenido = obtenerVBoxContenido();

        // Buscar el panelSeleccion
        for (javafx.scene.Node node : contenido.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                if ("panelSeleccion".equals(vbox.getId())) {
                    // Buscar el ComboBox dentro
                    for (javafx.scene.Node child : vbox.getChildren()) {
                        if (child instanceof ComboBox) {
                            ComboBox<String> combo = (ComboBox<String>) child;
                            List<MotivoTipo> motivosYaSeleccionados = new ArrayList<>(
                                    motivosYComentariosLocal.keySet());
                            actualizarComboMotivos(combo, descripcionesMotivos, motivosYaSeleccionados);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    public void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void mostrarMensaje(String mensaje) {
        VBox contenido = obtenerVBoxContenido();
        Label lbl = crearLabel(mensaje, FUENTE_PEQUEÑA, Color.web("#666"));
        contenido.getChildren().add(lbl);
    }

    private void mostrarConfirmacionRechazo() {
        inicializarLayout(8, 9);

        VBox contenido = obtenerVBoxContenido();

        VBox panel = crearPanelContenido();
        panel.setStyle(panel.getStyle() + "; -fx-border-color: " + COLOR_PELIGRO);

        Label icono = new Label("❓");
        icono.setFont(new Font(30));

        Label titulo = crearLabel(
                "Confirmar rechazo",
                FUENTE_SUBTITULO,
                Color.web(COLOR_PELIGRO));

        Label descripcion = crearLabel(
                "¿Qué desea hacer?\n\n" +
                        "• Reintentar: Vuelve al inicio del proceso\n" +
                        "• Cancelar: Cierra completamente la operación",
                FUENTE_NORMAL,
                Color.web("#666"));

        panel.getChildren().addAll(icono, titulo, descripcion);
        contenido.getChildren().add(panel);

        Button btnReintentar = crearBotonPrimario("↻ Reintentar");
        btnReintentar.setStyle(
                btnReintentar.getStyle() + "; -fx-background-color: " + COLOR_PRIMARIO);
        btnReintentar.setOnAction(e -> gestor.buscarOrdenesDeInspeccionDeRI());

        Button btnCancelarFinal = new Button("✕ Cancelar operación");
        btnCancelarFinal.setStyle(
                "-fx-background-color: " + COLOR_RECHAZAR + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8 20;" +
                        "-fx-border-radius: 3;" +
                        "-fx-cursor: hand");
        btnCancelarFinal.setOnAction(e -> cancelarCasoUso());

        contenido.getChildren().add(crearBarraBotones(btnReintentar, btnCancelarFinal));
    }

    /**
     * Cancela el caso de uso
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
                        "-fx-padding: 30");

        Label icono = new Label("⚠");
        icono.setFont(new Font(40));
        icono.setTextFill(Color.web(COLOR_ADVERTENCIA));

        Label titulo = crearLabel(
                "Cierre cancelado",
                FUENTE_SUBTITULO,
                Color.web(COLOR_ADVERTENCIA));

        Label descripcion = crearLabel(
                "La operación de cierre ha sido cancelada. " +
                        "No se realizaron cambios en el sistema.",
                FUENTE_NORMAL,
                Color.web("#666"));

        panel.getChildren().addAll(icono, titulo, descripcion);
        root.getChildren().add(panel);
    }

    public void mostrarOrdenSeleccionada(Map<String, Object> orden) {
        if (orden == null) {
            mostrarError("Error", "No se pudo cargar la orden");
            return;
        }

        Integer nroOrden = (Integer) orden.get("nroDeOrden");
        String estacion = (String) orden.get("nombreEstacionSismologica");
        String idSismografo = (String) orden.get("idSismografo");
        LocalDateTime fechaFin = (LocalDateTime) orden.get("fechaFinalizacion");

        inicializarLayout(3, 3);
        VBox contenido = obtenerVBoxContenido();

        VBox panel = crearPanelContenido();
        Label titulo = crearLabel("Orden Seleccionada", FUENTE_TITULO, Color.web(COLOR_PRIMARIO));

        VBox resumen = new VBox(10);
        resumen.setPadding(new Insets(15));
        resumen.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-color: #f5f5f5");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fechaFormato = (fechaFin != null) ? fechaFin.format(fmt) : "N/A";

        resumen.getChildren().addAll(
                crearLabel("📋 Orden #" + nroOrden, FUENTE_SUBTITULO, Color.web(COLOR_PRIMARIO)),
                crearLabel("📍 Estación: " + estacion, FUENTE_NORMAL, Color.web("#333")),
                crearLabel("📡 Sismógrafo: " + (idSismografo != null ? idSismografo : "NO ENCONTRADO"),
                        FUENTE_NORMAL, Color.web(idSismografo != null ? "#333" : COLOR_PELIGRO)),
                crearLabel("🕐 Finalizada: " + fechaFormato, FUENTE_NORMAL, Color.web("#333")));

        panel.getChildren().addAll(titulo, resumen);
        contenido.getChildren().add(panel);
    }
}
