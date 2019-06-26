package servicios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Actividad;
import model.Asistencia;
import model.Aula;
import model.Bibliografia;
import model.Carrera;
import model.Contenido;
import model.Feriado;
import model.FichaDocente;
import model.Herramienta;
import model.Horario;
import model.MallaCurricularMateria;
import model.Materia;
import model.Metodologia;
import model.Parametro;
import model.Syllabo;
import model.UnidadCurricular;

/**
 * Session Bean implementation class SrvSeguimiento
 */
@Stateless
@LocalBean
public class SrvSeguimiento implements SrvSeguimientoLocal {

	@PersistenceContext
	EntityManager em;
	private static String[] camposParametros = { "ENTRADA", "SALIDA" };

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
		List<Materia> lstM = em.createNamedQuery("Materia.findAllById", Materia.class).setParameter("idcr", id)
				.getResultList();
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
	public List<Contenido> listarContenidos(Integer uncrId) {
		List<Contenido> lstCn = em.createNamedQuery("Contenido.findAllByUcId", Contenido.class)
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
	private int buscarParametroVista(int index, List<Parametro> lstParametro) {
		if (lstParametro.get(index).getPrmNombre().equalsIgnoreCase(camposParametros[0])) {
			/** Obtiene los minutos del parametro */
			return Integer.parseInt(lstParametro.get(index).getPrmValor().substring(3, 5));
		} else {
			/** Obtiene los minutos del parametro */
			return Integer.parseInt(lstParametro.get(index).getPrmValor().substring(3, 5));
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public Horario verificarHorario(Date fecha, Integer fcdcId, Boolean tipo, Integer fclId) {
		List<Parametro> lstP = new ArrayList<>();
		int parametroEntrada = 0;
		int parametroSalida = 0;
		try {
			Query query = em.createQuery("select p from Parametro as p where p.fclId=:fclId");
			query.setParameter("fclId", fclId);
			lstP = (List<Parametro>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Parametros: " + e);
		}

		if (!lstP.isEmpty()) {
			parametroEntrada = buscarParametroVista(0, lstP);
			parametroSalida = buscarParametroVista(1, lstP);
		}
		int horas = Integer.parseInt(fecha.getHours() + "");
		int minutos = Integer.parseInt(fecha.getMinutes() + "");
		int horasProceso = 0;
		int minutosProceso = 0;
		int holguraInicio = minutos - parametroEntrada;
		int holguraFin = minutos + parametroSalida;
		String horaRegistro = fecha.getHours() + ":" + fecha.getMinutes();
		System.out.println("Hora actual: " + horaRegistro);
		if (holguraInicio < 0) {
			horasProceso = horas - 1;
			if (holguraInicio == 0) {
				minutosProceso = 0;
			} else {
				minutosProceso = 60 + holguraInicio;
			}
		} else {
			horasProceso = horas;
			minutosProceso = holguraInicio;
		}
		String horIni = validarTamMinutos(horasProceso) + ":" + validarTamMinutos(minutosProceso);

		if (holguraFin >= 60) {
			horasProceso = horas + 1;
			minutosProceso = holguraFin - 60;
		} else {
			horasProceso = horas;
			minutosProceso = holguraFin;
		}

		String horFin = validarTamMinutos(horasProceso) + ":" + validarTamMinutos(minutosProceso);
		Horario hr;
		System.out.println("Hora Holgura Inicio:" + horIni + "\nHora holgura Fin:" + horFin);
		try {
			Query query;
			Object[] objArray;
			Object obj;
			if (tipo) {
				query = em.createQuery(
						"select h,m,a from Horario as h join h.materia as m join h.aula as a where h.diaSemana.dsmId=:diaId and h.fichaDocente.fcdcId=:fdId  and (h.hrrInicio between :iniH and :finH)");
				obj = query.setParameter("diaId", fecha.getDay()).setParameter("fdId", fcdcId)
						.setParameter("iniH", horIni).setParameter("finH", horFin).getSingleResult();
				objArray = (Object[]) obj;
				hr = (Horario) objArray[0];
			} else {
				query = em.createQuery(
						"select h,m,a from Horario as h join h.materia as m join h.aula as a where h.diaSemana.dsmId=:diaId and h.fichaDocente.fcdcId=:fdId  and (h.hrrFin between :iniH and :finH)");
				obj = query.setParameter("diaId", fecha.getDay()).setParameter("fdId", fcdcId)
						.setParameter("iniH", horIni).setParameter("finH", horFin).getSingleResult();
				objArray = (Object[]) obj;
				hr = (Horario) objArray[0];
			}
		} catch (Exception e) {
			System.out.println("Error al obtener horarios: " + e);
			return hr = null;
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
	public List<Contenido> getContenidos(Integer mtrId) {
		List<Contenido> lstCn = new ArrayList<>();
		try {
			Query query;
			query = em.createQuery(
					"select cnt,un from Contenido as cnt join cnt.unidadCurricular as un where cnt.unidadCurricular.syllabo.mallaCurricularMateria.materia.mtrId=:mtId order by un.uncrId,cnt.cntId");
			query.setParameter("mtId", mtrId);
			for (Object obj : query.getResultList()) {
				Object[] objArray = (Object[]) obj;
				lstCn.add((Contenido) objArray[0]);
			}

		} catch (Exception e) {
			System.out.println("No tiene contenidos:" + e);
			return lstCn = new ArrayList<>();
		}
		return lstCn;
	}

	@Override
	public List<Asistencia> marcacionReg(Date fecha, Integer fcdcId) {
		List<Asistencia> asis;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
		String hora = formateador.format(fecha);
		System.out.println("Fecha actual: " + hora);
		String format = "dd/mm/yyyy";
		// String estado = "Finalizado";
		try {
			Query q = em.createQuery(
					"select ass from Asistencia as ass where ass.fichaDocente.fcdcId=:fdId and to_char( ass.assFecha,:format) =:hora");
			// and ass.assEstado <> :estado
			q.setParameter("fdId", fcdcId);
			q.setParameter("hora", hora);
			q.setParameter("format", format);
			// q.setParameter("estado", estado);
			asis = q.getResultList();

		} catch (Exception e) {
			System.out.println("No hay una asistencia gregistrada" + e);
			return asis = new ArrayList<>();
		}
		return asis;
	}

	@Override
	public void guardarRegistro(Asistencia regAss) {
		try {
			if (regAss.getAssId() != null) {
				System.out.println(regAss.getAssHoraSalida());
				em.merge(regAss);
			} else {
				System.out.println(regAss.getFichaDocente().getFcdcId());
				System.out.println(regAss.getAssHoraEntrada());
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
	public void generar(Date ahora) {
		List<Object> lstO = null;
		Horario hrr;
		FichaDocente fd = new FichaDocente();
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy/MM/dd");
		Date fecha = new Date(formateador.format(ahora));
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery(
					"select h,fd from Horario as h join h.fichaDocente as fd where h.diaSemana.dsmId=:diaId order by h.hrrInicio");
			query.setParameter("diaId", ahora.getDay());
			lstO = query.getResultList();
			for (Object obj : lstO) {
				objArray = (Object[]) obj;
				hrr = (Horario) objArray[0];
				fd = (FichaDocente) objArray[1];
				Asistencia ass = new Asistencia();
				ass.setAssFecha(fecha);
				ass.setHorario(hrr);
				ass.setFichaDocente(fd);
				guardarRegistro(ass);
			}
		} catch (Exception e) {
			System.out.println("Error al crear registro laboral docente");
		}
		System.out.println("Registro laboral docente creado");
	}

	@Override
	public void guardarSeguimiento(Contenido cnt) {
		try {
			if (cnt.getCntId() != null) {
				em.merge(cnt);
			} else {
				cnt.setCntId(obtenerCntdAsistencia() + 1);
				em.persist(cnt);
			}
		} catch (Exception e) {
			System.out.println("Error al guardar tema de clase");
		}

	}

	private int obtenerCntdAsistencia() {
		int id;
		try {
			Query query = em.createQuery("select cont.cntId from Contenido as cont order by cont.cntId desc");
			query.setMaxResults(1);
			id = (int) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar los contenidos secuencia" + e);
			return 0;
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
	public void guardarActualizarContenido(Contenido cnt) {
		try {
			if (cnt.getCntId() > 0) {
				em.merge(cnt);
			} else {
				int c = obtenerSecuenciaContenido();
				cnt.setCntId(c + 1);
				em.persist(cnt);

			}
		} catch (Exception e) {
			System.out.println("Error al guardarActualizarContenido: " + e);
		}

	}

	private int obtenerSecuenciaContenido() {
		int id;
		try {
			Query query = em.createQuery("select c.cntId from Contenido as c order by c.cntId desc");
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
					"select mcm, m, c, f, mc from MallaCurricularMateria as mcm join mcm.materia as m join m.carrera as c join c.facultad as f join mcm.mallaCurricular as mc where m.mtrId=:mtrId");
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
			if (syl.getSylId() > 0) {
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
			return 1;
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

}
