package servicios;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Actividad;
import model.Carrera;
import model.Contenido;
import model.Herramienta;
import model.Materia;
import model.Syllabo;
import model.UnidadCurricular;

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

	@Override
	public Carrera getCarrera(Integer crrId) {
		Carrera cr = em.createNamedQuery("Carrera.findByMtrId", Carrera.class).setParameter("mtrId", crrId)
				.getSingleResult();
		return cr;
	}

	@Override
	public Syllabo getSyllabus(Integer mtrId) {
		Syllabo syl = em.createNamedQuery("Syllabo.findByMtrId", Syllabo.class).setParameter("mtrId", mtrId)
				.getSingleResult();
		return syl;
	}

	@Override
	public List<UnidadCurricular> listarUnidadCurricular(Integer mtrId) {
		List<UnidadCurricular> lstUc = em.createNamedQuery("UnidadCurricular.findAllBySylId", UnidadCurricular.class)
				.setParameter("mtrId", mtrId).getResultList();
		return lstUc;
	}

	@Override
	public List<Contenido> listarContenidos(Integer uncrId) {
		List<Contenido> lstCn = em.createNamedQuery("Contenido.findAllByUcId", Contenido.class)
				.setParameter("ucId", uncrId).getResultList();
		return lstCn;
	}

	@Override
	public List<Actividad> listarActividades(Integer cntId) {
		List<Actividad> lstAc = em.createNamedQuery("Actividad.findAllByCnId", Actividad.class)
				.setParameter("cntId", cntId).getResultList();
		return lstAc;
	}

	@Override
	public List<Herramienta> listarHerramientas(Integer cntId) {
		List<Herramienta> lstHr = em.createNamedQuery("Herramienta.findAllByCnId", Herramienta.class)
				.setParameter("cntId", cntId).getResultList();
		return lstHr;
	}

}
