package datos.repositories;

import jakarta.persistence.EntityManager;
import model.MotivoTipo;

public class JpaMotivoTipoRepository extends JpaRepositoryBase<MotivoTipo, Integer> {

    public JpaMotivoTipoRepository(EntityManager em) {
        super(em, MotivoTipo.class);
    }
}
