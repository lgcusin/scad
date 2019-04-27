package servicios;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Facultad;
import model.Feriado;

/**
 * Session Bean implementation class SrvRegistroFeriado
 */
@Stateless
@LocalBean
@SuppressWarnings("unchecked")
public class SrvRegistroFeriado implements SrvRegistroFeriadoLocal {

	@PersistenceContext
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvRegistroFeriado() {
		/** Constructor vacio */
	}

	@Override
	public Collection<Feriado> listarFeriados(Integer fclId, Date fechaInicio, Date fechaFin) {
		Collection<Feriado> lstF = null;
		try {
			Query query = em.createQuery(
					"select f from Feriado as f where f.fclId=:fclId and f.frdFecha >= :fechaInicio and f.frdFecha <= :fechaFin");
			query.setParameter("fclId", fclId);
			query.setParameter("fechaInicio", fechaInicio);
			query.setParameter("fechaFin", fechaFin);
			lstF = (Collection<Feriado>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
		}
		return lstF;
	}

	@Override
	public void guardarActualizarFeriado(Feriado feriado) {
		try {
			if (feriado.getFrdId() > 0) {
				em.merge(feriado);
			} else {
				// Feriado f = obtenerSecuenciaParametro();
				// feriado.setFrdId(f.getFrdId() + 1);
				em.persist(feriado);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarParametro " + e);
		}
	}

	@Override
	public void eliminarFeriado(Feriado feriado) {
		try {
			Feriado f = em.find(Feriado.class, feriado.getFrdId());
			if (f != null) {
				em.remove(f);
			}
		} catch (Exception e) {
			System.out.println("Error al eliminar feriado" + e);
		}
	}

	@Override
	public List<Facultad> listarFacultades() {
		List<Facultad> lstFacultades = null;
		try {
			Query query = em.createQuery("select f from Facultad as f");
			System.out.println("Valores de la lista");
			lstFacultades = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar facultades" + e);
		}
		return lstFacultades;
	}

}
