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

// import de utilidades de Java
import java.util.Map;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.IntStream;

public class PantallaInspeccion {
    // Gestor
    private GestorCierreInspeccion gestor;

    // Scanner o "input"
    private Scanner scanner = new Scanner(System.in);
    /*
    // Atributos (componentes de la interfaz) --> faltan agregar más
    private Boton botonSeleccionOrdenInspeccion;
    private CampoTexto campoObservacionCierreOrden;
    private ComboBox comboMotivoFueraServicio;
    private CampoTexto campoComentario;
    private Boton botonConfirmacionCierreOrden; */

    private VBox root;
    private List<Map<String,Object>> ordenesActuales;
    // Para la entrada de texto
    private TextField campoObservacion;
    // Botón para confirmar la observación
    private Button btnEnviarObservacion;
    // lista de descripciones proveniente de mostrarMotivosTipoFueraServicio(...)
    private List<String> descripcionesMotivos;

    // Métodos
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
    public void opcionCerrarOrdenDeInspeccion() {
        // Limpiamos la UI y lanzamos el CU
        root.getChildren().clear();
        habilitarVentana();
    }

    public void habilitarVentana() {
        this.gestor = new GestorCierreInspeccion(this); // Se crea el gestor
        gestor.iniciarCierreOrdenInspeccion();          // comienza el flujo del caso de uso
    }


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

    /*
    public void pedirSelecOrdenInspeccion(List<Map<String,Object>> ordenes) {
        Integer seleccion = null;
        Map<String,Object> ordenSeleccionada = null;

        while (ordenSeleccionada == null) {
            System.out.println("Seleccione una orden de inspección por número:");

            for (Map<String,Object> o : ordenes) {
                System.out.println("  " + o.get("nroDeOrden") + "  (" + o.get("idSismografo") + ")");
            }

            System.out.print("Ingrese el número: ");
            if (scanner.hasNextInt()) {
                seleccion = scanner.nextInt();
                // buscar en la lista
                for (Map<String,Object> o : ordenes) {
                    if (seleccion.equals(o.get("nroDeOrden"))) {
                        ordenSeleccionada = o;
                        break;
                    }
                }
            } else {
                scanner.next(); // descarta texto no numérico
            }
            if (ordenSeleccionada == null) {
                System.out.println("Número inválido, inténtalo de nuevo.\n");
            }
        }

        tomarOrdenInspeccionSelec(ordenSeleccionada);
    }*/

    public void tomarOrdenInspeccionSelec(Map<String,Object> ordenSeleccionada) {
        gestor.tomarOrdenInspeccionSelec(ordenSeleccionada);
    }

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

    public void tomarObservacionCierreOrden(String observacionCierre) {
        // Delego al gestor
        gestor.tomarObservacionCierreOrden(observacionCierre);
    }

    /*
    public void pedirObservacionCierreOrden() {
        System.out.println("Ingrese una observación de cierre a la orden de inspección seleccionada:");

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // limpia el buffer si venís de un nextInt()
        }

        String observacionCierre = scanner.nextLine(); // lee la observación real

        tomarObservacionCierreOrden(observacionCierre);
    }

    public void tomarObservacionCierreOrden(String observacionCierre) {
        gestor.tomarObservacionCierreOrden(observacionCierre);
    }*/

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

        /*public void mostrarMotivosTipoFueraServicio(List<String> descrip) {
        System.out.println("Motivos Fuera de servicio:");

        for (int i = 0; i < descrip.size(); i++) {
            System.out.println((i + 1) + ". " + descrip.get(i));
        }
    }*/

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
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

    /*
    public int pedirSelecMotivoTipo() {
        int numMotivo = tomarMotivoTipoFueraServicio();
        return numMotivo;
    }

    public int tomarMotivoTipoFueraServicio() {
        int motivoNum = -1;

        while (true) {
            System.out.print("Seleccione el número del motivo (0 para terminar): ");
            if (scanner.hasNextInt()) {
                motivoNum = scanner.nextInt();

                if (motivoNum == 0) {
                    return 0;
                }

                if (motivoNum >= 1 && motivoNum <= gestor.getPunteroMotivoSize()) {
                    gestor.tomarMotivoTipoFueraServicio(motivoNum);
                    return motivoNum;
                } else {
                    System.out.println("Número fuera de rango. Intente de nuevo.");
                }
            } else {
                scanner.next(); // descarta texto no numérico
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }
        }
    }
    */

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


    /*
    public void pedirComentario() {
        tomarComentario();
    }

    public void tomarComentario() {
        System.out.println("Ingrese un comentario al tipo de motivo seleccionado:");

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // limpia el buffer si venís de un nextInt()
        }

        String comentario = scanner.nextLine(); // lee la observación real

        gestor.tomarComentario(comentario);
    }*/

    public void pedirConfirmacionCierreOrden() {
        Scanner scanner = new Scanner(System.in);
        String confirmacionCierre;

        while (true) {
            System.out.println("¿Cerrar Orden de Inspección? ('SI' para confirmar):");
            confirmacionCierre = scanner.nextLine();

            if (confirmacionCierre.equalsIgnoreCase("si")) {
                tomarConfirmacionCierreOrden(confirmacionCierre);
                break; // sale del bucle
            } else {
                System.out.println("Entrada inválida. Por favor, escriba 'SI' para confirmar.");
            }
        }
    }

    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        gestor.tomarConfirmacionCierreOrden(confirmacionCierre);
    }

    public void mostrarEstadoCerrado(Estado estado) {
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
    }


    // Clases auxiliares simuladas para que compile (después se reeplazan por las reales)
    private class Boton {}
    private class CampoTexto {}
    private class ComboBox {}

}
