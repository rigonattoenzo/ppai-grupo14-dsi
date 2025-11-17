package datos.persistence;

import model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;

import boundary.MonitorManager;

/**
 * Inserta datos de prueba
 * - Se ejecuta una sola vez
 * - Solo persiste entities
 */
public final class DataSeeder {
    private DataSeeder() {
    }

    public static void initializeTestDataIfEmpty() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            Long usuarioCount = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class)
                    .getSingleResult();

            if (usuarioCount > 0) {
                System.out.println("⏭️ [DataSeeder] Datos ya existen - Saltando seed");
                return;
            }

            System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("📝 INSERTANDO DATOS DE PRUEBA");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            EntityTransaction tx = em.getTransaction();
            tx.begin();

            crearRoles(em);
            crearEmpleados(em);
            crearUsuarios(em);
            crearMotivos(em);
            crearCambiosDeEstado(em);
            crearEstacionesYSismografos(em);
            crearOrdenes(em);
            crearMonitores(); // ✅ Monitores en memoria, no persisten

            tx.commit();
        } catch (Exception e) {
            System.err.println("❌ ERROR AL INSERTAR DATOS: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // ========== MÉTODOS PRIVADOS DE CREACIÓN ==========

    private static void crearRoles(EntityManager em) {
        Rol rolRI = new Rol("RI", "Responsable Inspección");
        Rol rolRR = new Rol("RR", "Responsable Reparación");
        em.persist(rolRI);
        em.persist(rolRR);
    }

    private static void crearEmpleados(EntityManager em) {
        Rol rolRI = em.createQuery("SELECT r FROM Rol r WHERE r.nombre = 'RI'", Rol.class)
                .getSingleResult();
        Rol rolRR = em.createQuery("SELECT r FROM Rol r WHERE r.nombre = 'RR'", Rol.class)
                .getSingleResult();

        Empleado emp1 = new Empleado("Pepe", "Gonzales", "0001", "3512718234", "pepe@mail.com", rolRI);
        Empleado emp2 = new Empleado("Otro", "Empleado", "0002", "3512234534", "otro@mail.com", rolRI);
        Empleado emp3 = new Empleado("Claudia", "Reparadora", "0003", "3512798876", "claudia@mail.com", rolRR);
        Empleado emp4 = new Empleado("Mateo", "Reparador", "0004", "3512758697", "matute@mail.com", rolRR);

        em.persist(emp1);
        em.persist(emp2);
        em.persist(emp3);
        em.persist(emp4);
    }

    private static void crearUsuarios(EntityManager em) {
        List<Empleado> empleados = em.createQuery("SELECT e FROM Empleado e", Empleado.class)
                .getResultList();

        Usuario usuario1 = new Usuario("pepe123", "pass", empleados.get(0));
        Usuario usuario2 = new Usuario("potro123", "pepass", empleados.get(1));
        Usuario usuario3 = new Usuario("clau123", "pass123", empleados.get(2));
        Usuario usuario4 = new Usuario("mateo123", "pass456", empleados.get(3));

        em.persist(usuario1);
        em.persist(usuario2);
        em.persist(usuario3);
        em.persist(usuario4);
    }

    private static void crearMotivos(EntityManager em) {
        MotivoTipo mot1 = new MotivoTipo("Sensor dañado");
        MotivoTipo mot2 = new MotivoTipo("Interferencia eléctrica");
        MotivoTipo mot3 = new MotivoTipo("Condiciones climáticas adversas");
        em.persist(mot1);
        em.persist(mot2);
        em.persist(mot3);
    }

    private static void crearCambiosDeEstado(EntityManager em) {
        model.estados.Estado enLinea = new model.estados.EnLinea();
        model.estados.Estado inhabilitado = new model.estados.InhabilitadoPorInspeccion();
        model.estados.Estado fueraDeServicio = new model.estados.FueraServicio();

        CambioDeEstado cambioEstado1 = new CambioDeEstado(enLinea, LocalDateTime.of(2024, 3, 4, 6, 10));
        CambioDeEstado cambioEstado2 = new CambioDeEstado(inhabilitado, LocalDateTime.of(2023, 6, 7, 16, 45));
        CambioDeEstado cambioEstado3 = new CambioDeEstado(fueraDeServicio, LocalDateTime.of(2024, 5, 7, 19, 40));
        CambioDeEstado cambioEstado4 = new CambioDeEstado(inhabilitado, LocalDateTime.of(2022, 10, 10, 10, 10));
        em.persist(cambioEstado1);
        em.persist(cambioEstado2);
        em.persist(cambioEstado3);
        em.persist(cambioEstado4);
    }

    private static void crearEstacionesYSismografos(EntityManager em) {
        model.estados.Estado inhabilitado = new model.estados.InhabilitadoPorInspeccion();
        List<CambioDeEstado> cambiosEstado = em.createQuery("SELECT c FROM CambioDeEstado c", CambioDeEstado.class)
                .getResultList();

        EstacionSismologica est1 = new EstacionSismologica(
                "EST-001", "DOC-001", LocalDateTime.of(2024, 10, 1, 9, 0),
                -34.5, -58.4, "La Plata", "CERT-001", null);
        EstacionSismologica est2 = new EstacionSismologica(
                "EST-002", "DOC-002", LocalDateTime.of(2024, 11, 1, 9, 0),
                -31.4, -64.2, "Córdoba", "CERT-002", null);
        EstacionSismologica est3 = new EstacionSismologica(
                "EST-004", "DOC-006", LocalDateTime.of(2023, 3, 2, 9, 0),
                -35.0, -70, "Puerto Madryn", "CERT-005", null);

        Sismografo sism1 = new Sismografo("SISM-001", "334253", LocalDateTime.of(2023, 10, 1, 15, 34), est1);
        Sismografo sism2 = new Sismografo("SISM-004", "444332", LocalDateTime.of(2024, 3, 10, 10, 30), est2);
        Sismografo sism3 = new Sismografo("SISM-010", "997754", LocalDateTime.of(2022, 8, 4, 16, 50), est3);

        sism1.setEstadoActual(inhabilitado);
        sism2.setEstadoActual(inhabilitado);
        sism3.setEstadoActual(inhabilitado);

        est1.setSismografo(sism1);
        est2.setSismografo(sism2);
        est3.setSismografo(sism3);

        sism1.setCambiosDeEstado(cambiosEstado);
        sism2.setCambiosDeEstado(cambiosEstado);
        sism3.setCambiosDeEstado(cambiosEstado);

        em.persist(est1);
        em.persist(est2);
        em.persist(est3);
        em.persist(sism1);
        em.persist(sism2);
        em.persist(sism3);
        System.out.println("   ✓ 3 Estaciones y 3 Sismógrafos creados");
    }

    private static void crearOrdenes(EntityManager em) {
        System.out.println("8️⃣ Creando órdenes de inspección...");
        Empleado emp1 = em.createQuery("SELECT e FROM Empleado e WHERE e.codigo = '0001'", Empleado.class)
                .getSingleResult();
        Empleado emp2 = em.createQuery("SELECT e FROM Empleado e WHERE e.codigo = '0002'", Empleado.class)
                .getSingleResult();
        EstacionSismologica est1 = em
                .createQuery("SELECT e FROM EstacionSismologica e WHERE e.codigoEstacion = 'EST-001'",
                        EstacionSismologica.class)
                .getSingleResult();
        EstacionSismologica est2 = em
                .createQuery("SELECT e FROM EstacionSismologica e WHERE e.codigoEstacion = 'EST-002'",
                        EstacionSismologica.class)
                .getSingleResult();
        EstacionSismologica est3 = em
                .createQuery("SELECT e FROM EstacionSismologica e WHERE e.codigoEstacion = 'EST-004'",
                        EstacionSismologica.class)
                .getSingleResult();

        OrdenDeInspeccion o1 = new OrdenDeInspeccion(101, LocalDateTime.of(2025, 5, 1, 9, 0),
                LocalDateTime.of(2025, 5, 3, 17, 0), emp1, est1);
        o1.completarTodas();

        OrdenDeInspeccion o2 = new OrdenDeInspeccion(102, LocalDateTime.of(2025, 5, 2, 9, 0),
                LocalDateTime.of(2025, 5, 4, 17, 0), emp2, est2);

        OrdenDeInspeccion o3 = new OrdenDeInspeccion(103, LocalDateTime.of(2025, 4, 28, 9, 0),
                LocalDateTime.of(2025, 5, 1, 12, 0), emp1, est3);
        o3.completarTodas();

        em.persist(o1);
        em.persist(o2);
        em.persist(o3);
    }

    private static void crearMonitores() {
        MonitorManager mm = MonitorManager.getInstancia();
        // Ya se crean en el constructor del MonitorManager
    }
}
