package boundary;

import gestor.GestorCierreInspeccion;
import model.*;
import datos.RepositorioDatos;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
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
    private ProgressBar progressBar;
    private Label labelEstado;
    private ScrollPane scrollActual;
    
    // ========== DATOS ==========
    private List<Map<String, Object>> ordenesActuales;
    private List<String> descripcionesMotivos;
    private List<MotivoTipo> punteroMotivos;
    private Map<MotivoTipo, String> motivosYComentariosLocal = new HashMap<>();
    private VBox listaMotivosSeleccionadosVBox;
    private Label lblVacioMotivos;
    private String comentarioTemporal;
    
    // ========== PALETA DE COLORES ==========
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
        
        // Contenido principal (scrollable)
        scrollActual = new ScrollPane();
        scrollActual.setStyle("-fx-background-color: " + COLOR_FONDO);
        scrollActual.setFitToWidth(true);

        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(30));
        contenido.setStyle("-fx-background-color: " + COLOR_FONDO);
        
        scrollActual.setContent(contenido);
        root.getChildren().add(scrollActual);
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
        panel.setStyle(
            "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;" +
            "-fx-background-color: white;" +
            "-fx-padding: 20"
        );
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

    private VBox obtenerVBoxContenido() {
        ScrollPane scroll = (ScrollPane) root.getChildren().get(1);
        return (VBox) scroll.getContent();
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
    public void mostrarOrdCompRealizadas(List<Map<String, Object>> ordenes) {
        this.ordenesActuales = ordenes;

        inicializarLayout(2, 9);
        
        VBox contenido = obtenerVBoxContenido();
        
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
    
    // ❗❗❗❗❗❗❗❗❗❗❗❗?????????????????
    public void pedirSelecOrdenInspeccion(List<Map<String, Object>> ordenes) {
        // Este método es llamado por el gestor para indicar que
        // ya puede mostrarse la pantalla de selección
        // La pantalla ya está mostrada en mostrarOrdCompRealizadas()
        // Este método es principalmente para mantener el flujo del diagrama
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
        // REVISAR FLUJO ❗❗❗❗❗❗❗❗❗❗❗❗❗❗ (pantalla.pedirSelecc() -> gestor.pedirSelecc())
    }

    /**
     * PASO 3: Tomar orden seleccionada
     */
    public void tomarOrdenInspeccionSelec(Map<String, Object> ordenSeleccionada) {
        gestor.tomarOrdenInspeccionSelec(ordenSeleccionada);
    }

    /**
     * PASO 4: Sistema permite ingresar observación de cierre
     * Nielsen #7: Flexibilidad y eficiencia
     * Nielsen #8: Minimalista
     */
    public void pedirObservacionCierreOrden() {
        inicializarLayout(3, 9);
        
        VBox contenido = obtenerVBoxContenido();
        
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
    public void mostrarMotivosTipoFueraServicio(List<String> descripciones, List<MotivoTipo> punteros) {
        this.descripcionesMotivos = descripciones;
        this.punteroMotivos = punteros;  
        this.motivosYComentariosLocal.clear();

        inicializarLayout(4, 9);
        
        VBox contenido = obtenerVBoxContenido();
        
        // Panel principal
        VBox panelMotivos = crearPanelContenido();
        
        Label titulo = crearLabel(
            "Seleccione Motivos de Fuera de Servicio",
            FUENTE_SUBTITULO,
            Color.web(COLOR_PRIMARIO)
        );
        
        Label descripcion = crearLabel(
            "Seleccione uno o varios motivos y agregue un comentario para cada uno. " +
            "Debe seleccionar al menos un motivo.",
            FUENTE_PEQUEÑA,
            Color.web("#666")
        );
        
        panelMotivos.getChildren().addAll(titulo, descripcion);
        
        Separator sep1 = new Separator();
        panelMotivos.getChildren().add(sep1);
        
        // Motivos disponibles
        Label lblMotivosTitulo = crearLabel(
            "Motivos disponibles:",
            FUENTE_SUBTITULO,
            Color.web(COLOR_PRIMARIO)
        );
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
        
        // Motivos seleccionados
        Label lblSeleccionados = crearLabel(
            "Motivos seleccionados:",
            FUENTE_SUBTITULO,
            Color.web(COLOR_PRIMARIO)
        );
        panelMotivos.getChildren().add(lblSeleccionados);
        
        VBox listaMotivosSeleccionados = new VBox(10);
        listaMotivosSeleccionados.setPadding(new Insets(10));
        listaMotivosSeleccionados.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-color: white");
        
        Label lblVacio = new Label("(No hay motivos seleccionados aún)");
        lblVacio.setFont(FUENTE_PEQUEÑA);
        lblVacio.setTextFill(Color.web("#999"));
        listaMotivosSeleccionados.getChildren().add(lblVacio);
        
        panelMotivos.getChildren().add(listaMotivosSeleccionados);
        
        // Guardar referencias para uso posterior
        this.listaMotivosSeleccionadosVBox = listaMotivosSeleccionados;
        this.lblVacioMotivos = lblVacio;
        this.descripcionesMotivos = descripciones;
        
        contenido.getChildren().add(panelMotivos);
        
        // Botones principales
        Button btnContinuar = crearBotonPrimario("Continuar");
        btnContinuar.setOnAction(e -> {
            if (motivosYComentariosLocal.isEmpty()) {
                mostrarError("Motivos requeridos", "Debe seleccionar al menos un motivo antes de continuar.");
                return;
            }

            for (Map.Entry<MotivoTipo, String> entry : motivosYComentariosLocal.entrySet()) {
                gestor.agregarMotivoAlGestor(entry.getKey(), entry.getValue());
            }

            gestor.pedirConfirmacionCierreOrden();
        });
        
        Button btnCancelar = crearBotonCancelar();
        
        contenido.getChildren().add(crearBarraBotones(btnContinuar, btnCancelar));
        
        pedirSelecMotivoTipo();
    }

    /**
     * PASO 7: Pedir selección de motivos
     * Nielsen #5: Prevención de errores
     */
    public void pedirSelecMotivoTipo() {
        VBox contenido = obtenerVBoxContenido();
        
        // Buscar el panel principal (el primero después del header)
        VBox panelPrincipal = null;
        for (javafx.scene.Node node : contenido.getChildren()) {
            if (node instanceof VBox) {
                panelPrincipal = (VBox) node;
                break;
            }
        }
        
        if (panelPrincipal == null) return;
        
        // Insertar el panel de selección
        VBox panelSeleccion = new VBox(15);
        panelSeleccion.setPadding(new Insets(15));
        panelSeleccion.setStyle(
            "-fx-border-color: " + COLOR_EXITO + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-background-color: #f0f8f0"
        );
        
        Label lblSeleccionar = crearLabel(
            "Seleccionar nuevo motivo:",
            FUENTE_SUBTITULO,
            Color.web(COLOR_PRIMARIO)
        );
        panelSeleccion.getChildren().add(lblSeleccionar);
        
        // ComboBox con motivos NO seleccionados
        ComboBox<String> comboMotivos = new ComboBox<>();
        actualizarComboMotivos(comboMotivos, descripcionesMotivos, new ArrayList<>(motivosYComentariosLocal.keySet()));
        
        comboMotivos.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
            "-fx-padding: 8;" +
            "-fx-font-size: 12px"
        );
        comboMotivos.setPrefWidth(Double.MAX_VALUE);
        
        panelSeleccion.getChildren().add(comboMotivos);
        
        // TextArea para comentario
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
            "-fx-border-color: " + COLOR_SECUNDARIO
        );
        panelSeleccion.getChildren().add(areaComentario);
        
        // Botón agregar
        Button btnAgregar = new Button("+ Agregar motivo");
        btnAgregar.setStyle(
            "-fx-background-color: " + COLOR_EXITO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 15;" +
            "-fx-border-radius: 3;" +
            "-fx-cursor: hand"
        );
        btnAgregar.setPrefWidth(200);
        
        HBox hboxAgregar = new HBox(10);
        hboxAgregar.setAlignment(Pos.CENTER_LEFT);
        hboxAgregar.getChildren().add(btnAgregar);
        panelSeleccion.getChildren().add(hboxAgregar);
        
        // Manejador del boton agregar
        btnAgregar.setOnAction(e -> {
            String motivoSeleccionado = comboMotivos.getValue();
            String comentario = areaComentario.getText().trim();
            
            if (motivoSeleccionado == null || motivoSeleccionado.isEmpty()) {
                mostrarError("Seleccione un motivo", "Por favor, seleccione un motivo de la lista.");
                return;
            }
            
            if (comentario.isEmpty()) {
                mostrarError("Comentario requerido", "Por favor, ingrese un comentario para este motivo.");
                return;
            }
            
            // Extraer índice del motivo (ej: "1: Mantenimiento" -> 1)
            int indiceMotivo = Integer.parseInt(motivoSeleccionado.split(":")[0]) - 1;
            
            tomarMotivoTipoFueraServicio(indiceMotivo + 1, comentario);
            
            // Limpiar formulario
            areaComentario.clear();
            
            List<MotivoTipo> nuevosSeleccionados = new ArrayList<>(motivosYComentariosLocal.keySet());
            actualizarComboMotivos(comboMotivos, descripcionesMotivos, nuevosSeleccionados);
            
            comboMotivos.getSelectionModel().selectFirst();
        });
        
        // Insertar el panel de selección ANTES de la lista de seleccionados
        int indiceInsercion = panelPrincipal.getChildren().indexOf(listaMotivosSeleccionadosVBox);
        if (indiceInsercion > 0) {
            panelPrincipal.getChildren().add(indiceInsercion, panelSeleccion);
        }
    }

    public void tomarMotivoTipoFueraServicio(int indiceMotivo, String comentario) {
        // Obtener el motivo del listado
        MotivoTipo motivo = this.punteroMotivos.get(indiceMotivo - 1);
        
        // Almacenar comentario temporalmente
        this.comentarioTemporal = comentario;
        
        // Guardar en gestor temporalmente
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
            // Obtener el motivo que guardó el gestor y agregarlo localmente
            MotivoTipo ultimoMotivo = gestor.getUltimoMotivoSeleccionado();
            if (ultimoMotivo != null) {
                motivosYComentariosLocal.put(ultimoMotivo, this.comentarioTemporal);
            }
            
            this.comentarioTemporal = null;
            
            // Actualizar lista visual de motivos seleccionados
            actualizarListaMotivosSelecionados();
        }
    }

    /**
     * PASO 8: Pedir confirmación
     * Nielsen #1: Visibilidad
     * Nielsen #3: Libertad y control
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
        Button btnConfirmar = crearBotonPrimario("Confirmar cierre");
        btnConfirmar.setStyle(
            btnConfirmar.getStyle() +
            "; -fx-background-color: " + COLOR_EXITO
        );
        // ❗❗❗❗❗❗❗❗ Ver de cambiar confirmacionCierreOrden por un int
        btnConfirmar.setOnAction(e -> tomarConfirmacionCierreOrden("SI"));
        
        Button btnCancelar = crearBotonCancelar();
        
        contenido.getChildren().add(crearBarraBotones(btnConfirmar, btnCancelar));
    }

    /**
     * PASO 9: Tomar confirmación
     */
    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        Map<String, Object> datos = gestor.getOrdenSeleccionada();
        
        for (Map.Entry<MotivoTipo, String> entry : motivosYComentariosLocal.entrySet()) {
            gestor.agregarMotivoAlGestor(entry.getKey(), entry.getValue());
        }

        gestor.tomarConfirmacionCierreOrden(confirmacionCierre);

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
            Color.web(COLOR_EXITO)
        );

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
            return;
        }
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fechaCierre = gestor.getFechaHoraActual().format(fmt);
        
        VBox resumen = new VBox(10);
        resumen.setPadding(new Insets(15));
        resumen.setStyle("-fx-border-color: #eee; -fx-border-width: 1; -fx-background-color: #fafafa");
        
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
    
    // Actualiza el ComboBox eliminando motivos ya seleccionados
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

    // Actualiza la lista visual de motivos seleccionados
    private void actualizarListaMotivosSelecionados() {
        if (listaMotivosSeleccionadosVBox == null) return;
        
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
                entry.getKey()
            );
            listaMotivosSeleccionadosVBox.getChildren().add(itemMotivo);
            contador++;
        }
    }

    // Crea un item visual para un motivo seleccionado con botón eliminar
    private VBox crearItemMotivoSeleccionado(int numero, String descripcion, String comentario, 
                                            MotivoTipo motivo) {
        VBox item = new VBox(5);
        item.setPadding(new Insets(10));
        item.setStyle(
            "-fx-border-color: " + COLOR_SECUNDARIO + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 3;" +
            "-fx-background-color: #f9f9f9"
        );
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label lblNumero = new Label(numero + ".");
        lblNumero.setFont(FUENTE_SUBTITULO);
        lblNumero.setTextFill(Color.web(COLOR_PRIMARIO));
        
        Label lblDescripcion = crearLabel(
            descripcion,
            FUENTE_NORMAL,
            Color.web(COLOR_PRIMARIO)
        );
        lblDescripcion.setPrefWidth(300);
        
        Button btnEliminar = new Button("✕");
        btnEliminar.setStyle(
            "-fx-background-color: " + COLOR_PELIGRO + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 2 8;" +
            "-fx-font-size: 12px;" +
            "-fx-cursor: hand"
        );
        
        btnEliminar.setOnAction(e -> {
            motivosYComentariosLocal.remove(motivo);
            actualizarListaMotivosSelecionados();
        });
        
        header.getChildren().addAll(lblNumero, lblDescripcion, btnEliminar);
        
        Label lblComentarioTitulo = crearLabel(
            "Comentario:",
            FUENTE_PEQUEÑA,
            Color.web("#666")
        );
        
        Label lblComentarioTexto = crearLabel(
            "\"" + comentario + "\"",
            FUENTE_PEQUEÑA,
            Color.web(COLOR_PRIMARIO)
        );
        lblComentarioTexto.setWrapText(true);
        lblComentarioTexto.setStyle("-fx-font-style: italic");
        
        item.getChildren().addAll(header, lblComentarioTitulo, lblComentarioTexto);
        
        return item;
    }

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
        VBox contenido = obtenerVBoxContenido();
        Label lbl = crearLabel(mensaje, FUENTE_PEQUEÑA, Color.web("#666"));
        contenido.getChildren().add(lbl);
    }

    /**
     * Cancela el caso de uso completamente
     * Nielsen #3: Libertad y control
     * Flujo alternativo A7
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
