package datos;

import model.*;
import datos.repositories.*;
import datos.persistence.LocalEntityManagerProvider;
import boundary.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class RepositorioDatos {

    private static InterfazNotificacionMail interfazMail = InterfazNotificacionMail.getInstancia();
    private static MonitorCCRS monitor = MonitorCCRS.getInstancia();

    static {
        System.out.println("\n🔍 [RepositorioDatos] Static initializer - Verificando si usuarios están vacíos...");
        if (usuariosVacios()) {
            System.out.println("✅ [RepositorioDatos] Usuarios vacíos - Iniciando inicialización de datos");
            inicializarDatos();
        } else {
            System.out.println("⏭️ [RepositorioDatos] Datos ya existen - Saltando inicialización");
        }
    }

    private static boolean usuariosVacios() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaUsuarioRepository repo = new JpaUsuarioRepository(em);
            List<Usuario> usuarios = repo.findAll();
            return usuarios.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            em.close();
        }
    }

    private static void inicializarDatos() {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📝 INICIALIZANDO DATOS DE PRUEBA EN BD");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        EntityManager emTx = LocalEntityManagerProvider.createEntityManager();
        EntityTransaction tx = emTx.getTransaction();

        try {
            System.out.println("1️⃣ Iniciando transacción...");
            tx.begin();

            // ========== Roles ==========
            System.out.println("2️⃣ Creando roles...");
            Rol rolRI = new Rol("RI", "Responsable Inspección");
            Rol rolRR = new Rol("RR", "Responsable Reparación");
            emTx.persist(rolRI);
            emTx.persist(rolRR);
            System.out.println("   ✓ Roles creados y flushed");

            // ========== Empleados ==========
            System.out.println("3️⃣ Creando empleados...");
            Empleado emp1 = new Empleado("Pepe", "Gonzales", "0001", "3512718234", "pepe@mail.com", rolRI);
            Empleado emp2 = new Empleado("Otro", "Empleado", "0002", "3512234534", "otro@mail.com", rolRI);
            Empleado emp3 = new Empleado("Claudia", "Reparadora", "0003", "3512798876", "claudia@mail.com", rolRR);
            Empleado emp4 = new Empleado("Mateo", "Reparador", "0004", "3512758697", "matute@mail.com", rolRR);
            emTx.persist(emp1);
            emTx.persist(emp2);
            emTx.persist(emp3);
            emTx.persist(emp4);
            System.out.println("   ✓ 4 Empleados creados y flushed");

            // ========== Usuarios ==========
            System.out.println("4️⃣ Creando usuarios...");
            Usuario usuario1 = new Usuario("pepe123", "pass", emp1);
            Usuario usuario2 = new Usuario("potro123", "pepass", emp2);
            Usuario usuario3 = new Usuario("clau123", "pass123", emp3);
            Usuario usuario4 = new Usuario("mateo123", "pass456", emp4);
            emTx.persist(usuario1);
            emTx.persist(usuario2);
            emTx.persist(usuario3);
            emTx.persist(usuario4);
            System.out.println("   ✓ 4 Usuarios creados y flushed");

            // ========== Motivos ==========
            System.out.println("5️⃣ Creando motivos...");
            MotivoTipo mot1 = new MotivoTipo("Sensor dañado");
            MotivoTipo mot2 = new MotivoTipo("Interferencia eléctrica");
            MotivoTipo mot3 = new MotivoTipo("Condiciones climáticas adversas");
            emTx.persist(mot1);
            emTx.persist(mot2);
            emTx.persist(mot3);
            System.out.println("   ✓ 3 Motivos creados y flushed");

            // ========== Cambios de Estado ==========
            model.estados.Estado enLinea = new model.estados.EnLinea();
            model.estados.Estado inhabilitado = new model.estados.InhabilitadoPorInspeccion();
            model.estados.Estado fueraDeServicio = new model.estados.FueraServicio();

            CambioDeEstado cambioEstado1 = new CambioDeEstado(enLinea, LocalDateTime.of(2024, 3, 4, 6, 10));
            CambioDeEstado cambioEstado2 = new CambioDeEstado(inhabilitado, LocalDateTime.of(2023, 6, 7, 16, 45));
            CambioDeEstado cambioEstado3 = new CambioDeEstado(fueraDeServicio, LocalDateTime.of(2024, 5, 7, 19, 40));
            CambioDeEstado cambioEstado4 = new CambioDeEstado(fueraDeServicio, LocalDateTime.of(2022, 10, 10, 10, 10));
            emTx.persist(cambioEstado1);
            emTx.persist(cambioEstado2);
            emTx.persist(cambioEstado3);
            emTx.persist(cambioEstado4);

            List<CambioDeEstado> cambiosEstado = new ArrayList<>();
            cambiosEstado.add(cambioEstado1);
            cambiosEstado.add(cambioEstado2);
            cambiosEstado.add(cambioEstado3);
            cambiosEstado.add(cambioEstado4);

            // ========== Estaciones ==========
            System.out.println("6️⃣ Creando estaciones...");
            EstacionSismologica est1 = new EstacionSismologica(
                    "EST-001", "DOC-001", LocalDateTime.of(2024, 10, 1, 9, 0),
                    -34.5, -58.4, "La Plata", "CERT-001", null);
            EstacionSismologica est2 = new EstacionSismologica(
                    "EST-002", "DOC-002", LocalDateTime.of(2024, 11, 1, 9, 0),
                    -31.4, -64.2, "Córdoba", "CERT-002", null);
            System.out.println("   ✓ 2 Estaciones creadas y flushed");

            // ========== Sismógrafos ==========
            Sismografo sism1 = new Sismografo("SISM-001", "334253", LocalDateTime.of(2023, 10, 1, 15, 34), est1);
            Sismografo sism2 = new Sismografo("SISM-004", "444332", LocalDateTime.of(2024, 03, 10, 10, 30), est2);

            sism1.setEstadoActual(inhabilitado);
            sism2.setEstadoActual(inhabilitado);

            est1.setSismografo(sism1);
            est2.setSismografo(sism2);

            sism1.setCambiosDeEstado(cambiosEstado);
            sism2.setCambiosDeEstado(cambiosEstado);

            emTx.persist(est1);
            emTx.persist(est2);
            emTx.persist(sism1);
            emTx.persist(sism2);
            System.out.println("   ✓ 2 Sismógrafos creados y flushed");

            // ========== Órdenes de Inspección ==========
            OrdenDeInspeccion o1 = new OrdenDeInspeccion(101, LocalDateTime.of(2025, 5, 1, 9, 0),
                    LocalDateTime.of(2025, 5, 3, 17, 0), emp1, est1);
            o1.completarTodas();

            OrdenDeInspeccion o2 = new OrdenDeInspeccion(102, LocalDateTime.of(2025, 5, 2, 9, 0),
                    LocalDateTime.of(2025, 5, 4, 17, 0), emp2, est2);

            OrdenDeInspeccion o3 = new OrdenDeInspeccion(103, LocalDateTime.of(2025, 4, 28, 9, 0),
                    LocalDateTime.of(2025, 5, 1, 12, 0), emp1, est1);
            o3.completarTodas();

            emTx.persist(o1);
            emTx.persist(o2);
            emTx.persist(o3);
            System.out.println("   📋 Orden #103 - Estado: " + o3.sosCompletamenteRealizada());

            emTx.persist(o1);
            emTx.persist(o2);
            emTx.persist(o3);
            emTx.flush();
            System.out.println("   ✓ 3 Órdenes creadas y flushed");

            System.out.println("9️⃣ Commiteando transacción...");
            tx.commit();
            System.out.println("✅ TRANSACCIÓN EXITOSA - Datos inicializados correctamente");
            System.out.println(
                    "   📊 TOTALES: 2 roles, 4 empleados, 4 usuarios, 3 motivos, 2 estaciones, 2 sismógrafos, 3 órdenes\n");

        } catch (Exception e) {
            System.err.println("❌ ERROR EN INICIALIZACIÓN: " + e.getMessage());
            if (tx.isActive()) {
                System.err.println("   ↻ Rollback de transacción...");
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            System.out.println("🔒 Cerrando EntityManager de inicialización...");
            emTx.close();
        }
    }

    // ==================== MÉTODOS DE ACCESO ====================

    public static List<OrdenDeInspeccion> obtenerOrdenes() {
        System.out.println("\n🔍 [obtenerOrdenes] Buscando órdenes en BD...");
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaOrdenInspeccionRepository repo = new JpaOrdenInspeccionRepository(em);
            List<OrdenDeInspeccion> ordenes = repo.findAll();
            System.out.println("✅ [obtenerOrdenes] Se encontraron " + ordenes.size() + " órdenes");
            for (OrdenDeInspeccion o : ordenes) {
                System.out.println("   📋 Orden #" + o.getNroDeOrden() +
                        " - Completamente Realizada: " + o.sosCompletamenteRealizada());
            }
            return ordenes;
        } catch (Exception e) {
            System.err.println("❌ Error al obtener órdenes: " + e.getMessage());
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

    public static List<MotivoTipo> getMotivos() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            JpaMotivoTipoRepository repo = new JpaMotivoTipoRepository(em);
            return repo.findAll();
        } finally {
            em.close();
        }
    }

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
            JpaOrdenInspeccionRepository repo = new JpaOrdenInspeccionRepository(em);
            repo.save(orden);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static InterfazNotificacionMail getInterfazMail() {
        return interfazMail;
    }

    public static MonitorCCRS getMonitor() {
        return monitor;
    }

    public static List<MonitorCCRS> getMonitores() {
        List<MonitorCCRS> monitores = new ArrayList<>();
        monitores.add(MonitorCCRS.getInstancia());
        return monitores;
    }

    public static void guardarCambioDeEstado(CambioDeEstado cambio) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            JpaCambioDeEstadoRepository repo = new JpaCambioDeEstadoRepository(em);
            repo.save(cambio);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
