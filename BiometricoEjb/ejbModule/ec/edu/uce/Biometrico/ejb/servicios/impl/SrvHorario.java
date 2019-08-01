package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvHorarioLocal;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.Aula;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.HorarioAcademicoExamen;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Nivel;
import ec.uce.edu.biometrico.jpa.Paralelo;
import ec.uce.edu.biometrico.jpa.PeriodoAcademico;

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
	public List<Nivel> listarSemestrexCarrera(Integer fdId, Integer crrId) {
		List<Nivel> lstN = new ArrayList<>();
		try {
			Query query = em.createQuery(
					"select nv.nvlId from HorarioAcademico as ha join ha.mallaCurricularParalelo as mcpr join mcpr.paralelo as p join mcpr.mallaCurricularMateria as mcm join mcm.materia as ma join ma.carrera as cr join mcm.nivelByNvlId as nv  join ha.horaClaseAula as hca join hca.horaClase as hc join hca.aula as al  where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.periodoAcademico as pa  where pa.pracEstado=0 and ch.detallePuesto.fichaDocente.fcdcId=:fdId group by mcp.mlcrprId) and hca.hoclalEstado=0 and cr.crrId=:crId group by nv.nvlId order by nv.nvlId asc");
			query.setParameter("crId", crrId).setParameter("fdId", fdId);
			for (Object obj : query.getResultList()) {
				lstN.add(em.find(Nivel.class, obj));
			}
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
			Query query = em.createQuery(
					"select ma.mtrId from HorarioAcademico as ha join ha.mallaCurricularParalelo as mcpr join mcpr.paralelo as p join mcpr.mallaCurricularMateria as mcm join mcm.materia as ma join ma.carrera as cr join mcm.nivelByNvlId as nv  join ha.horaClaseAula as hca join hca.horaClase as hc join hca.aula as al  where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.periodoAcademico as pa  where pa.pracEstado=0 and ch.detallePuesto.fichaDocente.fcdcId=:fdId group by mcp.mlcrprId) and hca.hoclalEstado=0 and cr.crrId=:crrId and nv.nvlId=:smsId group by ma.mtrId order by ma.mtrId asc");
			query.setParameter("smsId", idSemestre);
			query.setParameter("crrId", idCarrera);
			query.setParameter("fdId", fdId);
			for (Object obj : query.getResultList()) {
				lstM.add(em.find(Materia.class, obj));
			}
		} catch (Exception e) {
			System.out.println("Error al consultar materias por semestre: " + e);
			return lstM;
		}
		return lstM;
	}

	@Override
	public List<Paralelo> listarParalelosHorario(Integer fdId, Integer idSemestre, Integer idMateria,
			Integer idCarrera) {
		List<Paralelo> lstPar = new ArrayList<>();
		try {
			Query query = em.createQuery(
					"select p.prlId from HorarioAcademico as ha join ha.mallaCurricularParalelo as mcpr join mcpr.paralelo as p join mcpr.mallaCurricularMateria as mcm join mcm.materia as ma join ma.carrera as cr join mcm.nivelByNvlId as nv  join ha.horaClaseAula as hca join hca.horaClase as hc join hca.aula as al  where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.periodoAcademico as pa  where pa.pracEstado=0 and ch.detallePuesto.fichaDocente.fcdcId=:fdId group by mcp.mlcrprId) and hca.hoclalEstado=0 and cr.crrId=:crrId and nv.nvlId=:smsId and ma.mtrId=:mtrId group by p.prlId order by p.prlId asc");
			query.setParameter("smsId", idSemestre);
			query.setParameter("crrId", idCarrera);
			query.setParameter("mtrId", idMateria);
			query.setParameter("fdId", fdId);
			for (Object obj : query.getResultList()) {
				lstPar.add(em.find(Paralelo.class, obj));
			}
		} catch (Exception e) {
			System.out.println("Error al consultar paralelos por semestre, materia y carrera: " + e);
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

	@Override
	public void guardarHorarioExamen(HorarioAcademicoExamen horario) {
		try {
			if (horario.getHracexId() != null) {
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
			Query query = em
					.createQuery("select h.hracexId from HorarioAcademicoExamen as h  order by h.hracexId desc");
			query.setMaxResults(1);
			h = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los horarios secuencia" + e);
			return h;
		}
		return h;
	}

	@Override
	public List<HorarioAcademico> listarHorarios(Integer fdId, Integer idParalelo, Integer idMateria) {
		List<HorarioAcademico> lstHorario = new ArrayList<>();
		try {
			Query query = em.createQuery(
					"select ha.hracId from HorarioAcademico as ha join ha.mallaCurricularParalelo as mcpr join mcpr.paralelo as p join mcpr.mallaCurricularMateria as mcm join mcm.materia as ma join ma.carrera as cr join mcm.nivelByNvlId as nv  join ha.horaClaseAula as hca join hca.horaClase as hc join hca.aula as al  where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.periodoAcademico as pa  where pa.pracEstado=0 and ch.detallePuesto.fichaDocente.fcdcId=:fdId group by mcp.mlcrprId) and hca.hoclalEstado=0 and ma.mtrId=:mtrId and p.prlId=:prlId group by ha.hracId order by ha.hracId asc");
			query.setParameter("prlId", idParalelo);
			query.setParameter("mtrId", idMateria);
			query.setParameter("fdId", fdId);
			System.out.println("Valores de la lista");
			for (Object obj : query.getResultList()) {
				lstHorario.add(em.find(HorarioAcademico.class, obj));
			}
			// List<HorarioAcademico> lstAux = lstHorario;
			// for (HorarioAcademico h : lstHorario) {
			// List<HorarioAcademico> auxHorario = new ArrayList<>();
			// for (HorarioAcademico h2 : lstAux) {
			// if (h.getHracDia().equals(h2.getHracDia()) &&
			// h.getMallaCurricularParalelo().getMlcrprId()
			// .equals(h2.getMallaCurricularParalelo().getMlcrprId())) {
			// auxHorario.add(h2);
			// }
			// }
			// for (int i = 1; i<auxHorario.size(); i++) {
			// lstAux.remove(auxHorario.get(i));
			// }
			// }
			// lstHorario = lstAux;
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

	@Override
	public void actualizarGuardarAsistencia(Asistencia asis) {
		try {
			if (asis.getAssId() != null) {
				em.merge(asis);
			} else {
				asis.setAssId(0);
				em.persist(asis);
			}
		} catch (Exception e) {
			System.out.println("No se puede guardar asistencia");
		}

	}

	@Override
	public List<Asistencia> listarAsistenciasByHorario(Integer fcdcId, List<Integer> idHorarios, Date fechaProceso) {
		List<Asistencia> lstAsistencia = new ArrayList<>();
		try {
			Query query = em.createQuery("select asdc, hae from Asistencia as asdc "
					+ "left join asdc.horarioAcademicoExamen as hae where asdc.fichaDocente.fcdcId=:fcdcId "
					+ "and asdc.assFecha >= :fechaProceso and asdc.horarioAcademico.hracId in (:idHorarios)");
			query.setParameter("idHorarios", idHorarios);
			query.setParameter("fechaProceso", fechaProceso);
			query.setParameter("fcdcId", fcdcId);
			Object[] objArray;
			HashMap<String, Object> map;
			for (Object object : query.getResultList()) {
				objArray = (Object[]) object;
				Asistencia ad = new Asistencia();
				HorarioAcademicoExamen hae = new HorarioAcademicoExamen();
				ad = (Asistencia) objArray[0];
				hae = (HorarioAcademicoExamen) objArray[1];
				ad.setHorarioAcademicoExamen(hae);
				lstAsistencia.add(ad);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar los asistencias por horarios" + e);
		}
		return lstAsistencia;
	}

	@Override
	public String obtenerHoraClasexHorario(HorarioAcademico h, int i) {
		String hora = "";
		List<HorarioAcademico> lstH = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em.createQuery(
					"select ha, hca, hc from HorarioAcademico as ha join ha.horaClaseAula as hca join hca.horaClase as hc where ha.mallaCurricularParalelo.mlcrprId=:mcpId and ha.hracDia=:dia order by ha.hracDescripcion");
			query.setParameter("mcpId", h.getMallaCurricularParalelo().getMlcrprId()).setParameter("dia",
					h.getHracDia());
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstH.add((HorarioAcademico) arrayObj[0]);
			}
			switch (i) {
			case 1:
				if (lstH.get(0).getHoraClaseAula().getHoraClase().getHoclHoraInicio() < 10) {
					hora = "0" + lstH.get(0).getHoraClaseAula().getHoraClase().getHoclHoraInicio() + ":00";
				} else {
					hora = lstH.get(0).getHoraClaseAula().getHoraClase().getHoclHoraInicio() + ":00";
				}
				break;
			case 2:
				if (lstH.get(lstH.size() - 1).getHoraClaseAula().getHoraClase().getHoclHoraFin() < 10) {
					hora = "0" +lstH.get(lstH.size() - 1).getHoraClaseAula().getHoraClase().getHoclHoraFin() + ":00";
				} else {
					hora = lstH.get(lstH.size() - 1).getHoraClaseAula().getHoraClase().getHoclHoraFin() + ":00";
				}

				break;
			}
		} catch (Exception e) {
			System.out.println("Error al obtener hora clase");
			return hora;
		}
		return hora;
	}

	@Override
	public List<PeriodoAcademico> listarPeriodos(String string) {
		String param= "%HEMISEMESTRE"+string;
		List<PeriodoAcademico> lstP= new ArrayList<>();
		try{
			Object[] arrayObj;
			Query query= em.createQuery("select pa from PlanificacionCronograma as pc join pc.cronogramaProcesoFlujo as cpf join cpf.procesoFlujo as pf join cpf.cronograma as crn join crn.periodoAcademico as pa where pc.plcrEstado=0 and  pf.prflEstado=0 and pf.prflDescripcion like :param and crn.crnEstado=0 and crn.crnTipo=0 and  pa.pracEstado=0 order by pa.pracFechaIncio asc");
			query.setParameter("param", param);
			for(Object obj: query.getResultList()){
			arrayObj= (Object[]) obj;
				lstP.add((PeriodoAcademico) arrayObj[0]);
			}
		}catch (Exception e) {
			// TODO: handle exception
			return lstP;
		}
		return lstP;
	}

}