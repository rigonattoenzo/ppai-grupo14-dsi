package datos.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Usuario;
import java.util.Optional;

public class JpaUsuarioRepository extends JpaRepositoryBase<Usuario, Integer> {

    public JpaUsuarioRepository(EntityManager em) {
        super(em, Usuario.class);
    }

    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombre", Usuario.class);
        query.setParameter("nombre", nombreUsuario);
        return query.getResultStream().findFirst();
    }
}
