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
    public PantallaInspeccion() {
        // Inicializar todos los atributos, para la interfaz gráfica
    }

    /** setter para inyectar el VBox desde MainFX */
    public void setRoot(VBox root) {
        this.root = root;
    }

    public GestorCierreInspeccion getGestor() {
        return gestor;
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
        root.getChildren().add(new Label("Órdenes Completamente Realizadas del Empleado:"));
        for (Map<String, Object> datosOrden : gestor.getOrdenesFiltradasConDatos()) {
            root.getChildren().add(new Label(gestor.asString(datosOrden))); // USO DE LA FUNCIÓN NUEVA
        }
    }

    /**
     * Muestra un botón por cada orden y espera al click
     */
    public void pedirSelecOrdenInspeccion(List<Map<String,Object>> ordenes) {
        this.ordenesActuales = ordenes;

        // Limpio pantalla y muestro un título
        root.getChildren().clear();
        root.getChildren().add(new Label("Seleccione una orden de inspección:"));

        // Por cada orden, creo un botón con su número
        for (Map<String,Object> o : ordenes) {
            String nro = String.valueOf(o.get("nroDeOrden"));
            String sismo = String.valueOf(o.get("idSismografo"));
            Button btn = new Button("Orden #" + nro + " (" + sismo + ")");
            btn.setOnAction(evt -> {
                // Cuando clickeen, delego al gestor y continuo el flujo
                gestor.tomarOrdenInspeccionSelec(o);
            });
            root.getChildren().add(btn);
        }
    }

    //PASO 3
    public void tomarOrdenInspeccionSelec(Map<String,Object> ordenSeleccionada) {
        gestor.tomarOrdenInspeccionSelec(ordenSeleccionada);
    }

    //PASO 4
    public void pedirObservacionCierreOrden() {
        // Limpio la UI
        root.getChildren().clear();

        // Mensaje de instrucción
        root.getChildren().add(new Label(
                "Ingrese una observación de cierre a la orden de inspección seleccionada:"
        ));

        // Campo de texto
        campoObservacion = new TextField();
        campoObservacion.setPromptText("Escriba su observación aquí...");

        // Botón de enviar
        btnEnviarObservacion = new Button("Enviar Observación");
        btnEnviarObservacion.setOnAction(evt -> {
            tomarObservacionCierreOrden(campoObservacion.getText());
        });

        // Agrego todo al layout
        root.getChildren().addAll(campoObservacion, btnEnviarObservacion);
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

        // 2) Mensaje de confirmación
        root.getChildren().add(new Label("¿Cerrar Orden de Inspección?"));

        // 3) Botón 'SI'
        Button btnSi = new Button("SI");
        btnSi.setOnAction(e -> tomarConfirmacionCierreOrden("SI"));

        // 4) Botón 'CANCELAR' (opcional: vuelve al menú o fin)
        Button btnCancelar = new Button("CANCELAR");
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
     */
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
        btnConfirmar.setOnAction(e -> {
            // Llamar al método de gestor que cierra la orden
            gestor.tomarConfirmacionCierreOrden("SI");
        });
        root.getChildren().add(btnConfirmar);
    }


    //PASO 9 (último de PantallaInspección)
    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        // 1) Llamada original al gestor para que haga el cierre
        gestor.tomarConfirmacionCierreOrden(confirmacionCierre);

        // 2) Limpiar la pantalla y mostrar mensaje de éxito
        root.getChildren().clear();
        root.getChildren().add(new Label("✅ La orden de inspección ha sido cerrada exitosamente."));

        // 3) Mostrar detalles de la orden
        Map<String,Object> datos = gestor.getOrdenSeleccionada();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        root.getChildren().add(new Label("Número de orden: "  + datos.get("nroDeOrden")));
        root.getChildren().add(new Label("Estación: "        + datos.get("nombreEstacionSismologica")));
        root.getChildren().add(new Label("Sismógrafo: "      + datos.get("idSismografo")));
        // Fecha de cierre fue seteada en el gestor, la obtenemos:
        String fechaCierre = gestor.getFechaHoraActual().format(fmt);
        root.getChildren().add(new Label("Fecha de cierre: " + fechaCierre));
    }

    /*public void mostrarEstadoCerrado(Estado estado) {
        System.out.println("¡Estado 'CERRADO' encontrado!");
        System.out.println("Nombre: " + estado.getNombreEstado());
        System.out.println("Entidad: " + "OrdenDeInspeccion");
    }

    public void mostrarEstadoFueraDeServicio(Estado estado) {
        System.out.println("¡Estado 'FUERA DE SERVICIO' encontrado!");
        System.out.println("Nombre: " + estado.getNombreEstado());
        System.out.println("Entidad: " + "EstacionSismologica");
    }

    public void mostrarErrorEstadoNoEncontrado(String nombreEstado) {
        System.out.println("Error: No se encontró el estado '" + nombreEstado + "'.");
    }*/
}
