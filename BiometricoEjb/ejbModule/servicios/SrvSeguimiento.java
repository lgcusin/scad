package servicios;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Carrera;
import model.Materia;
import model.Syllabo;

/**
 * Session Bean implementation class SrvSeguimiento
 */
@Stateless
@LocalBean
public class SrvSeguimiento implements SrvSeguimientoLocal {

	@PersistenceContext
	EntityManager em;

	public SrvSeguimiento() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Materia> listarAllMat() {
		List<Materia> lstM = em.createNamedQuery("Materia.findAll", Materia.class).getResultList();
		return lstM;
	}

	@Override
	public List<Carrera> listarAllCrr() {
		List<Carrera> lstC = em.createNamedQuery("Carrera.findAllC", Carrera.class).getResultList();
		return lstC;
	}

	@Override
	public List<Materia> listarMatByCrr(Integer id) {
		List<Materia> lstM = em.createNamedQuery("Materia.findAllById", Materia.class).setParameter("idcr", id)
				.getResultList();
		return lstM;
	}

}
