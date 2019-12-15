package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvHorarioLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.biometrico.jpa.*;



/**
 * Session Bean implementation class SrvHorario
 */
@Stateless
@Local
public class SrvHorario implements SrvHorarioLocal {

	@PersistenceContext
	private EntityManager em;

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
			Query query = em.createQuery("select cr.crrId from DetallePuesto as dp join dp.dtpsCarrera as cr "
					+ "where dp.dtpsFichaDocente.fcdcId=:fcId group by cr.crrId").setParameter("fcId", fcId);
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

	/**
	 * Lista los semestres de la carrera al que pertenece el docente
	 * 
	 * @param fdId
	 *            id del docente
	 * @param crrId
	 *            id de la carrera
	 * @return semestres
	 */
	@Override
	public List<Nivel> listarSemestrexCarrera(Integer fdId, Integer crrId) {
		List<Nivel> lstN = new ArrayList<Nivel>();
		try {
			Query query = em.createQuery("select distinct nv from HorarioAcademico as ha "
					+ "join ha.hracMallaCurricularParalelo as mcpr "
					+ "join mcpr.mlcrprMallaCurricularMateria as mcm " 
					+ "join mcm.mlcrmtMateria as ma "
					+ "join ma.mtrCarrera as cr "
					+ "join mcm.mlcrmtNivel as nv " 
					+ "join ha.hracHoraClaseAula as hca "
					+ "where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch "
					+ "inner join ch.crhrMallaCurricularParalelo as mcp " 
					+ "join ch.crhrPeriodoAcademico as pa "
					+ "where pa.pracEstado=0 and ch.crhrDetallePuesto.dtpsFichaDocente.fcdcId=:fdId "
					+ "group by mcp.mlcrprId) and hca.hoclalEstado=0 and cr.crrId=:crId order by nv.nvlId asc");
			query.setParameter("crId", crrId).setParameter("fdId", fdId);
			lstN = query.getResultList();
			// for (Object obj : query.getResultList()) {
			// lstN.add(em.find(Nivel.class, obj));
			// }
		} catch (Exception e) {
			System.out.println("Error al consultar semestres x carrera");
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
	public List<Materia> listarMatBySem(Integer fdId, Integer idSemestre, Integer idCarrera) {
		List<Materia> lstM = new ArrayList<>();
		try {
			Query query = em.createQuery("select distinct ma from HorarioAcademico as ha "
					+ "join ha.hracMallaCurricularParalelo as mcpr "
					+ "join mcpr.mlcrprMallaCurricularMateria as mcm "
					+ "join mcm.mlcrmtMateria as ma "
					+ "join ma.mtrCarrera as cr "
					+ "join mcm.mlcrmtNivel as nv "
					+ "join ha.hracHoraClaseAula as hca "
					+ "where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch "
					+ "inner join ch.crhrMallaCurricularParalelo as mcp "
					+ "join ch.crhrPeriodoAcademico as pa "
					+ "where pa.pracEstado=0 and ch.crhrDetallePuesto.dtpsFichaDocente.fcdcId=:fdId "
					+ "group by mcp.mlcrprId) and hca.hoclalEstado=0 and cr.crrId=:crrId and nv.nvlId=:smsId "
					+ "order by ma.mtrId asc");
			query.setParameter("smsId", idSemestre);
			query.setParameter("crrId", idCarrera);
			query.setParameter("fdId", fdId);
			lstM=query.getResultList();
			// for (Object obj : query.getResultList()) {
			// lstM.add(em.find(Materia.class, obj));
			// }
		} catch (Exception e) {
			System.out.println("Error al consultar materias por semestre: " + e);
			return lstM;
		}
		return lstM;
	}

	/**
	 * Obtiene los paralelos del semestres en que se encuentra la materia que
	 * dicta el docente
	 * 
	 * @param fdId
	 *            id del docente
	 * @param idSemestre
	 *            id del semestre
	 * @param idMateria
	 *            id de la materia
	 * @param idCarrera
	 *            id de la carrera
	 * @return
	 */
	@Override
	public List<Paralelo> listarParalelosHorario(Integer fdId, Integer idSemestre, Integer idMateria,
			Integer idCarrera) {
		List<Paralelo> lstPar = new ArrayList<>();
		try {
			Query query = em.createQuery("select distinct p from HorarioAcademico as ha "
					+ "join ha.hracMallaCurricularParalelo as mcpr "
					+ "join mcpr.mlcrprParalelo as p "
					+ "join mcpr.mlcrprMallaCurricularMateria as mcm "
					+ "join mcm.mlcrmtMateria as ma "
					+ "join ma.mtrCarrera as cr "
					+ "join mcm.mlcrmtNivel as nv "
					+ "join ha.hracHoraClaseAula as hca "
					+ "where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch "
					+ "inner join ch.crhrMallaCurricularParalelo as mcp "
					+ "join ch.crhrPeriodoAcademico as pa "
					+ "where pa.pracEstado=0 and ch.crhrDetallePuesto.dtpsFichaDocente.fcdcId=:fdId "
					+ "group by mcp.mlcrprId) and hca.hoclalEstado=0 and cr.crrId=:crrId and nv.nvlId=:smsId and ma.mtrId=:mtrId "
					+ "order by p.prlId asc");
			query.setParameter("smsId", idSemestre);
			query.setParameter("crrId", idCarrera);
			query.setParameter("mtrId", idMateria);
			query.setParameter("fdId", fdId);
			lstPar = query.getResultList();
			// for (Object obj : query.getResultList()) {
			// lstPar.add(em.find(Paralelo.class, obj));
			// }
		} catch (Exception e) {
			System.out.println("Error al consultar paralelos por semestre, materia y carrera: " + e);
			return lstPar;
		}
		return lstPar;
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

	/**
	 * Guarda los horarios de examen que dicta el docente
	 * 
	 * @param hor
	 *            Horario a guardar
	 */
	@Override
	public void guardarHorarioExamen(HorarioAcademicoExamen horario) {
		try {
			if (horario != null) {
				em.merge(horario);
			} else {
				int h = obtenerSecuenciaHorario();
				horario.setHracexId(h + 1);
				em.persist(horario);
			}
		} catch (Exception e) {
			System.out.println("Error al guardar horario" + e);
		}
	}

	private int obtenerSecuenciaHorario() {
		int h = 0;
		try {
			Query query = em.createQuery("select h.hracexId from HorarioAcademicoExamen as h order by h.hracexId desc");
			query.setMaxResults(1);
			h = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los horarios secuencia" + e);
			return h;
		}
		return h;
	}

	/**
	 * obtiene el horario academico de la materia en el paralelo,semestre y
	 * carrera a la que pertenece y dicta el docente
	 * 
	 * @param integer
	 *            id del docente
	 * @param prlId
	 *            id del paralelo
	 * @param mtrId
	 *            id de la materia
	 * @return horarios academicos
	 */
	@Override
	public List<HorarioAcademico> listarHorarios(Integer fdId, Integer idParalelo, Integer idMateria) {
		List<HorarioAcademico> lstHorario = null;
		try {
			Query query = em.createQuery(
					"select distinct ha from HorarioAcademico as ha " 
							+ "join ha.hracMallaCurricularParalelo as mcpr "
							+ "join mcpr.mlcrprParalelo as p "
							+ "join mcpr.mlcrprMallaCurricularMateria as mcm "
							+ "join mcm.mlcrmtMateria as ma "
							+ "join ma.mtrCarrera as cr "
							+ "join mcm.mlcrmtNivel as nv "
							+ "join ha.hracHoraClaseAula as hca "
							+ "join hca.hoclalHoraClase as hc "
							+ "join hca.hoclalAula as al "
							+ "where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch "
							+ "inner join ch.crhrMallaCurricularParalelo as mcp "
							+ "join ch.crhrPeriodoAcademico as pa where pa.pracEstado=0 "
							+ "and ch.crhrDetallePuesto.dtpsFichaDocente.fcdcId=:fdId group by mcp.mlcrprId) and hca.hoclalEstado=0 "
							+ "and ma.mtrId=:mtrId and p.prlId=:prlId order by ha.hracId asc");
			query.setParameter("prlId", idParalelo);
			query.setParameter("mtrId", idMateria);
			query.setParameter("fdId", fdId);
			lstHorario = query.getResultList();
			// for (Object obj : query.getResultList()) {
			// lstHorario.add(em.find(HorarioAcademico.class, obj));
			// }
		} catch (Exception e) {
			System.out.println("Error al consultar los horarios" + e);
			return lstHorario;
		}
		return lstHorario;
	}

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

	/**
	 * Guarda las asistencias de la semana de examenes
	 * 
	 * @param asis
	 *            asistencia para guardar
	 */
	@Override
	public void actualizarGuardarAsistencia(Asistencia asis) {
		try {
			if (asis != null) {
				em.merge(asis);
			} else {
				//asis.setAssId(0);
				em.persist(asis);
			}
		} catch (Exception e) {
			System.out.println("No se puede guardar asistencia");
		}

	}

	/**
	 * Comprueba si exiten asistencias de los horarios academicos
	 * 
	 * @param fcdcId
	 *            id del docente
	 * @param idHorarios
	 *            key id de hoarios obtenidos de la materia
	 * @param date
	 *            fecha actual
	 * @return asistencias
	 */
	@Override
	public Asistencia listarAsistenciasByHorario(Integer fcdcId, List<Integer> idHorarios, Date fechaProceso) {
		Asistencia Asistencia = null;
		try {
			Object[] objArray;
			Query query = em.createQuery("select asdc,ha, hae from Asistencia as asdc "
					+ "left join asdc.assHorarioAcademicoExamen as hae join asdc.assHorarioAcademico as ha where asdc.assFichaDocente.fcdcId=:fcdcId "
					+ "and asdc.assFecha = :fechaProceso and ha.hracId in (:idHorarios)");
			query.setParameter("idHorarios", idHorarios);
			query.setParameter("fechaProceso", fechaProceso);
			query.setParameter("fcdcId", fcdcId);
			for (Object object : query.getResultList()) {
				objArray = (Object[]) object;
				Asistencia = (Asistencia) objArray[0];
				Asistencia.setAssHorarioAcademico((HorarioAcademico) objArray[1]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar los asistencias por horarios" + e);
			return Asistencia;
		}
		return Asistencia;
	}

	/**
	 * Obtiene la hora del horario academico y lo tranforma a un formato
	 * estandar
	 * 
	 * @param h
	 *            horario academico
	 * @param i
	 *            indice del Horario
	 * @return hora transformada
	 */
	@Override
	public String obtenerHoraClasexHorario(HorarioAcademico h, int i) {
		String hora = null;
		List<HorarioAcademico> lstH = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em.createQuery("select ha, hca, hc from HorarioAcademico as ha "
					+ "join ha.hracHoraClaseAula as hca " 
					+ "join hca.hoclalHoraClase as hc "
					+ "where ha.hracMallaCurricularParalelo.mlcrprId=:mcpId and ha.hracDia=:dia "
					+ "order by hc.hoclDescripcion asc");
			query.setParameter("mcpId", h.getHracMallaCurricularParalelo().getMlcrprId()).setParameter("dia",
					h.getHracDia());
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstH.add((HorarioAcademico) arrayObj[0]);
			}
			switch (i) {
			case 1:
				if (lstH.get(0).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio() < 10) {
					hora = "0" + lstH.get(0).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio() + ":00";
				} else {
					hora = lstH.get(0).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio() + ":00";
				}
				break;
			case 2:
				if (lstH.get(lstH.size() - 1).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraFin() < 10) {
					hora = "0" + lstH.get(lstH.size() - 1).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraFin()
							+ ":00";
				} else {
					hora = lstH.get(lstH.size() - 1).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraFin()
							+ ":00";
				}

				break;
			}
		} catch (Exception e) {
			System.out.println("Error al obtener hora clase");
			return hora;
		}
		return hora;
	}

	/**
	 * Obtiene fechas de periodo acdemico al que pertenece horaios de materias
	 * del docente para examenes
	 * 
	 * @param string
	 *            Fecha actualizada
	 * @return periodos acdemicos
	 */
	@Override
	public List<PeriodoAcademico> listarPeriodos(String string) {
		List<PeriodoAcademico> lstP = null;
		String param = "%HEMISEMESTRE" + string;
		try {
			Object[] arrayObj;
			Query query = em.createQuery(
					"select pa from PlanificacionCronograma as pc join pc.plcrCronogramaProcesoFlujo as cpf "
							+ "join cpf.crprProcesoFlujo as pf join cpf.crprCronograma as crn join crn.crnPeriodoAcademico as pa "
							+ "where pc.plcrEstado=0 and pf.prflEstado=0 and pf.prflDescripcion like :param and crn.crnEstado=0 "
							+ "and crn.crnTipo=0 and  pa.pracEstado=0 order by pa.pracFechaIncio asc");
			query.setParameter("param", param);
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstP.add((PeriodoAcademico) arrayObj[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al obtener el periodo acdemico" + e);
			return lstP;
		}
		return lstP;
	}

	/**
	 * Obtiene fechas de periodo acdemico al que pertenece horaios de materias
	 * del docente para examenes
	 * 
	 * @param string
	 *            Fecha actualizada
	 * @return periodos acdemicos
	 */
	@Override
	public List<PlanificacionCronograma> listarPlannificacion(String string) {
		List<PlanificacionCronograma> lstP = null;
		String param = "%HEMISEMESTRE" + string;
		try {
			// Object[] arrayObj;
			Query query = em.createQuery(
					"select pc from PlanificacionCronograma as pc join pc.plcrCronogramaProcesoFlujo as cpf join cpf.crprProcesoFlujo as pf join cpf.crprCronograma as crn join crn.crnPeriodoAcademico as pa where pc.plcrEstado=0 and  pf.prflEstado=0 and pf.prflDescripcion like :param and crn.crnEstado=0 and crn.crnTipo=0 and  pa.pracEstado=0 order by pc.plcrId asc");
			query.setParameter("param", param);
			lstP = query.getResultList();
			// for (Object obj : query.getResultList()) {
			// arrayObj = (Object[]) obj;
			// lstP.add((PeriodoAcademico) arrayObj[0]);
			// }
		} catch (Exception e) {
			System.out.println("Error al obtener el periodo acdemico" + e);
			return lstP;
		}
		return lstP;
	}

	/**
	 * Elimina el regitro modificado
	 * @param asis Asisencia a eliminar
	 */
	@Override
	public void removeAsistencia(Asistencia asis) {
		try {
			Asistencia a = em.find(Asistencia.class, asis.getAssId());
			if (a != null) {
				em.remove(a);
			}
		} catch (Exception e) {
			System.out.println("Error al eliminar asistencia");
		}
		
	}

	@Override
	public void removerHorarioExamen(HorarioAcademicoExamen hracex) {
		try {
			HorarioAcademicoExamen he = em.find(HorarioAcademicoExamen.class, hracex.getHracexId());
			if (he != null) {
				em.remove(he);
			}
		} catch (Exception e) {
			System.out.println("Error al eliminar horario de examen");
		}
		
	}


}
