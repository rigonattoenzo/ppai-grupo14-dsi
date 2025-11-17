package datos.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Proveedor singleton de EntityManager para la aplicación.
 * Maneja el ciclo de vida de la fábrica de EntityManager.
 */
public final class LocalEntityManagerProvider {

    private static final String PERSISTENCE_UNIT = "ppai-pu";
    private static final EntityManagerFactory EMF;

    static {
        try {
            // Crear BD + DDL
            DatabaseInitializer.initializeDatabase();

            // Crear factory
            EMF = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

            // Utilizar la seed (EMF ya existe)
            DataSeeder.initializeTestDataIfEmpty();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Error inicializando JPA: " + e.getMessage());
        }
    }

    private LocalEntityManagerProvider() {
    }

    /**
     * Obtiene un EntityManager de la fábrica.
     * El cliente es responsable de cerrar el EM cuando termine.
     */
    public static EntityManager createEntityManager() {
        return EMF.createEntityManager();
    }

    /**
     * Cierra la fábrica de EntityManager (llamar al cerrar la aplicación).
     */
    public static void close() {
        if (EMF != null && EMF.isOpen()) {
            EMF.close();
        }
    }

    public static EntityManagerFactory getFactory() {
        return EMF;
    }
}
