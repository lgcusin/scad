package servicios;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Carrera;

/**
 * Session Bean implementation class SrvEmpleado
 */
@Stateless
@LocalBean
public class SrvEmpleado implements SrvEmpleadoLocal {

	@PersistenceContext
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvEmpleado() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Carrera> listarCarrera() {
		List<Carrera> lstC = em.createNamedQuery("Carrera.findAll", Carrera.class).getResultList();
		return lstC;
	}

}
