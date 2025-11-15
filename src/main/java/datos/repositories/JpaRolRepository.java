package datos.repositories;

import jakarta.persistence.EntityManager;
import datos.persistence.LocalEntityManagerProvider;
import jakarta.persistence.TypedQuery;
import model.Rol;
import java.util.Optional;

public class JpaRolRepository extends JpaRepositoryBase<Rol, Integer> {

    public JpaRolRepository(EntityManager em) {
        super(em, Rol.class);
    }

    public Optional<Rol> findByNombre(String nombre) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<Rol> query = em.createQuery(
                    "SELECT r FROM Rol r WHERE r.nombre = :nombre", Rol.class);
            query.setParameter("nombre", nombre);
            return query.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }
}
