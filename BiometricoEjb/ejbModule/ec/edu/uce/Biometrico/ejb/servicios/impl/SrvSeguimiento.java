package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvSeguimientoLocal;
import ec.edu.uce.biometrico.jpa.*;

/**
 * Session Bean implementation class SrvSeguimiento
 */
@Stateless
@Local
public class SrvSeguimiento implements SrvSeguimientoLocal {

	@PersistenceContext
	private EntityManager em;

	private static String[] camposParametros = { "ENTRADA ANTES", "ENTRADA DESPUES", "ATRASO", "SALIDA ANTES",
			"SALIDA DESPUES", };

	public SrvSeguimiento() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * lista todas las materias del docente
	 * 
	 * @param fdcID
	 *            id del docente
	 * @return materias
	 */
	@Override
	public List<Materia> listarAllMatByFcdc(Integer fdcId) {
		List<Materia> lstM = null;
		try {
			lstM = em.createNamedQuery("Materia.findAll", Materia.class).getResultList();
		} catch (Exception e) {
			System.out.println("No se encontraron " + e);
			return lstM = new ArrayList<>();
		}
		return lstM;
	}

	/**
	 * lista todos las carreras del docente
	 * 
	 * @param fdcId
	 *            id del docente
	 * @return carreras
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Carrera> listarAllCrrByFcdc(Integer fdcId) {
		List<Carrera> lstC = null;
		try {
			Query query;
			query = em
					.createQuery(
							"select distinct dp.dtpsCarrera from DetallePuesto as dp where dp.dtpsFichaDocente.fcdcId=:fcdcId")
					.setParameter("fcdcId", fdcId);
			lstC = query.getResultList();
		} catch (Exception e) {
			System.out.println("No se encontraron materias:" + e);
			return lstC;
		}
		return lstC;
	}

	/**
	 * Obtiene un lista de materias por la carrera seleccionada - agreagar
	 * sylabus
	 * 
	 * @param id
	 *            de la carrera
	 * @return materias
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Materia> listarMatByCrr(Integer id) {
		List<Materia> lstM = new ArrayList<>();
		try {
			Query query = em
					.createQuery(
							"select m from Materia as m where m.mtrCarrera.crrId=:idcr and m.mtrTipoMateria=2 order by m.mtrCodigo")
					.setParameter("idcr", id);
			lstM = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al encontrar materias de la carrera");
			return lstM;
		}
		return lstM;
	}

	/**
	 * obtiene la carrera para informacion del syllabus- agregar syllabus
	 * 
	 * @param crrId
	 *            id de la materia
	 * @return carrera
	 */
	@Override
	public Carrera obtenerCarreraSyllabus(Integer mtrId) {
		Carrera cr = null;
		try {
			Query query;
			query = em.createQuery("select m.mtrCarrera from Materia as m where m.mtrId=:mtrId").setParameter("mtrId",
					mtrId);
			cr = (Carrera) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al obtener carrera del syllabo");
			return cr;
		}
		return cr;
	}

	/**
	 * Obtiene el syllabus de la materia selecionada- agregar syllabus
	 * 
	 * @param mtrId
	 *            id de la materia
	 * @return syllabus
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Syllabo obtenerSyllabusxMateria(Integer mlcrmtId) {
		Syllabo syl = null;
		try {
			Query query = em
					.createQuery("select s from Syllabo as s where s.sylMallaCurricularMateria.mlcrmtId=:mlcrmtId");
			query.setParameter("mlcrmtId", mlcrmtId);
			List<Syllabo> lista = new ArrayList<>();
			lista = query.getResultList();
			if (!lista.isEmpty()) {
				syl = lista.get(0);
			}
		} catch (Exception e) {
			System.out.println("Error al obtener Syllabo de la materia seleccionada");
			return syl;
		}
		return syl;
	}

	/**
	 * Lista las unidades curriculares de la materia seleccionada - agregar
	 * syllabus
	 * 
	 * @param mtrId
	 *            id de la materia
	 * @return unidades curriculares
	 */
	@Override
	public List<UnidadCurricular> listarUnidadCurricular(Integer mtrId) {
		List<UnidadCurricular> lstUc = null;
		try {
			lstUc = em.createNamedQuery("UnidadCurricular.findAllBySylId", UnidadCurricular.class)
					.setParameter("mtrId", mtrId).getResultList();
		} catch (Exception e) {
			System.out.println("Error para listar unides curriculares de la mteria");
			return lstUc;
		}
		return lstUc;
	}

	/**
	 * Lista los cotenidos curriculares de la unidad curricular de la materia
	 * selecionada - agregar syllabus
	 * 
	 * @param uncrId
	 *            id de la unidad curricular
	 * @return contenidos curriculares
	 */
	@Override
	public List<ContenidoCurricular> listarContenidosCurriculares(Integer uncrId) {
		List<ContenidoCurricular> lstCn = null;
		try {
			lstCn = em.createNamedQuery("Contenido.findAllByUcId", ContenidoCurricular.class)
					.setParameter("ucId", uncrId).getResultList();
		} catch (Exception e) {
			System.out.println("Error al listar contenidos curriculares de la materia");
			return lstCn;
		}
		return lstCn;
	}

	/**
	 * Lista las actividades del contenido curricular de la materia seleccionada
	 * - agregar syllabus
	 * 
	 * @param cntId
	 *            id del contenido
	 * @return contenidos
	 */
	@Override
	public List<Actividad> listarActividades(Integer cntId) {
		List<Actividad> lstAc = null;
		try {
			lstAc = em.createNamedQuery("Actividad.findAllByCnId", Actividad.class).setParameter("cntId", cntId)
					.getResultList();
		} catch (Exception e) {
			System.out.println("Error al listar actividades del contenido");
			return lstAc;
		}
		return lstAc;
	}

	/**
	 * Lista las herrammientas utilixadas para los contenidos curriculares de la
	 * materia seleccionada - agregar syllabus
	 * 
	 * @param cntId
	 *            in del contenido
	 * @return herramientas
	 */
	@Override
	public List<Herramienta> listarHerramientas(Integer cntId) {
		List<Herramienta> lstHr = null;
		try {
			lstHr = em.createNamedQuery("Herramienta.findAllByCnId", Herramienta.class).setParameter("cntId", cntId)
					.getResultList();
		} catch (Exception e) {
			System.out.println("Error al listar herramientas del contenido curricular");
			return lstHr;
		}
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

	/**
	 * Obtiene un horario academico del periodo academico establecido y del
	 * docente - control
	 * 
	 * @param fecha
	 *            fecha actual
	 * @param fcdcId
	 *            id del docente
	 * @param b
	 *            tipo de horario inicio o fin
	 * @param fclId
	 *            id de la facutal
	 * @param tipo
	 *            id de tipo entrada o atraso
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public HorarioAcademico verificarHorario(Date fecha, Integer fcdcId, boolean tipo, Integer fclId, Integer estado) {
		List<Parametro> lstP = new ArrayList<>();
		HorarioAcademico hr = null;
		double parametroEntrada = 0;
		double parametroSalida = 0;
		double parametroEntradaD = 0;
		double parametroSalidaD = 0;
		double atraso = 0;
		try {
			Query query = em.createQuery(
					"select p from Parametro as p where p.prmDependencia.dpnId=:fclId order by p.prmPosicion asc");
			query.setParameter("fclId", fclId);
			lstP = (List<Parametro>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Parametros: " + e);
		}

		if (!lstP.isEmpty()) {
			parametroEntrada = buscarParametroVista(0, lstP);
			parametroEntradaD = buscarParametroVista(1, lstP);
			atraso = buscarParametroVista(2, lstP);
			parametroSalida = buscarParametroVista(3, lstP);
			parametroSalidaD = buscarParametroVista(4, lstP);
		}
		double horas = fecha.getHours();
		double minutos = fecha.getMinutes();
		double horasProceso = 0;
		double horIni = 0;
		double horFin = 0;
		String horaRegistro = fecha.getHours() + ":" + fecha.getMinutes();
		System.out.println("Hora actual: " + horaRegistro);
		try {
			Query query;
			Object[] objArray;
			double horActual = (double) (horas + (minutos / 100));
			List<Asistencia> lstAsistencias = obtenerAsistenciasxDocente(fecha, fcdcId);
			if (tipo) {
				for (Asistencia object : lstAsistencias) {
					hr = object.getAssHorarioAcademico();
					query = em.createQuery(
							"select ha, hca, hc from HorarioAcademico as ha join ha.hracHoraClaseAula as hca "
									+ "join hca.hoclalHoraClase as hc where ha.hracMallaCurricularParalelo.mlcrprId=:mcpId and ha.hracDia=:dia "
									+ "order by hc.hoclDescripcion");
					query.setParameter("mcpId", hr.getHracMallaCurricularParalelo().getMlcrprId()).setParameter("dia",
							hr.getHracDia());
					List<HorarioAcademico> lstH = new ArrayList<>();
					for (Object obj : query.getResultList()) {
						objArray = (Object[]) obj;
						lstH.add((HorarioAcademico) objArray[0]);
					}
					horasProceso = lstH.get(0).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio();
					switch (estado) {
					case 1:
						System.out.println("Iniciado");
						horIni = (double) (horasProceso - ((parametroEntrada + 40) / 100));
						horFin = (double) (horasProceso + (parametroEntradaD / 100));
						break;
					case 0:
						System.out.println("atrasado");
						horIni = (double) (horasProceso + (parametroEntradaD / 100));
						horFin = (double) (horasProceso + ((parametroEntradaD + atraso) / 100));
						break;
					}
					System.out.println("Hora Holgura Inicio:" + horIni + "\nHora holgura Fin:" + horFin);
					if (horActual >= horIni && horActual <= horFin) {
						break;
					}
					hr = null;
				}
			} else {
				for (Asistencia object : lstAsistencias) {
					hr = object.getAssHorarioAcademico();
					query = em.createQuery(
							"select ha, hca, hc from HorarioAcademico as ha join ha.hracHoraClaseAula as hca "
									+ "join hca.hoclalHoraClase as hc where ha.hracMallaCurricularParalelo.mlcrprId=:mcpId and ha.hracDia=:dia "
									+ "order by hc.hoclDescripcion");
					query.setParameter("mcpId", hr.getHracMallaCurricularParalelo().getMlcrprId()).setParameter("dia",
							hr.getHracDia());
					List<HorarioAcademico> lstH = new ArrayList<>();
					for (Object obj : query.getResultList()) {
						objArray = (Object[]) obj;
						lstH.add((HorarioAcademico) objArray[0]);
					}
					horasProceso = lstH.get(lstH.size() - 1).getHracHoraClaseAula().getHoclalHoraClase()
							.getHoclHoraFin();
					switch (estado) {
					case 2:
						System.out.println("Finalizado");
						horIni = (double) (horasProceso - ((parametroSalida + 40) / 100));
						horFin = (double) (horasProceso + (parametroSalidaD / 100));
						break;
					}
					System.out.println("Hora Holgura Inicio:" + horIni + "\nHora holgura Fin:" + horFin);
					if (horActual >= horIni && horActual <= horFin) {
						break;
					}
					hr = null;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al obtener horarios: " + e);
			return hr;
		}
		return hr;

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

	/**
	 * Obtiene los contenidos curriculares del syllabus para visualizar por el
	 * docente a registrar- control
	 * 
	 * @param mtrId
	 *            id de la amteria
	 * @return contenidos curriculares
	 */
	@Override
	public List<ContenidoCurricular> buscarContenidosCurriculares(Integer uncrId) {
		List<ContenidoCurricular> lstCn = null;
		try {
			Query query;
			query = em.createQuery(
					"select cnt, un, sy from ContenidoCurricular as cnt " + "join cnt.unidadCurricular as un "
							+ "join un.syllabo as sy " + "where un.uncrId=:uncrId order by cnt.cncrId");
			query.setParameter("uncrId", uncrId);
			lstCn = new ArrayList<>();
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

	/**
	 * Obtiene las asistencias generadas de la fecha actualdel docente y la
	 * materia a registrar
	 * 
	 * @param fecha
	 *            fecha actual
	 * @param fcdcId
	 *            id de docente
	 * @return asistencias
	 */
	@Override
	public List<Asistencia> obtenerAsistenciasxDocente(Date fecha, Integer fcdcId) {
		List<Asistencia> lstA = new ArrayList<>();
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		String hora = formateador.format(fecha);
		System.out.println("Fecha actual: " + hora);
		String format = "dd/mm/yyyy";
		try {
			Object[] objArray;
			Query q = em.createQuery("select a, h,mcp, mcm, m, hca, hc from Asistencia as a "
					+ "join a.assHorarioAcademico as h join h.hracMallaCurricularParalelo as mcp "
					+ "join mcp.mlcrprMallaCurricularMateria as mcm join mcm.mlcrmtMateria as m "
					+ "join h.hracHoraClaseAula as hca join hca.hoclalHoraClase as hc "
					+ "where a.assFichaDocente.fcdcId=:fdId and to_char( a.assFecha,:format) =:hora order by hc.hoclDescripcion asc");

			q.setParameter("fdId", fcdcId);
			q.setParameter("hora", hora);
			q.setParameter("format", format);
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

	/**
	 * Guarda las asistencias encontradascon la informacion de entrada
	 * 
	 * @param regAss
	 *            asistencia a guardar
	 */
	@SuppressWarnings("null")
	@Override
	public void guardarRegistro(Asistencia regAss) {
		try {
			if (regAss != null) {
				em.merge(regAss);
			} else {
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

	/**
	 * Metodo de autogeneracion de asistencias de los docenetes en la fecha
	 * actual de la facultad - control
	 * 
	 * @param ahora
	 *            fecha y hora actual
	 * @param fclId
	 *            id del docente
	 * @return true- generado, false-no generado
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public boolean generarAsistecniasxFacultad(Date ahora, Integer fclId) {
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		String hora = formateador.format(ahora);
		try {
			Object[] objArrayH;
			Object[] objArrayF;
			Query query = em.createQuery(
					"select fr, cr, fc from Feriado as fr join fr.frdCarrera as cr join cr.crrDependencia as fc where fc.dpnId=:fclId and to_char(fr.frdFecha,'dd/MM/yyyy') =:fecha");
			query.setParameter("fclId", fclId).setParameter("fecha", hora);
			List<Object> lstObjF = query.getResultList();

			query = em.createQuery("select ha, mcpr, cr, nv, p, ma, ha, hc, al from HorarioAcademico as ha "
					+ "join ha.hracMallaCurricularParalelo as mcpr " + "join mcpr.mlcrprParalelo as p "
					+ "join mcpr.mlcrprMallaCurricularMateria as mcm " + "join mcm.mlcrmtMateria as ma "
					+ "join ma.mtrCarrera as cr " + "join mcm.mlcrmtNivel as nv " + "join ha.hracHoraClaseAula as hca "
					+ "join hca.hoclalHoraClase as hc " + "join hca.hoclalAula as al "
					+ "where mcpr.mlcrprId in (select mcp.mlcrprId from CargaHoraria as ch "
					+ "inner join ch.crhrMallaCurricularParalelo as mcp "
					+ "join mcp.mlcrprMallaCurricularMateria.mlcrmtMateria.mtrCarrera.crrDependencia as fc "
					+ "join ch.crhrPeriodoAcademico as pa "
					+ "where pa.pracEstado=0 and fc.dpnId=:fclId group by mcp.mlcrprId) "
					+ "and ha.hracDia=:diaId order by mcpr.mlcrprId,cr.crrId,nv.nvlId, p.prlId");
			query.setParameter("fclId", fclId).setParameter("diaId", ahora.getDay() - 1);
			List<Object> lstObjH = query.getResultList();
			for (Object obj1 : lstObjH) {
				objArrayH = (Object[]) obj1;
				HorarioAcademico ha = (HorarioAcademico) objArrayH[0];
				if (!lstObjF.isEmpty()) {
					for (Object obj : lstObjF) {
						objArrayF = (Object[]) obj;
						Feriado fr = (Feriado) objArrayF[0];
						if (ha.getHracMallaCurricularParalelo().getMlcrprMallaCurricularMateria().getMlcrmtMateria()
								.getMtrCarrera().getCrrId() == fr.getFrdCarrera().getCrrId()) {
							if (ha.getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio() >= Integer
									.parseInt(fr.getFrdInicio().substring(0, 2))
									&& ha.getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraFin() <= Integer
											.parseInt(fr.getFrdFin().substring(0, 2))) {
							} else {
								List<FichaDocente> lstFd = em
										.createQuery(
												"select fd from CargaHoraria as ch inner join ch.crhrMallaCurricularParalelo as mcp join ch.crhrDetallePuesto.dtpsFichaDocente as fd where mcp.mlcrprId=:mcpId")
										.setParameter("mcpId", ha.getHracMallaCurricularParalelo().getMlcrprId())
										.getResultList();
								for (FichaDocente fd : lstFd) {
									generarAsistencias(ha, fd, formateador.parse(hora));
								}
							}
						}

					}
					return true;
				} else {
					List<FichaDocente> lstFd = em
							.createQuery(
									"select fd from CargaHoraria as ch inner join ch.crhrMallaCurricularParalelo as mcp join ch.crhrDetallePuesto.dtpsFichaDocente as fd where mcp.mlcrprId=:mcpId")
							.setParameter("mcpId", ha.getHracMallaCurricularParalelo().getMlcrprId()).getResultList();
					for (FichaDocente fd : lstFd) {
						generarAsistencias(ha, fd, formateador.parse(hora));
					}

				}
			}
			return true;
		} catch (Exception e) {
			System.out.println("Error al crear registro laboral docente " + e.getMessage());
			return false;
		}
	}

	/**
	 * Guarda un registro de seguimiento con la tarea seleccionda en la
	 * asistencia inicio o fin
	 * 
	 * @param seguimiento
	 *            seguimiento a guardar
	 */
	@Override
	public void guardarActualizarSeguimiento(Seguimiento sgm) {
		try {
			if (sgm != null) {
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
			sy.setSylMallaCurricularMateria(mllCrrMateria);
			em.persist(sy);
		} catch (Exception e) {
			System.out.println("Error al crear Syllabo " + e.getMessage());
		}
		return sy;
	}

	/**
	 * Guarda el contenido curricular ingresado en el syllabus- agregar syllabus
	 * 
	 * @param cnt
	 *            in del contenido curricular
	 * @return
	 */
	@Override
	public ContenidoCurricular guardarActualizarContenido(ContenidoCurricular cnt) {
		ContenidoCurricular reg = em.find(ContenidoCurricular.class, cnt.getCncrId());
		try {
			if (reg != null) {
				for (Actividad actividad : cnt.getActividads()) {
					actividad.setActContenidoCurricular(reg);
					guardarActualizarActividad(actividad);
				}
				for (Herramienta herramienta : cnt.getHerramientas()) {
					herramienta.setHrrContenidoCurricular(reg);
					guardarActualizarHerramienta(herramienta);
				}
				em.merge(cnt);
			} else {
				// int c = obtenerSecuenciaContenido();
				// cnt.setCncrId(c + 1);
				em.persist(cnt);
				for (Actividad actividad : cnt.getActividads()) {
					actividad.setActContenidoCurricular(cnt);
					guardarActualizarActividad(actividad);
				}
				for (Herramienta herramienta : cnt.getHerramientas()) {
					herramienta.setHrrContenidoCurricular(cnt);
					guardarActualizarHerramienta(herramienta);
				}
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarContenido: " + e.getMessage());
			return cnt = null;
		}
		return cnt;
	}

	private int obtenerSecuenciaContenido() {
		int id;
		try {
			Query query = em.createQuery("select c.cncrId from ContenidoCurricular as c order by c.cncrId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los contenidos secuencia" + e.getMessage());
			return 0;
		}
		return id;
	}

	@Override
	public UnidadCurricular guardarActualizarUnidad(UnidadCurricular uncr) {
		UnidadCurricular reg = em.find(UnidadCurricular.class, uncr.getUncrId());
		try {
			if (reg != null) {
				for (ContenidoCurricular contenidoCurricular : uncr.getContenidos()) {
					contenidoCurricular.setUnidadCurricular(reg);
					guardarActualizarContenido(contenidoCurricular);
				}
				for (Metodologia metodologia : uncr.getMetodologias()) {
					metodologia.setMtdUnidadCurricular(reg);
					guardarActualizarMetodologia(metodologia);
				}
				for (Bibliografia bibliografia : uncr.getBibliografias()) {
					bibliografia.setBblUnidadCurricular(reg);
					guardarActualizarBibliografia(bibliografia);
				}
				for (RecursoDidactico recurso : uncr.getRecursosDidacticoses()) {
					recurso.setRcddUnidadCurricular(reg);
					guardarActualizarRecursoDidactico(recurso);
				}
				em.merge(uncr);
			} else {
				// int u = obtenerSecuenciaUnidad();
				// uncr.setUncrId(u + 1);
				em.persist(uncr);
				for (ContenidoCurricular contenidoCurricular : uncr.getContenidos()) {
					contenidoCurricular.setUnidadCurricular(uncr);
					guardarActualizarContenido(contenidoCurricular);
				}
				for (Metodologia metodologia : uncr.getMetodologias()) {
					metodologia.setMtdUnidadCurricular(uncr);
					guardarActualizarMetodologia(metodologia);
				}
				for (Bibliografia bibliografia : uncr.getBibliografias()) {
					bibliografia.setBblUnidadCurricular(uncr);
					guardarActualizarBibliografia(bibliografia);
				}
				for (RecursoDidactico recurso : uncr.getRecursosDidacticoses()) {
					recurso.setRcddUnidadCurricular(uncr);
					guardarActualizarRecursoDidactico(recurso);
				}
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarUnidadCurricular: " + e.getMessage());
			return uncr = null;
		}
		return uncr;
	}

	private int obtenerSecuenciaUnidad() {
		int id;
		try {
			Query query = em.createQuery("select uc.uncrId from UnidadCurricular as uc order by uc.uncrId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los unidad secuencia " + e.getMessage());
			return 0;
		}
		return id;
	}

	/**
	 * Obtiene la malla curricular materia a la que pertenece la materia -
	 * agregar syllabus
	 * 
	 * @param mtrId
	 *            id de la materia
	 * @return malla curricular
	 */
	@Override
	public MallaCurricularMateria getMallaCurricularMateria(Integer mtrId) {
		MallaCurricularMateria mcm = null;
		try {
			Object[] objArray;
			Query query = em.createQuery("select mcm, m, c, f, mc, nvl, unf from MallaCurricularMateria as mcm "
					+ "join mcm.mlcrmtMateria as m " + "join m.mtrCarrera as c " + "join c.crrDependencia as f "
					+ "join mcm.mlcrmtMallaCurricular as mc " + "join mcm.mlcrmtNivel as nvl "
					+ "join mcm.mlcrmtUnidadFormacion as unf " + "where m.mtrId=:mtrId");
			query.setParameter("mtrId", mtrId);
			Object obj = query.getSingleResult();
			objArray = (Object[]) obj;
			mcm = (MallaCurricularMateria) objArray[0];
		} catch (Exception e) {
			System.out.println("Error al obtener malla-curricular-materia " + e.getMessage());
			return mcm = new MallaCurricularMateria();
		}
		return mcm;
	}

	/**
	 * Guarda el syllabu de la materia- agregar syllabus
	 * 
	 * @param sylllabus
	 *            a guardar
	 */
	@Override
	public void guardarActualizarSyllabus(Syllabo syl) {
		Syllabo reg = em.find(Syllabo.class, syl.getSylId());
		try {
			if (reg != null) {
				for (UnidadCurricular unidad : syl.getSylUnidadCurriculars()) {
					guardarActualizarUnidad(unidad);
				}
				em.merge(syl);
			} else {
				// int s = obtenerSecuenciaSyllabo();
				// syl.setSylId(s + 1);
				em.persist(syl);
				for (UnidadCurricular unidad : syl.getSylUnidadCurriculars()) {
					guardarActualizarUnidad(unidad);
				}
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarSyllabo: " + e.getMessage());
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

	/**
	 * Guarda la actividad del contenido ingresado en el syllabus - agregar
	 * syllabus
	 * 
	 * @param actividad
	 */
	@Override
	public void guardarActualizarActividad(Actividad act) {
		Actividad reg = em.find(Actividad.class, act.getActId());
		try {
			if (reg != null) {
				em.merge(act);
			} else {
				//int a = obtenerSecuenciaActividad();
				//act.setActId(a + 1);
				em.persist(act);
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

	/**
	 * Guarda la herrammienta del contenido ingresado en el syllabus - agregar
	 * syllabus
	 * 
	 * @param herramienta
	 *            a guardar
	 */
	@Override
	public void guardarActualizarHerramienta(Herramienta her) {
		try {
			Herramienta reg = em.find(Herramienta.class, her.getHrrId());
			if (reg != null) {
				em.merge(her);
			} else {
				//int h = obtenerSecuenciaHerramienta();
				//her.setHrrId(h + 1);
				em.persist(her);
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

	/**
	 * Guarda la metodologia de la unidad curricular utilizada en el syllabus -
	 * agregar syllabus
	 * 
	 * @param metodologia
	 *            a guardar
	 */
	@Override
	public void guardarActualizarMetodologia(Metodologia mtd) {
		Metodologia reg = em.find(Metodologia.class, mtd.getMtdId());
		try {
			if (reg != null) {
				// int m = obtenerSecuenciaMetodologia();
				// mtd.setMtdId(m + 1);
				em.merge(mtd);
			} else {
				em.persist(mtd);
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

	/**
	 * Guara la bibliografia de la unidad surricular ingresada en el syllabus -
	 * agregar syllabus
	 * 
	 * @param bibliografia
	 *            a guardar
	 */
	@Override
	public void guardarActualizarBibliografia(Bibliografia bbl) {
		Bibliografia reg = em.find(Bibliografia.class, bbl.getBblId());
		try {
			if (reg != null) {
				// int b = obtenerSecuenciaBibliografia();
				// bbl.setBblId(b + 1);
				em.merge(bbl);
			} else {
				em.persist(bbl);
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
					.createQuery("select mtd from Metodologia as mtd where mtd.mtdUnidadCurricular.uncrId=:uncrId");
			query.setParameter("uncrId", uncrId);
			lstM = query.getResultList();
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
					.createQuery("select bbl from Bibliografia as bbl where bbl.bblUnidadCurricular.uncrId=:uncrId");
			query.setParameter("uncrId", uncrId);
			lstB = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar bibliografias: " + e);
			return lstB;
		}
		return lstB;
	}

	/**
	 * Elimina una unidad curricular ingresada en el syllabus - agregar syllabus
	 * 
	 * @param uc
	 *            unidad a guardar
	 */
	@Override
	public void eliminarUnidad(UnidadCurricular uc) {
		int uncrId = uc.getUncrId();
		try {
			em.createQuery("DELETE FROM UnidadCurricular as uc where uc.uncrId=:uncrId").setParameter("uncrId", uncrId)
					.executeUpdate();
			em.remove(uc);
		} catch (Exception e) {
			System.out.println("Error al eliminar unidad: " + e.getMessage());
		}
	}

	/**
	 * Obtiene los seguimientos guardados de cada asistencia de la materia -
	 * control
	 * 
	 * @param mtrId
	 *            id de la materia
	 * @param fcdcId
	 *            id del docente
	 * @return seguimientos
	 */
	@Override
	public List<Seguimiento> obtenerSeguimientosxMateria(Integer mcpId, Integer fdId) {
		List<Seguimiento> lstS = null;
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery("select sg, mcp, ass, fd, cnt from Seguimiento as sg "
					+ "join sg.sgmMallaCurricularParalelo as mcp join sg.asistencia as ass join ass.assFichaDocente as fd "
					+ "join sg.sgmContenidoCurricular as cnt "
					+ "where mcp.mlcrprId=:mcpId and fd.fcdcId=:fdId order by sg.sgmId asc");
			query.setParameter("mcpId", mcpId).setParameter("fdId", fdId);
			lstS = new ArrayList<>();
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				lstS.add((Seguimiento) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar seguimiento-syllabus" + e.getMessage());
			return lstS;
		}
		return lstS;
	}

	/**
	 * Guardar una asistencia con el horario academico y el docente en la fecha
	 * actual - generar registros
	 * 
	 * @param hrac
	 *            horario academico
	 * @param docente
	 *            docente a guardar
	 * @param date
	 *            fecha actual
	 */
	@Override
	public void generarAsistencias(HorarioAcademico hrac, FichaDocente docente, Date date) {
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		String hora = formateador.format(date);
		Asistencia Asis = new Asistencia();
		Asis.setAssHorarioAcademico(hrac);
		Asis.setAssFichaDocente(docente);
		Asis.setAssFecha(date);
		Asis.setAssEstado("FALTA");
		Asis.setAssId(obtenerSecAsistencia() + 1);
		List<Asistencia> results = null;
		try {
			Query query = em.createQuery(
					"select ass from Asistencia as ass where to_char(ass.assFecha,'dd/MM/yyyy') =:fecha and ass.assFichaDocente.fcdcId=:fdId and ass.assHorarioAcademico.hracId=:haId");
			query.setParameter("fdId", docente.getFcdcId()).setParameter("fecha", hora).setParameter("haId",
					hrac.getHracId());
			results = query.getResultList();
			if (results.isEmpty()) {
				em.persist(Asis);
			}
		} catch (Exception e) {
			System.out.println("error al guardar registros de asistencias: " + e.getMessage());
		}
	}

	/**
	 * Obtiene una hora de inicio y de fin del horario acadmeico para el
	 * registro- control
	 * 
	 * @param hrrI
	 *            horario
	 * @return hora inicio y fin
	 */
	@Override
	public String obtenerHoraClasexHorario(HorarioAcademico hrr) {
		String hora = "";
		List<HorarioAcademico> lstH = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em
					.createQuery("select ha, hca, hc from HorarioAcademico as ha join ha.hracHoraClaseAula as hca "
							+ "join hca.hoclalHoraClase as hc where ha.hracMallaCurricularParalelo.mlcrprId=:mcpId and ha.hracDia=:dia "
							+ "order by hc.hoclDescripcion");
			query.setParameter("mcpId", hrr.getHracMallaCurricularParalelo().getMlcrprId()).setParameter("dia",
					hrr.getHracDia());
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstH.add((HorarioAcademico) arrayObj[0]);
			}
			hora = lstH.get(0).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraInicio() + ":00 - "
					+ lstH.get(lstH.size() - 1).getHracHoraClaseAula().getHoclalHoraClase().getHoclHoraFin() + ":00";
		} catch (Exception e) {
			System.out.println("Error al obtener hora clase");
			return hora;
		}
		return hora;
	}

	/**
	 * Obtiene los recursos didacticos de la Unidad Curricular selecciona
	 * 
	 * @param uncrId
	 *            id de LaUnidad Curricular
	 * @return Lista de recursos
	 */
	@Override
	public List<RecursoDidactico> listarRecursosDidacticos(int uncrId) {
		List<RecursoDidactico> lstR = new ArrayList<>();
		try {
			Query query = em.createQuery(
					"select rcdd from RecursoDidactico as rcdd where rcdd.rcddUnidadCurricular.uncrId=:uncrId");
			query.setParameter("uncrId", uncrId);
			lstR = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Recursos Didactico: " + e);
			return lstR;
		}
		return lstR;
	}

	/**
	 * Actualiza o guardar el recurso didactico ingresado
	 * 
	 * @param recurso
	 *            Objeto a guardar
	 */
	@Override
	public void guardarActualizarRecursoDidactico(RecursoDidactico recurso) {
		RecursoDidactico reg = em.find(RecursoDidactico.class, recurso.getRcddId());
		try {
			if (reg != null) {
				// int r = obtenerSecuenciaRecurso();
				// recurso.setRcddId(r + 1);
				em.merge(recurso);
			} else {
				em.persist(recurso);
			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizar Recurso: " + e);
		}

	}

	private int obtenerSecuenciaRecurso() {
		int id;
		try {
			Query query = em.createQuery("select rcd.rcddId from RecursosDidacticos as rcd order by rcd.rcddId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los secuencia Recurso" + e);
			return 0;
		}
		return id;
	}

	@Override
	public List<PeriodoAcademico> listarPeriodos(int i, int j) {
		List<PeriodoAcademico> retorno = null;
		try {
			Query query = em.createNamedQuery("PeriodoAcademico.findPorTipoEstado");
			query.setParameter("pracTipo", i);
			query.setParameter("pracEstado", j);
			retorno = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al  consultar periodos");
		}
		return retorno;
	}

	@Override
	public List<ContenidoCurricular> buscarAllContenidosCurriculares(int mlcrmtId) {
		List<ContenidoCurricular> retorno = null;
		try {
			Query query;
			query = em.createQuery(
					"select cnt, un, sy from ContenidoCurricular as cnt " + "join cnt.unidadCurricular as un "
							+ "join un.syllabo as sy " + "where sy.sylId=:mlcrmtId order by cnt.cncrId");
			query.setParameter("mlcrmtId", mlcrmtId);
			retorno = new ArrayList<>();
			for (Object obj : query.getResultList()) {
				Object[] objarray = (Object[]) obj;
				retorno.add((ContenidoCurricular) objarray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al buscar todos los contenidos" + e.getMessage());
			return retorno;
		}
		return retorno;
	}

	/**
	 * Busca la unidades curriculares del sylabus al que pertence la materia del
	 * horario actua
	 * 
	 * @param mlcrmtId
	 *            Id del Syllabus
	 * @return Lista de Unidades Curriculares
	 */
	@Override
	public List<UnidadCurricular> buscarAllUnidadesCurriculares(int mlcrmtId) {
		List<UnidadCurricular> retorno = null;
		try {
			Query query;
			query = em.createQuery("select un, sy from UnidadCurricular as un " + "join un.syllabo as sy "
					+ "where sy.sylId=:mlcrmtId order by un.uncrId");
			query.setParameter("mlcrmtId", mlcrmtId);
			retorno = new ArrayList<>();
			for (Object obj : query.getResultList()) {
				Object[] objarray = (Object[]) obj;
				retorno.add((UnidadCurricular) objarray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al buscar todos las unidades curriculares" + e.getMessage());
			return retorno;
		}
		return retorno;
	}

	/**
	 * Obtiene los syllabus regostrados en periodos anteriores
	 * @param selectMtr Materia seleciona a para asignar un syllabo
	 * @return Lista de syllabos
	 */
	@Override
	public List<Syllabo> buscarAllSyllaboxPeriodo(MallaCurricularMateria mllCrrMateria) {
		List<Syllabo> retorno=new ArrayList<>();
		try{
			Object[] objarray;
			Query query = em.createQuery("select syl, mlcrmt, mtr, crr, mlcr from Syllabo as syl "
					+ "join syl.sylMallaCurricularMateria as mlcrmt "
					+ "join mlcrmt.mlcrmtMateria as mtr "
					+ "join mtr.mtrCarrera as crr "
					+ "join crr.crrDependencia as dpn "
					+ "join mlcrmt.mlcrmtMallaCurricular as mlcr "
					+ "join mlcrmt.mlcrmtNivel as sms  "
					+ "where dpn.dpnId=:dpnId and mtr.mtrDescripcion =:mtrDescripcion and sms.nvlId=:nvlId");
			query.setParameter("dpnId", mllCrrMateria.getMlcrmtMateria().getMtrCarrera().getCrrDependencia().getDpnId());
			query.setParameter("mtrDescripcion", mllCrrMateria.getMlcrmtMateria().getMtrDescripcion());
			query.setParameter("nvlId", mllCrrMateria.getMlcrmtNivel().getNvlId());
			for(Object obj: query.getResultList()){
				objarray = (Object[]) obj;
				retorno.add((Syllabo) objarray[0]);
			}
		}catch (Exception e) {
			System.out.println("Error al listar syllabos: "+ e.getMessage());
		}
		return retorno;
	}

	// @Override
	// public Integer obtenerHoraSyllabusRestante(int mtrId, int fdId) {
	// int retorno = 0;
	// try {
	// Query query;
	// // Object[] objArray;
	// query = em.createQuery("select sg.sgmHoraClaseRestante from Seguimiento
	// as sg "
	// + "join sg.sgmMallaCurricularParalelo as mcp join
	// mcp.mlcrprMallaCurricularMateria as mcm "
	// + "join mcm.mlcrmtMateria as mt join sg.asistencia as ass join
	// ass.assFichaDocente as fd "
	// + "where mt.mtrId=:mtId and fd.fcdcId=:fdId and sg.sgmEstado =
	// 'COMPLETADO' order by sg.sgmId desc");
	// query.setParameter("mtId", mtrId).setParameter("fdId", fdId);
	// query.setMaxResults(1);
	// retorno = (int) query.getSingleResult();
	// } catch (Exception e) {
	// System.out.println("Error al consulta hora restante");
	// return retorno;
	// }
	// return retorno;
	// }

}
