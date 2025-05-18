package datos;

import model.*;
import gestor.GestorCierreInspeccion;
import boundary.PantallaInspeccion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class RepositorioDatos {
    private static List<OrdenDeInspeccion> ordenesDeInspeccion = new ArrayList<>();
    private static Usuario usuario;  // declarada aquí a nivel de clase

    static {
        // Empleados y usuario
        Empleado emp1 = new Empleado("Pepe", "López", "0001", "pepe@mail.com", new Rol("RI", "Responsable"));
        usuario = new Usuario("pepe123", "pass", emp1);  // asignación

        Empleado emp2 = new Empleado("Otro", "Empleado", "0002", "otro@mail.com", new Rol("RI", "Responsable"));

        // Estados
        Estado completo = new Estado("Completamente Realizada", "OrdenDeInspeccion");
        Estado enProceso = new Estado("En Proceso", "OrdenDeInspeccion");

        // Estaciones
        EstacionSismologica est1 = new EstacionSismologica("EST-001", "DOC-001", LocalDateTime.of(2024,10,1, 9, 0), -34.5, -58.4, "La Plata", "CERT-001", null);
        EstacionSismologica est2 = new EstacionSismologica("EST-002", "DOC-002", LocalDateTime.of(2024,11,1, 9, 0), -31.4, -64.2, "Córdoba", "CERT-002", null);

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
}