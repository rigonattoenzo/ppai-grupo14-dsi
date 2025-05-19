package boundary;

// Import del gestor y los modelos
import gestor.GestorCierreInspeccion;
import model.*;

// import de utilidades de Java
import java.util.Map;
import java.util.List;
import java.util.Scanner;

public class PantallaInspeccion {
    // Gestor
    private GestorCierreInspeccion gestor;

    // Scanner o "input"
    private Scanner scanner = new Scanner(System.in);

    // Atributos (componentes de la interfaz) --> faltan agregar más
    private Boton botonSeleccionOrdenInspeccion;
    private CampoTexto campoObservacionCierreOrden;
    private ComboBox comboMotivoFueraServicio;
    private CampoTexto campoComentario;
    private Boton botonConfirmacionCierreOrden;

    // Métodos
    // Constructor
    public PantallaInspeccion() {
        // Inicializar todos los atributos, para la interfaz gráfica
    }

    public GestorCierreInspeccion getGestor() {
        return gestor;
    }

    // Métodos del caso de uso 37
    //PASO 1
    public void opcionCerrarOrdenDeInspeccion() {
        habilitarVentana();
    }

    public void habilitarVentana() {
        this.gestor = new GestorCierreInspeccion(this); // Se crea el gestor
        gestor.iniciarCierreOrdenInspeccion();          // comienza el flujo del caso de uso
    }

    //PASO 2
    public void mostrarOrdCompRealizadas() {
        System.out.println("Órdenes Completamente Realizadas del Empleado:");

        for (Map<String, Object> datosOrden : gestor.getOrdenesFiltradasConDatos()) {
            System.out.println(datosOrden);
        }
    }

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
    }

    //PASO 3
    public void tomarOrdenInspeccionSelec(Map<String,Object> ordenSeleccionada) {
        gestor.tomarOrdenInspeccionSelec(ordenSeleccionada);
    }

    //PASO 4
    public void pedirObservacionCierreOrden() {
        System.out.println("Ingrese una observación de cierre a la orden de inspección seleccionada:");

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // limpia el buffer si venís de un nextInt()
        }

        String observacionCierre = scanner.nextLine(); // lee la observación real

        tomarObservacionCierreOrden(observacionCierre);
    }

    //PASO 5
    public void tomarObservacionCierreOrden(String observacionCierre) {
        gestor.tomarObservacionCierreOrden(observacionCierre);
    }

    //PASO 6
    public void mostrarMotivosTipoFueraServicio(List<String> descrip) {
        System.out.println("Motivos Fuera de servicio:");

        for (int i = 0; i < descrip.size(); i++) {
            System.out.println((i + 1) + ". " + descrip.get(i));
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public int pedirSelecMotivoTipo() {
        int numMotivo = tomarMotivoTipoFueraServicio();
        return numMotivo;
    }

    //PASO 7
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

        /*int motivoNum = 0;

        System.out.print("Seleccione el número del motivo: ");
        if (scanner.hasNextInt()) {
            motivoNum = scanner.nextInt();
        } else {
            scanner.next(); // descarta texto no numérico
        }

        gestor.tomarMotivoTipoFueraServicio(motivoNum);
        return motivoNum;*/
    }

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
    }

    //PASO 8
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

    //PASO 9 (último de PantallaInspección)
    public void tomarConfirmacionCierreOrden(String confirmacionCierre) {
        gestor.tomarConfirmacionCierreOrden(confirmacionCierre);
    }
     /* //REVISAR!
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
    */

    // Clases auxiliares simuladas para que compile (después se reeplazan por las reales)
    private class Boton {}
    private class CampoTexto {}
    private class ComboBox {}

}
