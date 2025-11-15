package datos.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.EstacionSismologica;
import java.util.Optional;

public class JpaEstacionRepository extends JpaRepositoryBase<EstacionSismologica, Integer> {

    public JpaEstacionRepository(EntityManager em) {
        super(em, EstacionSismologica.class);
    }

    public Optional<EstacionSismologica> findByCodigoEstacion(String codigo) {
        TypedQuery<EstacionSismologica> query = em.createQuery(
                "SELECT e FROM EstacionSismologica e WHERE e.codigoEstacion = :cod", EstacionSismologica.class);
        query.setParameter("cod", codigo);
        return query.getResultStream().findFirst();
    }
}
