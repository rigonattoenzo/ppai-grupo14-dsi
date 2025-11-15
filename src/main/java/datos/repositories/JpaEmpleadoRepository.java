package datos.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import datos.persistence.LocalEntityManagerProvider;
import model.Empleado;
import model.Rol;
import java.util.List;
import java.util.Optional;

public class JpaEmpleadoRepository extends JpaRepositoryBase<Empleado, Integer> {

    public JpaEmpleadoRepository(EntityManager em) {
        super(em, Empleado.class);
    }

    public Optional<Empleado> findByCodigo(String codigo) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<Empleado> query = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.codigo = :cod", Empleado.class);
            query.setParameter("cod", codigo);
            return query.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    public List<Empleado> findByRol(Rol rol) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<Empleado> query = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.rol = :rol", Empleado.class);
            query.setParameter("rol", rol);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Empleado> findResponsablesReparacion() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            TypedQuery<Empleado> query = em.createQuery(
                    "SELECT e FROM Empleado e WHERE e.rol.nombre = 'RR'", Empleado.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
