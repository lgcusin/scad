package beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import model.FichaDocente;
import model.Usuario;

/**
 * Session Bean implementation class DocenteBean
 */
@Stateless
@LocalBean
public class DocenteBean implements DocenteBeanLocal {

	public DocenteBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void guardar(FichaDocente fcdc) {

	}

	@Override
	public List<FichaDocente> listar(String param) {
		param = "%" + param + "%";
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
		return em.createNamedQuery("Docente.listar", FichaDocente.class).setParameter("apellido", param)
				.setParameter("primernombre", param).setParameter("segundonombre", param).getResultList();

	}

}
