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
import model.Carrera;
import model.Contenido;
import model.Herramienta;
import model.Horario;
import model.Materia;
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
		Syllabo syl = em.createNamedQuery("Syllabo.findByMtrId", Syllabo.class).setParameter("mtrId", mtrId)
				.getSingleResult();
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

	@SuppressWarnings("deprecation")
	@Override
	public Horario verificarHorario(Date fecha, Integer fcdcId, Boolean tipo) {
		final int parametro = 15;
		int horas = Integer.parseInt(fecha.getHours() + "");
		int minutos = Integer.parseInt(fecha.getMinutes() + "");
		int horasProceso = 0;
		int minutosProceso = 0;
		int holguraInicio = minutos - parametro;
		int holguraFin = minutos + parametro;
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
		List<Contenido> lstCn;
		try {
			lstCn = em.createNamedQuery("Contenido.findAllByMtrId", Contenido.class).setParameter("mtId", mtrId)
					.getResultList();
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
		String estado = "Finalizado";
		try {
			Query q = em.createQuery(
					"select ass from Asistencia as ass where ass.fichaDocente.fcdcId=:fdId and to_char( ass.assFecha,:format) =:hora and ass.assEstado <> :estado");
			q.setParameter("fdId", fcdcId);
			q.setParameter("hora", hora);
			q.setParameter("format", format);
			q.setParameter("estado", estado);
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
}
