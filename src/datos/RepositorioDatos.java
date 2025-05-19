package datos;

// Import del gestor, el boundary y modelos
import model.*;
import gestor.GestorCierreInspeccion;
import boundary.PantallaInspeccion;

// Import de utilidades de java
import java.time.LocalDateTime;
import java.util.List;
// import java.util.Map;
import java.util.ArrayList;

public class RepositorioDatos {
    private static List<OrdenDeInspeccion> ordenesDeInspeccion = new ArrayList<>();
    private static Usuario usuario;  // declarada aquí a nivel de clase
    private static List<MotivoTipo> motivos = new ArrayList<>();
    private static List<Estado> estados = new ArrayList<>();
    private static List<CambioDeEstado> cambiosEstado = new ArrayList<>();
    private static List<Empleado> empleados = new ArrayList<>();

    static {
        // Empleados y usuario
        Empleado emp1 = new Empleado("Pepe", "López", "0001", "pepe@mail.com", new Rol("RI", "Responsable"));
        usuario = new Usuario("pepe123", "pass", emp1);  // asignación

        Empleado emp2 = new Empleado("Otro", "Empleado", "0002", "otro@mail.com", new Rol("RI", "Responsable"));

        Empleado emp3 = new Empleado("Claudia", "Reparadora", "0003", "claudia@mail.com", new Rol("RR", "Responsable Reparación"));
        // Estados
        Estado completo = new Estado("Completamente Realizada", "OrdenDeInspeccion");
        Estado enProceso = new Estado("En Proceso", "OrdenDeInspeccion");
        Estado cerrado = new Estado("CERRADO", "OrdenDeInspeccion");
        Estado fueraDeServicio = new Estado("FUERA DE SERVICIO", "EstacionSismologica");

        estados.add(cerrado);
        estados.add(enProceso);
        estados.add(completo);
        estados.add(fueraDeServicio);

        // Motivos fuera de servicio
        motivos.add(new MotivoTipo("Sensor dañado"));
        motivos.add(new MotivoTipo("Interferencia eléctrica"));
        motivos.add(new MotivoTipo("Condiciones climáticas adversas"));

        // Cambios de Estado
        CambioDeEstado cambioEstado1 = new CambioDeEstado(completo, LocalDateTime.of(2024, 3, 4, 6, 10));
        CambioDeEstado cambioEstado2 = new CambioDeEstado(enProceso, LocalDateTime.of(2023, 6, 7, 16, 45));
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
        Sismografo sism2 = new Sismografo("SISM-004", "444332", LocalDateTime.of(2024,03,10, 10, 30), est2);

        est1.setSismografo(sism2);
        est2.setSismografo(sism1);

        sism1.setCambiosDeEstado(cambiosEstado);
        sism2.setCambiosDeEstado(cambiosEstado);

        // Órdenes
        OrdenDeInspeccion o1 = new OrdenDeInspeccion(101, LocalDateTime.of(2025,5,1,9,0), LocalDateTime.of(2025,5,3,17,0), emp1, est1);
        o1.setEstado(completo);

        OrdenDeInspeccion o2 = new OrdenDeInspeccion(102, LocalDateTime.of(2025,5,2,9,0), LocalDateTime.of(2025,5,4,17,0), emp1, est2);
        o2.setEstado(enProceso);

        OrdenDeInspeccion o3 = new OrdenDeInspeccion(103, LocalDateTime.of(2025,4,28,9,0), LocalDateTime.of(2025,5,1,12,0), emp2, est1);
        o3.setEstado(completo);

        ordenesDeInspeccion.addAll(List.of(o1, o2, o3));
    }

    public static List<OrdenDeInspeccion> obtenerOrdenes() {
        return ordenesDeInspeccion;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static List<MotivoTipo> getMotivos(){
        return motivos;
    }

    public static List<Estado> getEstados() {
        return estados;
    }

    public static List<Empleado> getEmpleados() {
        return empleados;
    }
}