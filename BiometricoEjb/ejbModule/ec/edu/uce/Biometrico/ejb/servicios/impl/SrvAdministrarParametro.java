package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvAdministrarParametroLocal;
import ec.uce.edu.biometrico.jpa.Dependencia;
import ec.uce.edu.biometrico.jpa.Parametro;

/**
 * Session Bean implementation class SrvAdministrarParametro
 */
@Stateless
@LocalBean
@SuppressWarnings("unchecked")
public class SrvAdministrarParametro implements SrvAdministrarParametroLocal {

	@PersistenceContext
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvAdministrarParametro() {
		/** Constructor vacio */
	}

	/**
	 * Metodo definido para buscar los parametros por facultad.
	 */
	@Override
	public List<Parametro> listarParametroxFacultad(Integer fclId) {
		List<Parametro> lstP = null;
		try {
			Query query = em.createQuery("select p from Parametro as p where p.dependencia.dpnId=:fclId");
			query.setParameter("fclId", fclId);
			lstP = (List<Parametro>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Parametros: " + e);
		}
		return lstP;
	}

	/**
	 * Metodo definido para guardar o actualizar un parametro.
	 */
	@Override
	public void guardarActualizarParametro(Parametro parametro) {
		try {
			if (parametro.getPrmId() != 0) {
				em.merge(parametro);
			} else {
				// Parametro p = obtenerSecuenciaParametro();
				// parametro.setPrmId(p.getPrmId() + 1);
				em.persist(parametro);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarParametro: " + e);
		}
	}

	/**
	 * Metodo definido para buscar las facultades.
	 */
	@Override
	public List<Dependencia> listarFacultades() {
		List<Dependencia> lstFacultades = null;
		try {
			Query query = em.createQuery("select f from Facultad as f");
			lstFacultades = (List<Dependencia>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar facultades: " + e);
		}
		return lstFacultades;
	}

	/**
	 * Metodo definido para obtener la secuencia de base de datos para el nuevo
	 * registro de parametro.
	 * 
	 * @return
	 */
//	private Parametro obtenerSecuenciaParametro() {
//		Parametro parametro = null;
//		try {
//			Query query = em.createQuery("select p from Parametro as p  order by p.prmId desc");
//			query.setMaxResults(1);
//			parametro = (Parametro) query.getSingleResult();
//		} catch (Exception e) {
//			System.out.println("Error al consultar la secuencia del parametro: " + e);
//		}
//		return parametro;
//	}
}
