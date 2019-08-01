package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.MallaCurricularMateria;
import ec.uce.edu.biometrico.jpa.MallaCurricularParalelo;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Persona;
import ec.uce.edu.biometrico.jpa.Seguimiento;
import ec.uce.edu.biometrico.jpa.UnidadCurricular;

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
		// TODO Auto-generated constructor stubxxxxxxxxxxxxxx
	}

	@Override
	public List<Carrera> listarCarreras(Integer fdId) {
		List<Carrera> lstC = new ArrayList<>();
		try {
			Query query = em.createQuery("select cr.crrId from HorarioAcademico as ha join ha.mallaCurricularParalelo as mcpr join mcpr.paralelo as p join mcpr.mallaCurricularMateria as mcm join mcm.materia as ma join ma.carrera as cr join mcm.nivelByNvlId as nv  join ha.horaClaseAula as hca join hca.horaClase as hc join hca.aula as al  where mcpr.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.periodoAcademico as pa  where pa.pracEstado=0 and ch.detallePuesto.fichaDocente.fcdcId=:fdId group by mcp.mlcrprId) and hca.hoclalEstado=0 group by cr.crrId order by cr.crrId asc")
					.setParameter("fdId", fdId);
			for(Object obj: query.getResultList()){
				lstC.add(em.find(Carrera.class, obj));
			}
		} catch (Exception e) {
			System.out.println(e);
			return lstC;
		}

		return lstC;
	}

	@Override
	public List<FichaDocente> listarDocentes(Integer crrId) {
		List<FichaDocente> lstD = new ArrayList<>();
		try {
			Object[] arraObj;
			Query query = em
					.createQuery(
							"select fd,dp,p from DetallePuesto as dp join dp.fichaDocente as fd join fd.persona as p where dp.carrera.crrId=:crId order by p desc")
					.setParameter("crId", crrId);
			for (Object obj : query.getResultList()) {
				arraObj = (Object[]) obj;
				lstD.add((FichaDocente) arraObj[0]);
			}
		} catch (Exception e) {
			System.out.println(e);
			return lstD;
		}
		return lstD;
	}

	@Override
	public List<HorarioAcademico> listarHorariosxDocentexFechaHora(Integer integer, Integer[] arrayHora, int dia,Integer crrId) {
		List<HorarioAcademico> lstH = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em.createQuery(
					"select ha.hracId,cr.crrId,nv.nvlId, p.prlId, ma.mtrId,ha.hracDia,hc.hoclId, al.alaId, mcp.mlcrprId, mcm.mlcrmtId from HorarioAcademico as ha join ha.mallaCurricularParalelo as mcp join mcp.paralelo as p join mcp.mallaCurricularMateria as mcm join mcm.materia as ma join ma.carrera as cr join mcm.nivelByNvlId as nv  join ha.horaClaseAula as hca join hca.horaClase as hc join hca.aula as al where mcp.mlcrprId in ( select mcp.mlcrprId from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.periodoAcademico as pa  where pa.pracEstado=0 and ch.detallePuesto.fichaDocente.fcdcId=:fdId group by mcp.mlcrprId) and hca.hoclalEstado=0 and ha.hracDia=:dia and hc.hoclHoraFin between :Hinicio and :Hfin order by cr.crrId,nv.nvlId, p.prlId, ha.hracDia, ha.hracDescripcion asc");
			query.setParameter("fdId", integer).setParameter("dia", dia).setParameter("Hinicio", arrayHora[0])
					.setParameter("Hfin", arrayHora[2]);
//					.setParameter("crrId", crrId);and cr.crrId=:crrId
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				HorarioAcademico ha = em.find(HorarioAcademico.class, arrayObj[0]);
				MallaCurricularParalelo mcp = em.find(MallaCurricularParalelo.class, arrayObj[8]);
				MallaCurricularMateria mcm = em.find(MallaCurricularMateria.class, arrayObj[9]);
				Materia ma = em.find(Materia.class, arrayObj[4]);
				ha.setMallaCurricularParalelo(mcp);
				ha.getMallaCurricularParalelo().setMallaCurricularMateria(mcm);
				ha.getMallaCurricularParalelo().getMallaCurricularMateria().setMateria(ma);
				lstH.add(ha);
			}
		} catch (Exception e) {
			System.out.println("Error al listar horarios del Docente por dia -hora");
			return lstH;
		}
		return lstH;
	}

	@Override
	public List<FichaDocente> listarDocentesxCarrera(Integer crrId) {
		List<FichaDocente> lstD = new ArrayList<>();
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery(
					"select fd.fcdcId,p.prsId,p.prsPrimerApellido,p.prsSegundoApellido,p.prsNombres from DetallePuesto as dp join dp.fichaDocente as fd join fd.persona as p join dp.carrera as c  where c.crrId=:crrId group by fd.fcdcId,p.prsId,p.prsPrimerApellido,p.prsSegundoApellido,p.prsNombres order by p.prsPrimerApellido, p.prsSegundoApellido");
			query.setParameter("crrId", crrId);
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				FichaDocente fd = new FichaDocente((Integer) objArray[0]);
				fd.setPersona(em.find(Persona.class, (Integer) objArray[1]));
				lstD.add(fd);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar docentes por facultad");
			return lstD;
		}
		return lstD;
	}

	@Override
	public List<Carrera> listarCarrerasxFacultad(Integer dpnId) {
		List<Carrera> lstC = new ArrayList<>();
		try {
			Query query = em.createQuery("select cr.crrId from Carrera as cr where cr.dependencia.dpnId=:dpnId order by cr.crrId desc")
					.setParameter("dpnId", dpnId);
			for(Object obj: query.getResultList()){
				lstC.add(em.find(Carrera.class, obj));
			}
		} catch (Exception e) {
			System.out.println(e);
			return lstC;
		}

		return lstC;
	}

	@Override
	public List<Seguimiento> listarSeguimientosxDocente(Integer fcdcId) {
		List<Seguimiento> lstS = new ArrayList<>();
		try {
			Object[] objArray;
			Query query = em.createQuery(
					"select sg,mcp,mcm, m, ass, cnt, uc from Seguimiento as sg join sg.mallaCurricularParalelo as mcp join mcp.mallaCurricularMateria as mcm join mcm.materia as m join sg.asistencia as ass join sg.contenidoCurricular as cnt join cnt.unidadCurricular as uc where sg.asistencia.fichaDocente.fcdcId=:fcdcId order by sg.sgmId asc ");
			query.setParameter("fcdcId", fcdcId);
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				lstS.add((Seguimiento) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar seguimiento por docente y materia");
			return lstS;
		}
		return lstS;
	}

}