package servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Carrera;
import model.FichaDocente;

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
	public List<Carrera> listarCarreras() {
		List<Carrera> lstC;
		try {
			lstC = em.createNamedQuery("Carrera.findAll", Carrera.class).getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return lstC = new ArrayList<>();
		}

		return lstC;
	}

	@Override
	public List<FichaDocente> listarDocentes(Integer crrId) {
		List<FichaDocente> lstD;
		try {
			lstD = em.createNamedQuery("Docente.findAllByCrrId", FichaDocente.class).setParameter("crId", crrId)
					.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return lstD = new ArrayList<>();
		}
		return lstD;
	}

}