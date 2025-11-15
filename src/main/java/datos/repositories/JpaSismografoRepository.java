package datos.repositories;

import jakarta.persistence.EntityManager;
import datos.persistence.LocalEntityManagerProvider;
import jakarta.persistence.TypedQuery;
import model.Sismografo;
import model.EstacionSismologica;
import java.util.List;
import java.util.Optional;

public class JpaSismografoRepository extends JpaRepositoryBase<Sismografo, Integer> {

    public JpaSismografoRepository(EntityManager em) {
        super(em, Sismografo.class);
    }

    public Optional<Sismografo> findByIdentificador(String identificador) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<Sismografo> query = em.createQuery(
                    "SELECT s FROM Sismografo s WHERE s.identificadorSismografo = :id", Sismografo.class);
            query.setParameter("id", identificador);
            return query.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<Sismografo> findByEstacion(EstacionSismologica estacion) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<Sismografo> query = em.createQuery(
                    "SELECT s FROM Sismografo s WHERE s.estacionSismologica = :est", Sismografo.class);
            query.setParameter("est", estacion);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
