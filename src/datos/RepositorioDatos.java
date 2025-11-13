package datos;

// Import del gestor, el boundary y modelos
import model.*;
import gestor.GestorCierreInspeccion;
import boundary.*;

// Import de utilidades de java
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class RepositorioDatos {
    private static List<OrdenDeInspeccion> ordenesDeInspeccion = new ArrayList<>();
    private static Usuario usuario1;
    private static List<MotivoTipo> motivos = new ArrayList<>();
    private static List<CambioDeEstado> cambiosEstado = new ArrayList<>();
    private static List<Empleado> empleados = new ArrayList<>();
    private static List<MonitorCCRS> monitores = new ArrayList<>();
    private static List<String> correosResponsables = new ArrayList<>();
    private static InterfazNotificacionMail interfazMail;

    static {
        // Empleados y usuario
        Empleado emp1 = new Empleado("Pepe", "Gonzales", "0001", "pepe@mail.com", new Rol("RI", "Responsable Inspección"));
        usuario1 = new Usuario("pepe123", "pass", emp1);  // asignación

        Empleado emp2 = new Empleado("Otro", "Empleado", "0002", "otro@mail.com", new Rol("RI", "Responsable Inspección"));
        Usuario usuario2 = new Usuario("potro123", "pepass", emp2);  // asignación

        Empleado emp3 = new Empleado("Claudia", "Reparadora", "0003", "claudia@mail.com", new Rol("RR", "Responsable Reparación"));
        Usuario usuario3 = new Usuario("clau123", "pass123", emp3);  // asignación

        Empleado emp4 = new Empleado("Mateo", "Reparador", "0004", "matute@mail.com", new Rol("RR", "Responsable Reparación"));
        Usuario usuario4 = new Usuario("mateo123", "pass456", emp4);  // asignación

        //empleados
        empleados.add(emp1);
        empleados.add(emp2);
        empleados.add(emp3);
        empleados.add(emp4);

        /* 
        // Las órdenes ahora usan el patrón State con model.estados.Estado
        // Los estados se asignan automáticamente en el constructor de OrdenDeInspeccion
        Estado completo = new Estado("Completamente Realizada", "OrdenDeInspeccion");
        Estado enProceso = new Estado("En Proceso", "OrdenDeInspeccion");
        Estado cerrado = new Estado("CERRADO", "OrdenDeInspeccion");
        Estado fueraDeServicio = new Estado("FUERA DE SERVICIO", "EstacionSismologica");

        estados.add(cerrado);
        estados.add(enProceso);
        estados.add(completo);
        estados.add(fueraDeServicio);
        */

        // Motivos fuera de servicio
        motivos.add(new MotivoTipo("Sensor dañado"));
        motivos.add(new MotivoTipo("Interferencia eléctrica"));
        motivos.add(new MotivoTipo("Condiciones climáticas adversas"));

        // Cambios de Estado para Sismografo
        // Se usa model.estados.Estado (la clase abstracta del patrón State)
        model.estados.Estado enLinea = new model.estados.EnLinea();
        model.estados.Estado inhabilitado = new model.estados.InhabilitadoPorInspeccion();
        model.estados.Estado fueraDeServicio = new model.estados.FueraServicio();

        // Cambios de Estado
        CambioDeEstado cambioEstado1 = new CambioDeEstado(enLinea, LocalDateTime.of(2024, 3, 4, 6, 10));
        CambioDeEstado cambioEstado2 = new CambioDeEstado(inhabilitado, LocalDateTime.of(2023, 6, 7, 16, 45));
        CambioDeEstado cambioEstado3 = new CambioDeEstado(fueraDeServicio, LocalDateTime.of(2024, 5, 7, 19, 40));
        CambioDeEstado cambioEstado4 = new CambioDeEstado(fueraDeServicio, LocalDateTime.of(2022, 10, 10, 10, 10));

        cambiosEstado.add(cambioEstado1);
        cambiosEstado.add(cambioEstado2);
        cambiosEstado.add(cambioEstado3);
        cambiosEstado.add(cambioEstado4);

        // Estaciones
        EstacionSismologica est1 = new EstacionSismologica("EST-001", "DOC-001", LocalDateTime.of(2024,10,1, 9, 0), -34.5, -58.4, "La Plata", "CERT-001", null);
        EstacionSismologica est2 = new EstacionSismologica("EST-002", "DOC-002", LocalDateTime.of(2024,11,1, 9, 0), -31.4, -64.2, "Córdoba", "CERT-002", null);

        // Sismografo
        Sismografo sism1 = new Sismografo("SISM-001", "334253", LocalDateTime.of(2023,10,1, 15, 34), est1);
        sism1.setEstadoActual(inhabilitado);
        Sismografo sism2 = new Sismografo("SISM-004", "444332", LocalDateTime.of(2024,03,10, 10, 30), est2);
        sism2.setEstadoActual(inhabilitado);

        est1.setSismografo(sism2);
        est2.setSismografo(sism1);

        sism1.setCambiosDeEstado(cambiosEstado);
        sism2.setCambiosDeEstado(cambiosEstado);

        // Usa el constructor que ya setea PendienteDeRealizacion automáticamente
        // Luego cambiar a CompletamenteRealizada si es necesario
        OrdenDeInspeccion o1 = new OrdenDeInspeccion(101, LocalDateTime.of(2025,5,1,9,0), LocalDateTime.of(2025,5,3,17,0), emp1, est1);
        // Cambia a CompletamenteRealizada usando el patrón State
        o1.completarTodas();

        OrdenDeInspeccion o2 = new OrdenDeInspeccion(102, LocalDateTime.of(2025,5,2,9,0), LocalDateTime.of(2025,5,4,17,0), emp2, est2);
        // o2 queda en PendienteDeRealizacion (no se completa)

        OrdenDeInspeccion o3 = new OrdenDeInspeccion(103, LocalDateTime.of(2025,4,28,9,0), LocalDateTime.of(2025,5,1,12,0), emp1, est1);
        // Cambia a CompletamenteRealizada
        o3.completarTodas();

        ordenesDeInspeccion.addAll(List.of(o1, o2, o3));

        correosResponsables.add(emp3.obtenerMail()); // "claudia@mail.com"
        correosResponsables.add(emp4.obtenerMail()); // "matute@mail.com"

        interfazMail = new InterfazNotificacionMail();

        MonitorCCRS monitor1 = new MonitorCCRS();
        MonitorCCRS monitor2 = new MonitorCCRS();
        MonitorCCRS monitor3 = new MonitorCCRS();

        monitores.add(monitor1);
        monitores.add(monitor2);
        monitores.add(monitor3);
    }

    public static List<OrdenDeInspeccion> obtenerOrdenes() {
        return ordenesDeInspeccion;
    }

    public static Usuario getUsuario() {
        return usuario1;
    }

    public static List<MotivoTipo> getMotivos(){
        return motivos;
    }

    public static List<Empleado> getEmpleados() {
        return empleados;
    }

    public static List<MonitorCCRS> getMonitores(){ return monitores; }

    public static InterfazNotificacionMail getInterfazMail() { return interfazMail; }
}