package datos.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.OrdenDeInspeccion;
import model.Empleado;
import java.util.List;
import java.util.Optional;

public class JpaOrdenInspeccionRepository extends JpaRepositoryBase<OrdenDeInspeccion, Integer> {

    public JpaOrdenInspeccionRepository(EntityManager em) {
        super(em, OrdenDeInspeccion.class);
    }

    public Optional<OrdenDeInspeccion> findByNumeroOrden(Integer numeroOrden) {
        TypedQuery<OrdenDeInspeccion> query = em.createQuery(
                "SELECT o FROM OrdenDeInspeccion o WHERE o.numeroOrden = :nro", OrdenDeInspeccion.class);
        query.setParameter("nro", numeroOrden);
        return query.getResultStream().findFirst();
    }

    public List<OrdenDeInspeccion> findByEmpleado(Empleado empleado) {
        TypedQuery<OrdenDeInspeccion> query = em.createQuery(
                "SELECT o FROM OrdenDeInspeccion o WHERE o.empleado = :emp", OrdenDeInspeccion.class);
        query.setParameter("emp", empleado);
        return query.getResultList();
    }

    public List<OrdenDeInspeccion> findCompletamenteRealizadas() {
        return em.createQuery(
                "SELECT o FROM OrdenDeInspeccion o WHERE o.estadoActual = 'CompletamenteRealizada'",
                OrdenDeInspeccion.class).getResultList();
    }
}
