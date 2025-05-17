// src/Main.java
import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Crear Rol y Empleado
        Rol rolTecnico = new Rol("Técnico", "Técnico que realiza inspecciones");
        Empleado empleado = new Empleado(
                "Juan", "Pérez", "123456789", "juanperez@gmail.com", rolTecnico
        );

        // 2. Crear Usuario y Sesión
        Usuario usuario = new Usuario("juanp", "clave123", empleado);
        Sesion sesion = new Sesion(LocalDateTime.now());
        System.out.println("Usuario " + usuario.getEmpleado().getNombreCompleto() + " inició sesión.");

        // 3. Crear Estación Sismológica (usa LocalDate para fechaSolicitudCertificacion)
        EstacionSismologica estacion = new EstacionSismologica(
                "EST001",
                "DOC-1234",
                LocalDate.of(2024, 10, 1),
                -34.5, -58.4,
                "Estación La Plata",
                "CERT-9876",
                null  // todavía no asignamos el sismógrafo
        );

        // 4. Crear Sismógrafo y asociarlo a la Estación
        Sismografo sismografo = new Sismografo(
                "SISMO123",
                "SN-456",
                LocalDateTime.of(2023, 1, 1, 0, 0),
                estacion
        );
        // Ahora sí lo referenciamos en la estación
        estacion = new EstacionSismologica(
                estacion.getCodigoEstacion(),
                estacion.getDocumentoCertificacionAdq(),
                estacion.getFechaSolicitudCertificacion(),
                estacion.getLatitud(),
                estacion.getLongitud(),
                estacion.getNombre(),
                estacion.getNroCertificacionAdquisicion(),
                sismografo
        );

        // 5. Crear Orden de Inspección
        OrdenDeInspeccion orden = new OrdenDeInspeccion(
                101,                                               // numeroOrden
                LocalDateTime.of(2024, 10, 5, 9, 0),               // fechaHoraInicio
                LocalDateTime.of(2024, 10, 5, 12, 0),              // fechaHoraFinalizacion
                empleado,
                estacion
        );
        System.out.println("Orden #" + orden.getNumeroOrden()
                + " creada para la estación: " + orden.getEstacion().getNombre());

        // 6. Crear Estado y MotivoTipo + MotivoFueraDeServicio
        Estado estadoFuera = new Estado("Fuera de Servicio", "Sismógrafo");
        MotivoTipo tipoMantenimiento = new MotivoTipo("Mantenimiento Programado");
        MotivoFueraDeServicio motivo = new MotivoFueraDeServicio(
                "Cambio de piezas",
                tipoMantenimiento
        );

        // 7. Registrar CambioDeEstado en el Sismógrafo
        CambioDeEstado cambio = new CambioDeEstado(
                estadoFuera,
                empleado,
                LocalDateTime.of(2024, 10, 5, 9, 30),
                motivo
        );
        sismografo.agregarCambioDeEstado(cambio);

        // 8. Cerrar el Cambio de Estado más tarde
        cambio.setFechaHoraFin(LocalDateTime.of(2024, 10, 5, 11, 0));

        // 9. Cerrar la Orden de Inspección
        orden.setObservacionCierre("Inspección completada OK");
        orden.setFechaHoraCierre(LocalDateTime.of(2024, 10, 5, 12, 30));
        orden.setEstado(estadoFuera);  // se asume que el estado de la orden coincide con el del sismógrafo

        // 10. Mostrar resultados
        System.out.println("Estado del sismógrafo "
                + sismografo.getIdentificadorSismografo()
                + " -> " + cambio.getEstado().getNombreEstado()
                + " (finalizado: " + cambio.esEstadoActual() + ")"
        );

        /*System.out.println("Orden #" + orden.getNumeroOrden()
                + " finalizada con observación: " + orden.getObservacionCierre()
        );*/

        System.out.println("Mail del responsable: "
                + empleado.obtenerMail()
        );

        // 11. Cerrar Sesión
        sesion.cerrarSesion(LocalDateTime.now());
        System.out.println("Sesión cerrada a las "
                + sesion.getFechaHoraFin()
        );
    }
}
