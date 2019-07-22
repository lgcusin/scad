package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvRegistroFeriadoLocal;
import ec.uce.edu.biometrico.jpa.Dependencia;
import ec.uce.edu.biometrico.jpa.Feriado;

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
	public List<Feriado> listarFeriados(boolean tipo, Integer id, Date fechaInicio, Date fechaFin) {
		List<Feriado> lstF = new ArrayList<>();
		try {
			if (tipo) {
				Query query = em.createQuery(
						"select f from Feriado as f join f.carrera as c where c.dependencia.dpnId=:id and f.frdFecha >= :fechaInicio and f.frdFecha <= :fechaFin order by f.frdFecha asc");
				query.setParameter("id", id);
				query.setParameter("fechaInicio", fechaInicio);
				query.setParameter("fechaFin", fechaFin);
				lstF = (List<Feriado>) query.getResultList();
			} else {
				Query query = em.createQuery(
						"select f from Feriado as f join f.carrera as c where c.crrId=:id and f.frdFecha >= :fechaInicio and f.frdFecha <= :fechaFin order by f.frdFecha asc");
				query.setParameter("id", id);
				query.setParameter("fechaInicio", fechaInicio);
				query.setParameter("fechaFin", fechaFin);
				lstF = (List<Feriado>) query.getResultList();
			}
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
			return lstF;
		}
		return lstF;
	}

	/**
	 * Metodo definido para guardar o actualizar un registro de feriado.
	 */
	@Override
	public void guardarActualizarFeriado(Feriado feriado) {
		try {
			if (feriado.getFrdId() != null) {
				em.merge(feriado);
			} else {
				feriado.setFrdId(obtenerSecuenciaFeriado() + 1);
				em.persist(feriado);
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
	public List<Dependencia> listarFacultades() {
		List<Dependencia> lstFacultades = null;
		try {
			Query query = em.createQuery("select d from Dependencia as d");
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
	private int obtenerSecuenciaFeriado() {
		int i = 0;
		try {
			Query query = em.createQuery("select f.frdId from Feriado as f  order by f.frdId desc");
			query.setMaxResults(1);
			i = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar la secuencia del feriado" + e);
			return i;
		}
		return i;
	}
}
