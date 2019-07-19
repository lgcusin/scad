package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.uce.edu.biometrico.jpa.Actividad;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.Aula;
import ec.uce.edu.biometrico.jpa.Bibliografia;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.ContenidoCurricular;
import ec.uce.edu.biometrico.jpa.Feriado;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.Herramienta;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.MallaCurricularMateria;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Metodologia;
import ec.uce.edu.biometrico.jpa.Parametro;
import ec.uce.edu.biometrico.jpa.Seguimiento;
import ec.uce.edu.biometrico.jpa.Syllabo;
import ec.uce.edu.biometrico.jpa.UnidadCurricular;

/**
 * Session Bean implementation class SrvSeguimiento
 */
@Stateless
@LocalBean
public class SrvSeguimiento implements SrvSeguimientoLocal {

	@PersistenceContext
	EntityManager em;
	private static String[] camposParametros = { "ENTRADA ANTES", "ENTRADA DESPUES", "SALIDA ANTES", "SALIDA DESPUES",
			"ATRASO" };

	public SrvSeguimiento() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Materia> listarAllMatByFcdc(Integer fdcId) {
		List<Materia> lstM;
		try {
			lstM = em.createNamedQuery("Materia.findAll", Materia.class).getResultList();
		} catch (Exception e) {
			System.out.println("No se encontraron " + e);
			return lstM = new ArrayList<>();
		}
		return lstM;
	}

	@Override
	public List<Carrera> listarAllCrrByFcdc(Integer fdcId) {
		List<Carrera> lstC;
		try {
			lstC = em.createNamedQuery("Carrera.findAllByFdId", Carrera.class).setParameter("fcdcId", fdcId)
					.getResultList();
		} catch (Exception e) {
			System.out.println("No se encontraron materias:" + e);
			return lstC = new ArrayList<>();
		}
		return lstC;
	}

	@Override
	public List<Materia> listarMatByCrr(Integer id) {
		List<Materia> lstM = new ArrayList<>();
		try {
			Query query = em
					.createQuery(
							"select m from Materia as m where m.carrera.crrId=:idcr and m.tipoMateria=2 order by m.mtrCodigo")
					.setParameter("idcr", id);
			lstM = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al encontrar materias de la carrera");
		}
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
		Syllabo syl = null;
		try {
			syl = em.createNamedQuery("Syllabo.findByMtrId", Syllabo.class).setParameter("mtrId", mtrId)
					.getSingleResult();
		} catch (Exception e) {
			System.out.println("No se ha encontrado Syllabo de la materia seleccionada");
		}
		return syl;
	}

	@Override
	public List<UnidadCurricular> listarUnidadCurricular(Integer mtrId) {
		List<UnidadCurricular> lstUc = em.createNamedQuery("UnidadCurricular.findAllBySylId", UnidadCurricular.class)
				.setParameter("mtrId", mtrId).getResultList();
		return lstUc;
	}

	@Override
	public List<ContenidoCurricular> listarContenidos(Integer uncrId) {
		List<ContenidoCurricular> lstCn = em.createNamedQuery("Contenido.findAllByUcId", ContenidoCurricular.class)
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

	/**
	 * Metodo que busca los nombres de los parametros correspondientes a las
	 * variables.
	 * 
	 * @param lstParametro
	 */
	public int buscarParametroVista(int index, List<Parametro> lstParametro) {
		if (lstParametro.get(index).getPrmDescripcion().equals(camposParametros[0])) {
			/** Obtiene los minutos del parametro */
			return Integer.parseInt(lstParametro.get(index).getPrmValor().substring(3, 5));
		}
		if (lstParametro.get(index).getPrmDescripcion().equals(camposParametros[1])) {
			/** Obtiene los minutos del parametro */
			return Integer.parseInt(lstParametro.get(index).getPrmValor().substring(3, 5));
		}
		if (lstParametro.get(index).getPrmDescripcion().equals(camposParametros[2])) {
			/** Obtiene los minutos del parametro */
			return Integer.parseInt(lstParametro.get(index).getPrmValor().substring(3, 5));
		}
		if (lstParametro.get(index).getPrmDescripcion().equals(camposParametros[3])) {
			/** Obtiene los minutos del parametro */
			return Integer.parseInt(lstParametro.get(index).getPrmValor().substring(3, 5));
		} else {
			/** Obtiene los minutos del parametro */
			return Integer.parseInt(lstParametro.get(index).getPrmValor().substring(3, 5));
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public HorarioAcademico verificarHorario(Date fecha, Integer fcdcId, boolean tipo, Integer fclId, Integer estado) {
		List<Parametro> lstP = new ArrayList<>();
		HorarioAcademico hr = new HorarioAcademico();
		int parametroEntrada = 0;
		int parametroSalida = 0;
		int parametroEntradaD = 0;
		int parametroSalidaD = 0;
		int atraso = 0;
		try {
			Query query = em
					.createQuery("select p from Parametro as p where p.dependencia.dpnId=:fclId order by p.prmId");
			query.setParameter("fclId", fclId);
			lstP = (List<Parametro>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Parametros: " + e);
		}

		if (!lstP.isEmpty()) {
			parametroEntrada = buscarParametroVista(0, lstP);
			parametroEntradaD = buscarParametroVista(1, lstP);
			parametroSalida = buscarParametroVista(2, lstP);
			parametroSalidaD = buscarParametroVista(3, lstP);
			atraso = buscarParametroVista(4, lstP);
		}
		int horas = Integer.parseInt(fecha.getHours() + "");
		int minutos = Integer.parseInt(fecha.getMinutes() + "");
		int horasProceso = 0;
		Double minutosProceso = 0.0;
		Double horIni;
		Double horFin;
		int holguraInicio = 0;
		int holguraFin = 0;
		switch (estado) {
		case 1:
			holguraInicio = minutos - parametroEntrada;
			holguraFin = minutos + parametroEntradaD;
			System.out.println("Iniciado");
			break;
		case 2:
			holguraInicio = minutos - parametroSalida;
			holguraFin = minutos + parametroSalidaD;
			System.out.println("finalizado");
			break;
		case 0:
			holguraInicio = minutos - (parametroEntrada + atraso);
			holguraFin = minutos + (parametroEntradaD - atraso);
			break;
		default:
			holguraInicio = minutos - (parametroEntradaD + atraso);
			holguraFin = minutos + (parametroEntradaD - atraso);
			break;
		}
		String horaRegistro = fecha.getHours() + ":" + fecha.getMinutes();
		System.out.println("Hora actual: " + horaRegistro);
		if (holguraInicio < 0) {
			horasProceso = horas - 1;
			if (holguraInicio == 0) {
				minutosProceso = 0.0;
			} else {
				minutosProceso = (double) (60 + holguraInicio);
			}
		} else {
			horasProceso = horas;
			minutosProceso = (double) holguraInicio;
		}
		horIni = (double) (horasProceso + (minutosProceso / 100));
		// String horIni = validarTamMinutos(horasProceso) + ":" +
		// validarTamMinutos(minutosProceso);

		if (holguraFin >= 60) {
			horasProceso = horas + 1;
			minutosProceso = (double) (holguraFin - 60);
		} else {
			horasProceso = horas;
			minutosProceso = (double) holguraFin;
		}
		horFin = (double) (horasProceso + (minutosProceso / 100));
		// String horFin = validarTamMinutos(horasProceso) + ":" +
		// validarTamMinutos(minutosProceso);
		System.out.println("Hora Holgura Inicio:" + horIni + "\nHora holgura Fin:" + horFin);
		try {
			Query query;
			Object[] objArray;
			if (tipo) {
				query = em.createQuery(
						"select  ass,ha,fd,mcp,mcm,m,hca,al,hc from Asistencia as ass join ass.horarioAcademico as ha join ass.fichaDocente as fd join ha.horaClaseAula as hca join hca.aula as al join hca.horaClase as hc join ha.mallaCurricularParalelo as mcp join mcp.mallaCurricularMateria as mcm join mcm.materia as m where fd.fcdcId=:fdId and ha.hracDia =:diaId");
				query.setParameter("diaId", fecha.getDay() - 1).setParameter("fdId", fcdcId);
				for (Object object : query.getResultList()) {
					objArray = (Object[]) object;
					hr = (HorarioAcademico) objArray[1];
					if (hr.getHoraClaseAula().getHoraClase().getHoclHoraInicio() >= horIni
							&& hr.getHoraClaseAula().getHoraClase().getHoclHoraInicio() <= horFin) {
						break;
					}
					hr = new HorarioAcademico();
				}
			} else {
				query = em.createQuery(
						"select  ass,ha,fd,mcp,mcm,m,hca,al,hc from Asistencia as ass join ass.horarioAcademico as ha join ass.fichaDocente as fd join ha.horaClaseAula as hca join hca.aula as al join hca.horaClase as hc join ha.mallaCurricularParalelo as mcp join mcp.mallaCurricularMateria as mcm join mcm.materia as m where fd.fcdcId=:fdId and ha.hracDia =:diaId ");
				query.setParameter("diaId", fecha.getDay()).setParameter("fdId", fcdcId);
				for (Object object : query.getResultList()) {
					objArray = (Object[]) object;
					hr = (HorarioAcademico) objArray[1];
					if (hr.getHoraClaseAula().getHoraClase().getHoclHoraFin() >= horIni
							&& hr.getHoraClaseAula().getHoraClase().getHoclHoraFin() <= horFin) {
						break;
					}
					hr = new HorarioAcademico();
				}
			}
		} catch (Exception e) {
			System.out.println("Error al obtener horarios: " + e);
			return hr;
		}
		return hr;

	}

	/**
	 * Metodo que valida que los minutos/horas menores a 10 tengan 2 digitos
	 * 
	 * @param minutosProceso
	 */

	private String validarTamMinutos(int minutosProceso) {
		String tamMinutos = "";
		if (minutosProceso < 10) {
			tamMinutos = "0" + minutosProceso;
		} else {
			tamMinutos = minutosProceso + "";
		}
		return tamMinutos;
	}

	@Override
	public Materia getMateria(Integer hrrId) {
		Materia mt;
		try {
			mt = em.createNamedQuery("Materia.findByHrId", Materia.class).setParameter("hrrId", hrrId)
					.getSingleResult();
		} catch (Exception e) {
			System.out.println(e);
			return mt = null;
		}
		return mt;
	}

	@Override
	public Aula getAula(Integer hrrId) {
		Aula aul;
		try {
			aul = em.createNamedQuery("Aula.findByHrId", Aula.class).setParameter("hrrId", hrrId).getSingleResult();
		} catch (Exception e) {
			System.out.println("No se encontro aula: " + e);
			return aul = null;
		}
		return aul;
	}

	@Override
	public List<ContenidoCurricular> buscarContenidos(Integer mtrId) {
		List<ContenidoCurricular> lstCn = new ArrayList<>();
		try {
			Query query;
			query = em.createQuery(
					"select cnt, un, sy from ContenidoCurricular as cnt join cnt.unidadCurricular as un join un.syllabo as sy where sy.mallaCurricularMateria.materia.mtrId=:mtId order by cnt.cncrId");
			query.setParameter("mtId", mtrId);
			for (Object obj : query.getResultList()) {
				Object[] objArray = (Object[]) obj;
				lstCn.add((ContenidoCurricular) objArray[0]);
			}

		} catch (Exception e) {
			System.out.println("No tiene contenidos:" + e);
			return lstCn;
		}
		return lstCn;
	}

	@Override
	public List<Asistencia> marcacionReg(Date fecha, Integer fcdcId) {
		List<Asistencia> lstA = new ArrayList<>();
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		String hora = formateador.format(fecha);
		System.out.println("Fecha actual: " + hora);
		String format = "dd/mm/yyyy";
		// String estado = "Finalizado";
		try {
			Object[] objArray;
			Query q = em.createQuery(
					"select ass, hr from Asistencia as ass join ass.horarioAcademico as hr where ass.fichaDocente.fcdcId=:fdId and to_char( ass.assFecha,:format) =:hora order by ass.assId");
			// and ass.assEstado <> :estado
			q.setParameter("fdId", fcdcId);
			q.setParameter("hora", hora);
			q.setParameter("format", format);
			// q.setParameter("estado", estado);
			for (Object obj : q.getResultList()) {
				objArray = (Object[]) obj;
				lstA.add((Asistencia) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("No hay una asistencia gregistrada" + e);
			return lstA;
		}
		return lstA;
	}

	@Override
	public void guardarRegistro(Asistencia regAss) {
		try {
			if (regAss.getAssId() != null) {
				System.out.println(regAss.getAssFecha());
				em.merge(regAss);
			} else {
				System.out.println(regAss.getAssFecha());
				regAss.setAssId(obtenerSecAsistencia() + 1);
				em.persist(regAss);
			}
		} catch (Exception e) {
			System.out.println("No se puede guardar asistencia");
		}

	}

	private int obtenerSecAsistencia() {
		int id;
		try {
			Query query = em.createQuery("select a.assId from Asistencia as a order by a.assId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los horarios secuencia" + e);
			return 0;
		}
		return id;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void generar(Date ahora, Integer fclId) {
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		String hora = formateador.format(ahora);
		try {
			Object[] objArrayH;
			Object[] objArrayF;
			Query query = em.createQuery(
					"select fr, fc from Feriado as fr join fr.dependencia as fc where fc.dpnId=:fclId and to_char(fr.frdFecha,'dd/MM/yyyy') =:fecha");
			query.setParameter("fclId", fclId).setParameter("fecha", hora);
			List<Object> lstObjF = query.getResultList();

			query = em.createQuery(
					"select ha.hracId,mcpr.mlcrprId,cr.crrId,nv.nvlId, p.prlId, ma.mtrId,ha.hracDia,hc.hoclId, al.alaId from HorarioAcademico as ha join ha.mallaCurricularParalelo as mcpr join mcpr.paralelo as p	join mcpr.mallaCurricularMateria as mcm join mcm.materia as ma join ma.carrera as cr join mcm.nivelByNvlId as nv join ha.horaClaseAula as hca join hca.horaClase as hc join hca.aula as al where mcpr.mlcrprId in (select ch.mallaCurricularParalelo.mlcrprId from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join mcp.mallaCurricularMateria.materia.carrera.dependencia as fc join ch.periodoAcademico as pa where pa.pracEstado=0 and fc.dpnId=:fclId group by ch.mallaCurricularParalelo.mlcrprId) and hca.hoclalEstado=0 and ha.hracDia=:diaId group by ha.hracId,mcpr.mlcrprId,cr.crrId,nv.nvlId, p.prlId, ma.mtrId,ha.hracDia,hc.hoclId, al.alaId order by mcpr.mlcrprId,cr.crrId,nv.nvlId, p.prlId");
			query.setParameter("fclId", fclId).setParameter("diaId", ahora.getDay() - 1);
			List<Object> lstObjH = query.getResultList();
			for (Object obj1 : lstObjH) {
				objArrayH = (Object[]) obj1;
				HorarioAcademico ha = em.find(HorarioAcademico.class, objArrayH[0]);
				if (!lstObjF.isEmpty()) {
					for (Object obj : lstObjF) {
						objArrayF = (Object[]) obj;
						Feriado fr = (Feriado) objArrayF[0];
						if (ha.getHracHoraInicio() >= Integer.parseInt(fr.getFrdInicio().substring(0, 2))
								&& ha.getHracHoraFin() <= Integer.parseInt(fr.getFrdFin().substring(0, 2))) {
						} else {
							FichaDocente fd = (FichaDocente) em
									.createQuery(
											"select fd from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.detallePuesto.fichaDocente as fd where mcp.mlcrprId=:mcpId")
									.setParameter("mcpId", objArrayH[1]).getSingleResult();
							Asistencia ass = new Asistencia();
							ass.setHorarioAcademico(ha);
							ass.setAssFecha(formateador.parse(hora));
							ass.setFichaDocente(fd);
							guardarRegistro(ass);
						}
					}
				} else {
					FichaDocente fd = (FichaDocente) em
							.createQuery(
									"select fd from CargaHoraria as ch inner join ch.mallaCurricularParalelo as mcp join ch.detallePuesto.fichaDocente as fd where mcp.mlcrprId=:mcpId")
							.setParameter("mcpId", objArrayH[1]).getSingleResult();
					Asistencia ass = new Asistencia();
					ass.setHorarioAcademico(ha);
					ass.setAssFecha(formateador.parse(hora));
					ass.setFichaDocente(fd);
					guardarRegistro(ass);
				}
			}

		} catch (Exception e) {
			System.out.println("Error al crear registro laboral docente");
		}
		System.out.println("Registro laboral docente creado");
	}

	@Override
	public void guardarActualizarSeguimiento(Seguimiento sgm) {
		try {
			if (sgm.getSgmId() != null) {
				em.merge(sgm);
			} else {
				sgm.setSgmId(obtenerSecuenciaSeguimiento() + 1);
				em.persist(sgm);
			}
		} catch (Exception e) {
			System.out.println("Error al guardar tema de clase");
		}

	}

	private int obtenerSecuenciaSeguimiento() {
		int id = 0;
		try {
			Query query = em.createQuery("select sgm.sgmId from Seguimiento as sgm order by sgm.sgmId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al obtener secuencia seguiento");
			return id;
		}
		return id;
	}

	@Override
	public Syllabo setSyllabus(MallaCurricularMateria mllCrrMateria) {
		Syllabo sy = new Syllabo();
		try {
			sy.setSylId(mllCrrMateria.getMlcrmtId());
			sy.setMallaCurricularMateria(mllCrrMateria);
			em.persist(sy);
		} catch (Exception e) {
			System.out.println("Error al crear Syllabo " + e);
		}
		return sy;
	}

	@Override
	public void guardarActualizarContenido(ContenidoCurricular cnt) {
		try {
			if (cnt.getCncrId() != null) {
				em.merge(cnt);
			} else {
				int c = obtenerSecuenciaContenido();
				cnt.setCncrId(c + 1);
				em.persist(cnt);

			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarContenido: " + e);
		}

	}

	private int obtenerSecuenciaContenido() {
		int id;
		try {
			Query query = em.createQuery("select c.cncrId from ContenidoCurricular as c order by c.cncrId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los contenidos secuencia" + e);
			return 0;
		}
		return id;
	}

	@Override
	public void guardarActualizarUnidad(UnidadCurricular uncr) {
		try {
			if (uncr.getUncrId() == null) {
				int u = obtenerSecuenciaUnidad();
				uncr.setUncrId(u + 1);
				em.persist(uncr);
			} else {
				em.merge(uncr);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarUnidadCurricular: " + e);
		}
	}

	private Integer obtenerSecuenciaUnidad() {
		int id;
		try {
			Query query = em.createQuery("select uc.uncrId from UnidadCurricular as uc order by uc.uncrId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los unidad secuencia " + e);
			return 0;
		}
		return id;
	}

	@Override
	public MallaCurricularMateria getMallaCurricularMateria(Integer mtrId) {
		MallaCurricularMateria mcm = null;
		try {
			Object[] objArray;
			Query query = em.createQuery(
					"select mcm, m, c, f, mc from MallaCurricularMateria as mcm join mcm.materia as m join m.carrera as c join c.dependencia as f join mcm.mallaCurricular as mc where m.mtrId=:mtrId");
			query.setParameter("mtrId", mtrId);
			Object obj = query.getSingleResult();
			objArray = (Object[]) obj;
			mcm = (MallaCurricularMateria) objArray[0];
		} catch (Exception e) {
			System.out.println("Error al obtener malla-curricular-materia " + e);
			return mcm = new MallaCurricularMateria();
		}
		return mcm;
	}

	@Override
	public void guardarActualizarSyllabus(Syllabo syl) {
		try {
			if (syl.getSylId() != null) {
				em.merge(syl);
			} else {
				int s = obtenerSecuenciaSyllabo();
				syl.setSylId(s + 1);
				em.persist(syl);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarSyllabo: " + e);
		}

	}

	private int obtenerSecuenciaSyllabo() {
		int id;
		try {
			Query query = em.createQuery("select syl.sylId from Syllabo as syl order by syl.sylId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los secuencia syllabus" + e);
			return 0;
		}
		return id;
	}

	@Override
	public void guardarActualizarActividad(Actividad act) {
		try {
			if (act.getActId() == null) {
				int a = obtenerSecuenciaActividad();
				act.setActId(a + 1);
				em.persist(act);
			} else {
				em.merge(act);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarActividad: " + e);
		}
	}

	private int obtenerSecuenciaActividad() {
		int id;
		try {
			Query query = em.createQuery("select act.actId from Actividad as act order by act.actId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los secuencia actividad" + e);
			return 0;
		}
		return id;
	}

	@Override
	public void guardarActualizarHerramienta(Herramienta her) {
		try {
			if (her.getHrrId() == null) {
				int h = obtenerSecuenciaHerramienta();
				her.setHrrId(h + 1);
				em.persist(her);
			} else {
				em.merge(her);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarHerramienta: " + e);
		}

	}

	private int obtenerSecuenciaHerramienta() {
		int id;
		try {
			Query query = em.createQuery("select her.hrrId from Herramienta as her order by her.hrrId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los secuencia herramienta" + e);
			return 0;
		}
		return id;
	}

	@Override
	public void guardarActualizarMetodologia(Metodologia mtd) {
		try {
			if (mtd.getMtdId() == null) {
				int m = obtenerSecuenciaMetodologia();
				mtd.setMtdId(m + 1);
				em.persist(mtd);
			} else {
				em.merge(mtd);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizar Metodologia: " + e);
		}
	}

	private int obtenerSecuenciaMetodologia() {
		int id;
		try {
			Query query = em.createQuery("select mtd.mtdId from Metodologia as mtd order by mtd.mtdId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los secuencia Metodologia" + e);
			return 0;
		}
		return id;
	}

	@Override
	public void guardarActualizarBibliografia(Bibliografia bbl) {
		try {
			if (bbl.getBblId() == null) {
				int b = obtenerSecuenciaBibliografia();
				bbl.setBblId(b + 1);
				em.persist(bbl);
			} else {
				em.merge(bbl);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizar Metodologia: " + e);
		}
	}

	private int obtenerSecuenciaBibliografia() {
		int id;
		try {
			Query query = em.createQuery("select bbl from Bibliografia as bbl order by bbl.bblId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los secuencia Bibliografia" + e);
			return 0;
		}
		return id;
	}

	@Override
	public List<Metodologia> listarMetodologias(Integer uncrId) {
		List<Metodologia> lstM = new ArrayList<>();
		try {
			Query query = em
					.createQuery("	select mtd from Metodologia as mtd where mtd.unidadCurricular.uncrId=:uncrId");
			query.setParameter("uncrId", uncrId);
			lstM = (List<Metodologia>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar metodologias: " + e);
			return lstM;
		}
		return lstM;
	}

	@Override
	public List<Bibliografia> listarBibliografias(Integer uncrId) {
		List<Bibliografia> lstB = new ArrayList<>();
		try {
			Query query = em
					.createQuery("select bbl from Bibliografia as bbl where bbl.unidadCurricular.uncrId=:uncrId");
			query.setParameter("uncrId", uncrId);
			lstB = (List<Bibliografia>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Parametros: " + e);
			return lstB;
		}
		return lstB;
	}

	@Override
	public void eliminarUnidad(UnidadCurricular uc) {
		try {
			em.remove(uc);
		} catch (Exception e) {
			System.out.println("Error al eliminar unidad");
		}

	}

	@Override
	public List<Seguimiento> getSeguimiento(Integer mtrId, Integer fdId) {
		List<Seguimiento> lstS = new ArrayList<>();
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery(
					"select sg, mcp,mcm, mt, ass, cr from Seguimiento as sg join sg.mallaCurricularParalelo as mcp join mcp.mallaCurricularMateria as mcm join mcm.materia as mt join sg.asistencia as ass join ass.fichaDocente as fd join sg.contenidoCurricular as cr where mt.mtrId=:mtId and fd.fcdcId=:fdId order by sg.sgmId");
			query.setParameter("mtId", mtrId).setParameter("fdId", fdId);
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				lstS.add((Seguimiento) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar seguimiento-syllabus");
			return lstS;
		}
		return lstS;
	}

	@Override
	public void generarAsistencias(HorarioAcademico hrac, FichaDocente docente, Date date) {
		Asistencia Asis = new Asistencia();
		Asis.setHorarioAcademico(hrac);
		Asis.setFichaDocente(docente);
		Asis.setAssFecha(date);
		Asis.setAssId(obtenerSecAsistencia() + 1);
		if (em.find(Asistencia.class, obtenerSecAsistencia()).getHorarioAcademico().getMallaCurricularParalelo()
				.getMlcrprId().equals(hrac.getMallaCurricularParalelo().getMlcrprId())) {

		} else {
			em.persist(Asis);
		}

	}

	@Override
	public String obtenerHoraClasexHorario(HorarioAcademico hrr) {
		String hora = "";
		List<HorarioAcademico> lstH = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em.createQuery(
					"select ha, hca, hc from HorarioAcademico as ha join ha.horaClaseAula as hca join hca.horaClase as hc where ha.mallaCurricularParalelo.mlcrprId=:mcpId and ha.hracDia=:dia order by ha.hracDescripcion");
			query.setParameter("mcpId", hrr.getMallaCurricularParalelo().getMlcrprId()).setParameter("dia",
					hrr.getHracDia());
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstH.add((HorarioAcademico) arrayObj[0]);
			}
			hora = lstH.get(0).getHoraClaseAula().getHoraClase().getHoclHoraInicio() + ":00 - "
					+ lstH.get(lstH.size() - 1).getHoraClaseAula().getHoraClase().getHoclHoraFin() + ":00";
		} catch (Exception e) {
			System.out.println("Error al obtener hora clase");
			return hora;
		}
		return hora;
	}

}
