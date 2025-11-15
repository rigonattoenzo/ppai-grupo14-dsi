package datos.repositories;

import jakarta.persistence.EntityManager;
import datos.persistence.LocalEntityManagerProvider;
import jakarta.persistence.TypedQuery;
import model.CambioDeEstado;
import model.Sismografo;
import java.util.List;

public class JpaCambioDeEstadoRepository extends JpaRepositoryBase<CambioDeEstado, Integer> {

    public JpaCambioDeEstadoRepository(EntityManager em) {
        super(em, CambioDeEstado.class);
    }

    public List<CambioDeEstado> findBySismografo(Sismografo sismografo) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<CambioDeEstado> query = em.createQuery(
                    "SELECT c FROM CambioDeEstado c WHERE c.sismografo = :sism ORDER BY c.fechaHoraInicio DESC",
                    CambioDeEstado.class);
            query.setParameter("sism", sismografo);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<CambioDeEstado> findActuales() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<CambioDeEstado> query = em.createQuery(
                    "SELECT c FROM CambioDeEstado c WHERE c.fechaHoraFin IS NULL",
                    CambioDeEstado.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
