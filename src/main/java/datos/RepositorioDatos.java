package datos;

import model.*;
import datos.repositories.*;
import datos.persistence.LocalEntityManagerProvider;
import boundary.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RepositorioDatos {

    private static InterfazNotificacionMail interfazMail = InterfazNotificacionMail.getInstancia();
    private static MonitorManager monitorManager = MonitorManager.getInstancia();

    // ==================== MÉTODOS DE ACCESO ====================

    public static List<OrdenDeInspeccion> obtenerOrdenes() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaOrdenInspeccionRepository repo = new JpaOrdenInspeccionRepository(em);
            List<OrdenDeInspeccion> ordenes = repo.findAll();
            return ordenes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public static List<Sismografo> obtenerSismografos() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaSismografoRepository repo = new JpaSismografoRepository(em);
            List<Sismografo> sismografos = repo.findAll();
            return sismografos;
        } catch (Exception e) {
            System.err.println("Error al obtener sismógrafos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public static Usuario getUsuario() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaUsuarioRepository repo = new JpaUsuarioRepository(em);
            return repo.findByNombreUsuario("pepe123")
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        } finally {
            em.close();
        }
    }

    // ==================== MÉTODOS PARA MOTIVOS ====================

    // Obtiene todos los motivos disponibles
    public static List<MotivoTipo> obtenerMotivos() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaMotivoTipoRepository repo = new JpaMotivoTipoRepository(em);
            return repo.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    // Obtiene motivos como descripciones para UI
    public static List<String> obtenerDescripcionesMotivos() {
        List<MotivoTipo> motivos = obtenerMotivos();
        return motivos.stream()
                .map(MotivoTipo::getDescripcion)
                .collect(Collectors.toList());
    }

    // Busca un motivo por descripción
    public static MotivoTipo buscarMotivoPorDescripcion(String descripcion) {
        return obtenerMotivos().stream()
                .filter(m -> m.getDescripcion().equalsIgnoreCase(descripcion))
                .findFirst()
                .orElse(null);
    }

    // ==================== MÉTODOS PARA EMPLEADOS ====================

    public static List<Empleado> getEmpleados() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaEmpleadoRepository repo = new JpaEmpleadoRepository(em);
            return repo.findAll();
        } finally {
            em.close();
        }
    }

    public static List<Empleado> getResponsablesReparacion() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaEmpleadoRepository repo = new JpaEmpleadoRepository(em);
            return repo.findResponsablesReparacion();
        } finally {
            em.close();
        }
    }

    // ==================== MÉTODOS PARA ÓRDENES ====================

    public static OrdenDeInspeccion getOrdenPorNumero(Integer numeroOrden) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaOrdenInspeccionRepository repo = new JpaOrdenInspeccionRepository(em);
            return repo.findByNumeroOrden(numeroOrden)
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        } finally {
            em.close();
        }
    }

    public static void guardarOrden(OrdenDeInspeccion orden) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdenDeInspeccion ordenMerged = em.merge(orden);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    // ==================== MÉTODOS PARA SISMÓGRAFOS ====================

    public static void guardarSismografo(Sismografo sismografo) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Sismografo sismografoMerged = em.merge(sismografo);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    // ==================== MÉTODOS PARA ESTACIONES ====================

    public static void guardarEstacionSismologica(EstacionSismologica estacion) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            EstacionSismologica estacionMerged = em.merge(estacion);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    // ==================== MÉTODOS PARA CAMBIOS DE ESTADO ====================

    public static void guardarCambioDeEstado(CambioDeEstado cambio) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            CambioDeEstado cambioMerged = em.merge(cambio);
            em.flush();
            tx.commit();
            System.out.println("✓ CambioDeEstado persistido con fechaHoraFin: " + cambio.getFechaHoraFin());
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    // ==================== GETTERS PARA BOUNDARY ====================

    public static InterfazNotificacionMail getInterfazMail() {
        return interfazMail;
    }

    public static List<MonitorCCRS> getMonitores() {
        return monitorManager.getMonitores();
    }

    public static MonitorManager getMonitorManager() {
        return monitorManager;
    }
}
