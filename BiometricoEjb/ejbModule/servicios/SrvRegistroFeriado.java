package servicios;

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

	/**
	 * Metodo definido para consultar los feriados segun los filtros ingresados.
	 */
	@Override
	public List<Feriado> listarFeriados(Integer fclId, Date fechaInicio, Date fechaFin) {
		List<Feriado> lstF = null;
		try {
			Query query = em.createQuery(
					"select f from Feriado as f where (f.fclId=:fclId or f.fclId=1) and f.frdFecha >= :fechaInicio and f.frdFecha <= :fechaFin order by f.frdFecha asc");
			query.setParameter("fclId", fclId);
			query.setParameter("fechaInicio", fechaInicio);
			query.setParameter("fechaFin", fechaFin);
			lstF = (List<Feriado>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
		}
		return lstF;
	}

	/**
	 * Metodo definido para guardar o actualizar un registro de feriado.
	 */
	@Override
	public void guardarActualizarFeriado(Feriado feriado) {
		try {
			if (feriado.getFrdId() > 0) {
				em.merge(feriado);
			} else {
				Feriado f = obtenerSecuenciaFeriado();
				if (f != null && f.getFrdId() > 0) {
					feriado.setFrdId(f.getFrdId() + 1);
					em.persist(feriado);
				} else {
					System.out.println("Error al guardar Feriado");
				}
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarParametro: " + e);
		}
	}

	/**
	 * Metodo definido para eliminar un registro de feriado.
	 */
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

	/**
	 * Metodo definido para listar las facultades disponibles.
	 */
	@Override
	public List<Facultad> listarFacultades() {
		List<Facultad> lstFacultades = null;
		try {
			Query query = em.createQuery("select f from Facultad as f");
			lstFacultades = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar facultades" + e);
		}
		return lstFacultades;
	}

	/**
	 * Metodo definido para obtener la secuencia de feriados.
	 * 
	 * @return
	 */
	private Feriado obtenerSecuenciaFeriado() {
		Feriado feriado = null;
		try {
			Query query = em.createQuery("select f from Feriado as f  order by f.frdId desc");
			query.setMaxResults(1);
			feriado = (Feriado) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar la secuencia del feriado" + e);
		}
		return feriado;
	}
}