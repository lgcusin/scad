package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvHorarioLocal;
import ec.uce.edu.biometrico.jpa.Aula;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.DiaSemana;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Paralelo;
import ec.uce.edu.biometrico.jpa.Nivel;
import ec.uce.edu.biometrico.jpa.TipoHorario;

/**
 * Session Bean implementation class SrvHorario
 */
@Stateless
@LocalBean
public class SrvHorario implements SrvHorarioLocal {

	@PersistenceContext
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvHorario() {
	}

	@Override
	public List<Carrera> listarAllCrr(Integer fcId) {
		List<Carrera> lstC = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em
					.createQuery(
							"select cr.crrId from DetallePuesto as dp join dp.carrera as cr where dp.fichaDocente.fcdcId=:fcId group by cr.crrId")
					.setParameter("fcId", fcId);
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstC.add(em.find(Carrera.class, arrayObj[0]));
			}
		} catch (Exception e) {
			System.out.println("Error al obtener Carreras por Docente");
		}
		return lstC;
	}

	@Override
	public List<Materia> listarAllMat() {
		List<Materia> lstM = em.createNamedQuery("Materia.findAll", Materia.class).getResultList();
		return lstM;
	}

	@Override
	public List<Nivel> listarAllSem() {
		List<Nivel> lstN = new ArrayList<>();
		try {
			Query query = em.createQuery("select nivel from Nivel as nivel");
			lstN = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar semestres");
			return lstN;
		}
		return lstN;
	}

	@Override
	public List<Materia> listarMatByCrr(Integer id) {
		List<Materia> lstM = em.createNamedQuery("Materia.findAllById", Materia.class).setParameter("idcr", id)
				.getResultList();
		return lstM;
	}

	@Override
	public List<Materia> listarMatBySem(Integer idSemestre, Integer idCarrera) {
		List<Materia> lstM = null;
		try {
			Query query = em.createQuery("select m from MallaCurricularMateria as mcm"
					+ " join mcm.materia as m join mcm.semestre as s where s.smsId=:smsId and m.carrera.crrId=:crrId");
			query.setParameter("smsId", idSemestre);
			query.setParameter("crrId", idCarrera);
			lstM = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar materias por semestre: " + e);
		}
		return lstM;
	}

	@Override
	public List<Paralelo> listarParalelosHorario(Integer idSemestre, Integer idMateria, Integer idCarrera) {
		List<Paralelo> lstPar = null;
		try {
			Query query = em.createQuery("select p from Paralelo p "
					+ "where p.prlEstado=1 and p.carrera.crrId=:crrId and p.materia.mtrId=:mtrId and p.semestre.smsId=:smsId "
					+ "order by p.carrera.crrId,p.semestre.smsId");
			query.setParameter("smsId", idSemestre);
			query.setParameter("crrId", idCarrera);
			query.setParameter("mtrId", idMateria);
			lstPar = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar paralelos por semestre, materia y carrera: " + e);
		}
		return lstPar;
	}

	@Override
	public List<DiaSemana> listarAllDias() {
		List<DiaSemana> lstDia = null;
		try {
			Query query = em.createQuery("select diasemana from DiaSemana as diasemana");
			lstDia = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar dias de la semana: " + e);
		}
		return lstDia;
	}

	@Override
	public List<TipoHorario> listarTipoHorario() {
		List<TipoHorario> lstTipoHorario = null;
		try {
			Query query = em.createQuery("select tipohorario from TipoHorario as tipohorario");
			lstTipoHorario = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar tipos de horario" + e);
		}
		return lstTipoHorario;
	}

	@Override
	public List<FichaDocente> listarDocentes() {
		List<FichaDocente> lstDocente = null;
		try {
			Query query = em.createQuery("select fichadocente from FichaDocente as fichadocente");
			lstDocente = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar los docentes" + e);
		}
		return lstDocente;
	}

	@Override
	public List<Aula> listarAula() {
		List<Aula> lstAula = null;
		try {
			Query query = em.createQuery("select aula from Aula as aula");
			lstAula = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar los aulas" + e);
		}
		return lstAula;
	}

	/*
	 * @Override public void guardarHorario(Horario horario) { try { if
	 * (horario.getHrrId() != null) { em.merge(horario); } else { int h =
	 * obtenerSecuenciaHorario(); horario.setHrrId(h + 1); em.persist(horario);
	 * } } catch (Exception e) { System.out.println("Error al guardar horario" +
	 * e); } }
	 */

	private int obtenerSecuenciaHorario() {
		int h = 0;
		try {
			Query query = em.createQuery("select h.hrrId from Horario as h  order by h.hrrId desc");
			query.setMaxResults(1);
			h = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los horarios secuencia" + e);
			return h;
		}
		return h;
	}

	/*
	 * @Override public List<Horario> listarHorarios(Integer idParalelo, Integer
	 * idMateria) { List<Horario> lstHorario = new ArrayList<>(); try { Query
	 * query = em.
	 * createQuery("select h,th,m,fd,a,ds,p from Horario as h join h.tipoHorario as th"
	 * +
	 * " join h.materia as m join h.fichaDocente as fd join h.aula as a join h.diaSemana as ds"
	 * +
	 * " join h.paralelo as p where h.materia.mtrId=:mtrId and h.paralelo.prlId=:prlId order by h.hrrId asc"
	 * ); query.setParameter("prlId", idParalelo); query.setParameter("mtrId",
	 * idMateria); System.out.println("Valores de la lista"); for (Object obj :
	 * query.getResultList()) { Object[] objArray = (Object[]) obj;
	 * lstHorario.add((Horario) objArray[0]); } } catch (Exception e) {
	 * System.out.println("Error al consultar los horarios" + e); } return
	 * lstHorario; }
	 */

	/*
	 * @Override public List<Paralelo> listarParalelosHorario(Integer mtrId) {
	 * List<Paralelo> lstP; try { lstP =
	 * em.createNamedQuery("Paralelo.findAllByMtrId",
	 * Paralelo.class).setParameter("mtId", mtrId) .getResultList(); } catch
	 * (Exception e) { System.out.println("No se encontraron paralelos" + e);
	 * return lstP = new ArrayList<>(); } return lstP; }
	 */

	/*
	 * @Override public List<Horario> listarHorarios(String prcdId) {
	 * List<Horario> lstH = new ArrayList<>(); try { // lstH =
	 * em.createNamedQuery("Horario.findAllByPrlId", //
	 * Horario.class).setParameter("prlId", prcdId).getResultList(); Query query
	 * = em.
	 * createQuery("select  h,th,m,fd,a,ds from Horario as h join h.tipoHorario as th "
	 * +
	 * " join h.materia as m join h.fichaDocente as fd join h.aula as a join h.diaSemana as ds "
	 * +
	 * "where m.mtrId in(select mcp.mallaCurricularMateria.materia.mtrId from MallaCurricularParalelo as mcp where mcp.paralelo.prlCodigo=:prlCd) "
	 * + "order by ds,h asc"); query.setParameter("prlCd", prcdId);
	 * System.out.println("Valores de la lista"); for (Object obj :
	 * query.getResultList()) { Object[] objArray = (Object[]) obj;
	 * lstH.add((Horario) objArray[0]); } } catch (Exception e) {
	 * System.out.println("No se enontranos horarios " + e); return lstH; }
	 * return lstH; }
	 */

	/*
	 * @Override public void eliminarHorario(Horario horario) { try { Horario h
	 * = em.find(Horario.class, horario.getHrrId()); if (h != null) {
	 * em.remove(h); } } catch (Exception e) {
	 * System.out.println("Error al eliminar horario" + e); } }
	 */
}
