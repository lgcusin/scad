package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.edu.uce.Biometrico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.biometrico.jpa.*;


/**
 * Session Bean implementation class SrvEmpleado
 */
@Stateless
@Local
public class SrvEmpleado implements SrvEmpleadoLocal {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvEmpleado() {
		// TODO Auto-generated constructor stubxxxxxxxxxxxxxx
	}

	/**
	 * Obtiene las carreras de una facultad
	 * 
	 * @param fdId
	 *            de la Facultad
	 * @return lista carreras
	 */
	@Override
	public List<Carrera> listarCarreras(Integer fdId) {
		List<Carrera> lstC = new ArrayList<Carrera>(0);
		try {
			Query query = em
					.createQuery("select distinct crr from HorarioAcademico as hrac "
							+ "join hrac.hracMallaCurricularParalelo as mlcrpr " + "join mlcrpr.mlcrprParalelo as prl "
							+ "join mlcrpr.mlcrprMallaCurricularMateria as mlcrmt "
							+ "join mlcrmt.mlcrmtMateria as mtr " + "join mtr.mtrCarrera as crr "
							+ "join mlcrmt.mlcrmtNivel as nvl " + "join hrac.hracHoraClaseAula as hoclal "
							+ "where mlcrpr.mlcrprId in (select mlcrpr.mlcrprId from CargaHoraria as crhr "
							+ "inner join crhr.crhrMallaCurricularParalelo as mlcrpr "
							+ "join crhr.crhrPeriodoAcademico as prac "
							+ "where prac.pracEstado=0 and crhr.crhrDetallePuesto.dtpsFichaDocente.fcdcId=:fdId "
							+ "group by mlcrpr.mlcrprId) and hoclal.hoclalEstado=0 " + "order by crr.crrId asc")
					.setParameter("fdId", fdId);
			lstC = query.getResultList();
			// lstC = new ArrayList<Carrera>();
			// for (Object obj : query.getResultList()) {
			// int id = (int) obj;
			// lstC.add(em.find(Carrera.class, id));
			// }
		} catch (Exception e) {
			System.out.println(e);
			return lstC;
		}
		return lstC;
	}

	/**
	 * Lista de docentes por las careras
	 * 
	 * @param crrId
	 *            id de la carrera
	 * @return lista de docentes
	 */
	@Override
	public List<FichaDocente> listarDocentes(Integer crrId) {
		List<FichaDocente> lstD = null;
		try {
			Object[] arraObj;
			Query query = em
					.createQuery(
							"select fcdc,dtps,prs from DetallePuesto as dtps join dtps.dtpsFichaDocente as fcdc join fcdc.fcdcPersona as prs "
									+ "where dtps.dtpsCarrera.crrId=:crId order by prs.prsId desc")
					.setParameter("crId", crrId);
			lstD=new ArrayList<>();
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

	/**
	 * Obtiene horarios academicos de la fecha actual del Docente selecionado en
	 * el rango de hora
	 * 
	 * @param integer
	 *            id del Docente
	 * @param arrayHora
	 *            rango de hora
	 * @param dia
	 *            fecha actual
	 * @param crrId
	 *            id de la carrera
	 * @return Horarios
	 */
	@Override
	public List<HorarioAcademico> listarHorariosxDocentexFechaHora(Integer integer, Integer[] arrayHora, int dia,
			Integer crrId) {
		List<HorarioAcademico> lstH = null;
		try {
			Object[] arrayObj;
			Query query = em.createQuery(
					"select hrac.hracId,crr.crrId,nvl.nvlId, prl.prlId, mtr.mtrId,hrac.hracDia,hocl.hoclId, ala.alaId, mlcrpr.mlcrprId, mlcrmt.mlcrmtId "
							+ "from HorarioAcademico as hrac " + "join hrac.hracMallaCurricularParalelo as mlcrpr "
							+ "join mlcrpr.mlcrprParalelo as prl "
							+ "join mlcrpr.mlcrprMallaCurricularMateria as mlcrmt "
							+ "join mlcrmt.mlcrmtMateria as mtr join mtr.mtrCarrera as crr "
							+ "join mlcrmt.mlcrmtNivel as nvl " + "join hrac.hracHoraClaseAula as hoclal "
							+ "join hoclal.hoclalHoraClase as hocl " + "join hoclal.hoclalAula as ala "
							+ "where mlcrpr.mlcrprId in (select mcp.mlcrprId from CargaHoraria as ch "
							+ "inner join ch.crhrMallaCurricularParalelo as mcp "
							+ "join ch.crhrPeriodoAcademico as prac "
							+ "where prac.pracEstado=0 and ch.crhrDetallePuesto.dtpsFichaDocente.fcdcId=:fdId group by mcp.mlcrprId) "
							+ "and hoclal.hoclalEstado=0 and hrac.hracDia=:dia and hocl.hoclHoraFin between :Hinicio and :Hfin "
							+ "order by crr.crrId,nvl.nvlId, prl.prlId, hrac.hracDia, hrac.hracDescripcion asc");
			query.setParameter("fdId", integer).setParameter("dia", dia).setParameter("Hinicio", arrayHora[0])
					.setParameter("Hfin", arrayHora[1]);
			lstH = new ArrayList<>();
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				HorarioAcademico ha = em.find(HorarioAcademico.class, arrayObj[0]);
				MallaCurricularParalelo mcp = em.find(MallaCurricularParalelo.class, arrayObj[8]);
				MallaCurricularMateria mcm = em.find(MallaCurricularMateria.class, arrayObj[9]);
				Materia ma = em.find(Materia.class, arrayObj[4]);
				ha.setHracMallaCurricularParalelo(mcp);
				ha.getHracMallaCurricularParalelo().setMlcrprMallaCurricularMateria(mcm);
				ha.getHracMallaCurricularParalelo().getMlcrprMallaCurricularMateria().setMlcrmtMateria(ma);
				lstH.add(ha);
			}
		} catch (Exception e) {
			System.out.println("Error al listar horarios del Docente por dia -hora");
			return lstH;
		}
		return lstH;
	}

	/**
	 * Obtiene los docentes de la carrera a la que petenecen
	 * 
	 * @param crrId
	 *            id de carrera
	 * @return docentes
	 */
	@Override
	public List<FichaDocente> listarDocentesxCarrera(Integer crrId) {
		List<FichaDocente> lstD = null;
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery("select distinct fd, p from DetallePuesto as dp "
					+ "join dp.dtpsFichaDocente as fd join fd.fcdcPersona as p join dp.dtpsCarrera as c "
					+ "where c.crrId=:crrId " + "order by p.prsPrimerApellido, p.prsSegundoApellido");
			query.setParameter("crrId", crrId);
			lstD = new ArrayList<>();
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				FichaDocente fd = (FichaDocente) (objArray[0]);
				fd.setFcdcPersona((Persona) objArray[1]);
				lstD.add(fd);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar docentes por Carrera");
			return lstD;
		}
		return lstD;
	}

	/**
	 * Obtiene la carrera por facultad selecionada
	 * 
	 * @param dpnId
	 *            id de la facultad
	 * @return carreras
	 */
	@Override
	public List<Carrera> listarCarrerasxFacultad(Integer dpnId) {
		List<Carrera> lstC = null;
		try {
			Query query = em
					.createQuery(
							"select cr from Carrera as cr where cr.crrDependencia.dpnId=:dpnId order by cr.crrId desc")
					.setParameter("dpnId", dpnId);
			lstC = query.getResultList();
			// for (Object obj : query.getResultList()) {
			// lstC.add(em.find(Carrera.class, obj));
			// }
		} catch (Exception e) {
			System.out.println(e);
			return lstC;
		}
		return lstC;
	}

	/**
	 * Obtiene los contenidos del syllabo de la materia que dicta un docente
	 * 
	 * @param fcdcId
	 *            id del docente
	 * @return contenidos de clase
	 */
	@Override
	public List<Seguimiento> listarSeguimientosxDocente(Integer fcdcId) {
		List<Seguimiento> lstS = null;
		try {
			Object[] objArray;
			Query query = em.createQuery(
					"select sg, mcp, mcm, m, ass, cnt, uc from Seguimiento as sg join sg.sgmMallaCurricularParalelo as mcp "
							+ "join mcp.mlcrprMallaCurricularMateria as mcm join mcm.mlcrmtMateria as m join sg.asistencia as ass "
							+ "join sg.sgmContenidoCurricular as cnt join cnt.unidadCurricular as uc "
							+ "where sg.asistencia.assFichaDocente.fcdcId=:fcdcId order by sg.sgmId asc ");
			query.setParameter("fcdcId", fcdcId);
			lstS = new ArrayList<>();
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				lstS.add((Seguimiento) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar seguimiento por docente");
			return lstS;
		}
		return lstS;
	}

	/**
	 * Obtiene la Facultad a la que pertenece
	 * 
	 * @param usrId
	 *            id del Usuario
	 * @return Facultad
	 */
	@Override
	public Dependencia buscarOrigenCarrera(int usrId) {
		List<Dependencia> lista = null;
		Dependencia retorno = new Dependencia();
		try {
			Query query = em.createQuery(
					" select dpn from RolFlujoCarrera as rfc join rfc.roflcrCarrera as crr join crr.crrDependencia as dpn "
							+ "where rfc.roflcrUsuarioRol.usroUsuario.usrId=:usrId");
			query.setParameter("usrId", usrId);
			lista = query.getResultList();
			if (!lista.isEmpty()) {
				retorno = lista.get(0);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar facultad");
		}
		return retorno;
	}

	@Override
	public List<PeriodoAcademico> listarPeriodos() {
		List<PeriodoAcademico> retorno = null;
		try {
			Query query = em.createQuery(
					"SELECT p FROM PeriodoAcademico p WHERE p.pracTipo = 0 AND p.pracEstado in (0,1) order by p.pracDescripcion");
			retorno = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al  consultar periodos");
		}
		return retorno;
	}

	@Override
	public List<Carrera> listarCarrerasxFacultadPeriodo(int dpnId, Integer pracId) {
		List<Carrera> retorno = null;
		try {
			Query query = em.createQuery("select  distinct cr from MallaPeriodo as mlpr "
					+ "join mlpr.mlprPeriodoAcademico as prac join mlpr.mlprMallaCurricular as mlcr "
					+ "join mlcr.mlcrCarrera as cr "
					+ "where prac.pracId =:pracId and cr.crrDependencia.dpnId=:dpnId " + "order by cr.crrDescripcion");
			query.setParameter("dpnId", dpnId);
			query.setParameter("pracId", pracId);
			retorno= query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al obtene carreras:"+e.getLocalizedMessage());
		}
		return retorno;
	}

}
