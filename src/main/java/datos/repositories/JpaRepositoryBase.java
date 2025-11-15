package datos.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import datos.persistence.LocalEntityManagerProvider;
import java.util.List;
import java.util.Optional;

/**
 * Base genérica para repositorios JPA.
 * Proporciona operaciones CRUD estándar.
 */
public abstract class JpaRepositoryBase<T, ID> {

    protected Class<T> entityClass;
    protected EntityManager em;

    protected JpaRepositoryBase(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    /**
     * Ejecuta una operación dentro de una transacción.
     */
    protected <R> R tx(java.util.function.Function<EntityManager, R> work) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException ex) {
            if (tx.isActive())
                tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * Ejecuta una operación sin retorno dentro de transacción.
     */
    protected void txVoid(java.util.function.Consumer<EntityManager> work) {
        tx(em -> {
            work.accept(em);
            return null;
        });
    }

    // ===== CRUD =====

    public T save(T entity) {
        return tx(em -> {
            if (isNew(entity)) {
                em.persist(entity);
                return entity;
            } else {
                return em.merge(entity);
            }
        });
    }

    public Optional<T> findById(ID id) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            return Optional.ofNullable(em.find(entityClass, id));
        } finally {
            em.close();
        }
    }

    public List<T> findAll() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> q = em.createQuery(jpql, entityClass);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public long count() {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<Long> q = em.createQuery(jpql, Long.class);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public void delete(T entity) {
        txVoid(em -> {
            T managed = entity;
            if (!em.contains(entity)) {
                Object id = getId(entity);
                if (id != null) {
                    managed = em.find(entityClass, id);
                }
            }
            if (managed != null)
                em.remove(managed);
        });
    }

    public void deleteById(ID id) {
        txVoid(em -> {
            T managed = em.find(entityClass, id);
            if (managed != null)
                em.remove(managed);
        });
    }

    public List<T> findAll(int offset, int limit) {
        EntityManager em = LocalEntityManagerProvider.createEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> q = em.createQuery(jpql, entityClass);
            if (offset > 0)
                q.setFirstResult(offset);
            if (limit > 0)
                q.setMaxResults(limit);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // ===== Helpers =====

    protected boolean isNew(T entity) {
        return getId(entity) == null;
    }

    protected Object getId(T entity) {
        try {
            var m = entity.getClass().getMethod("getId");
            return m.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }
}
