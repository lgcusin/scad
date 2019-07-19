package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.rowset.serial.SerialException;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvDocenteLocal;
import ec.uce.edu.biometrico.jpa.Asistencia;
import ec.uce.edu.biometrico.jpa.ContenidoCurricular;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.HorarioAcademico;
import ec.uce.edu.biometrico.jpa.HuellaDactilar;
import ec.uce.edu.biometrico.jpa.Materia;
import ec.uce.edu.biometrico.jpa.Persona;
import ec.uce.edu.biometrico.jpa.Seguimiento;
import ec.uce.edu.biometrico.jpa.TipoHuella;

/**
 * Session Bean implementation class DocenteBean
 */
@Stateless
@LocalBean
public class SrvDocente implements SrvDocenteLocal {

	@PersistenceContext
	EntityManager em;

	public SrvDocente() {
		//
	}

	@Override
	public List<FichaDocente> listarDocentesxParametroxFacultad(String param, Integer fcId) {
		param = param.toUpperCase();
		param = "%" + param + "%";
		List<FichaDocente> lstD = new ArrayList<>();
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery(
					"select fd.fcdcId,p.prsId,p.prsPrimerApellido,p.prsSegundoApellido,p.prsNombres from DetallePuesto as dp join dp.fichaDocente as fd join fd.persona as p join dp.carrera as c join c.dependencia as d where (p.prsPrimerApellido LIKE :param or p.prsSegundoApellido LIKE :param or p.prsNombres LIKE :param) and d.dpnId=:fcId group by fd.fcdcId,p.prsId,p.prsPrimerApellido,p.prsSegundoApellido,p.prsNombres order by p.prsPrimerApellido, p.prsSegundoApellido");
			query.setParameter("param", param).setParameter("fcId", fcId);
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
	public List<TipoHuella> listarTipoHuellas() {
		return em.createNamedQuery("TipoHuella.listar", TipoHuella.class).getResultList();
	}

	@Override
	public List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException {
		List<HuellaDactilar> lsth = em.createNamedQuery("HuellaDactilar.findAllById", HuellaDactilar.class)
				.setParameter("idDcnt", fcdc_id).getResultList();
		List<BufferedImage> lstI = null;
		for (HuellaDactilar hldc : lsth) {
			lstI.add(toBufferedImage(hldc.getHldPrimerHuella()));
			lstI.add(toBufferedImage(hldc.getHldSegundaHuella()));
		}
		return lstI;
	}

	public BufferedImage toBufferedImage(Blob blb) throws SQLException, IOException {
		Blob blob = blb;
		InputStream in = blob.getBinaryStream();
		BufferedImage image = ImageIO.read(in);
		return image;
	}

	public HuellaDactilar findHuella(Integer fdId, Integer thid) {
		HuellaDactilar hldc;
		try {
			hldc = em.createNamedQuery("HuellaDactilar.findByFdicThid", HuellaDactilar.class).setParameter("fdId", fdId)
					.setParameter("thid", thid).getSingleResult();

		} catch (Exception e) {
			System.out.println(e);
			return hldc = new HuellaDactilar();
		}
		return hldc;
	}

	@Override
	public void guardarImagen(BufferedImage bimg1, BufferedImage bimg2, FichaDocente fcdc, TipoHuella tphl)
			throws IOException, SerialException, SQLException {
		HuellaDactilar hldc;
		hldc = findHuella(fcdc.getFcdcId(), tphl.getTphlId());
		if (hldc != null) {
			for (int i = 0; i < 2; i++) {
				ByteArrayOutputStream baos = null;
				try {
					baos = new ByteArrayOutputStream();
					if (i == 0) {
						ImageIO.write(bimg1, "png", baos);
					}
					ImageIO.write(bimg2, "png", baos);
				} finally {
					try {
						baos.close();
					} catch (Exception e) {
					}
				}
				Blob huella = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
				if (i == 0) {
					hldc.setHldPrimerHuella(huella);
				}
				hldc.setHldSegundaHuella(huella);
			}
			hldc.setFichaDocente(fcdc);
			hldc.setTipoHuella(tphl);
			em.merge(hldc);
		} else {
			hldc = new HuellaDactilar();
			for (int i = 0; i < 2; i++) {
				ByteArrayOutputStream baos = null;
				try {
					baos = new ByteArrayOutputStream();
					if (i == 0) {
						ImageIO.write(bimg1, "png", baos);
					}
					ImageIO.write(bimg2, "png", baos);
				} finally {
					try {
						baos.close();
					} catch (Exception e) {
					}
				}
				Blob huella = new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
				if (i == 0) {
					hldc.setHldPrimerHuella(huella);
				}
				hldc.setHldSegundaHuella(huella);
			}
			hldc.setFichaDocente(fcdc);
			hldc.setTipoHuella(tphl);
			em.persist(hldc);
		}
	}

	@Override
	public List<Asistencia> listarAsistencia(Integer fdId, Date inicio, Date fin, Integer crrId) {
		List<Asistencia> listAs = new ArrayList<>();
		try {
			Object[] objArray;
			Query query;
			if (crrId != null) {
				query = em.createQuery(
						"select a,h,mcp,mcm,m,crr from Asistencia as a join a.horarioAcademico as h join h.mallaCurricularParalelo as mcp join mcp.mallaCurricularMateria as mcm join mcm.materia as m join m.carrera as crr"
								+ " where a.fichaDocente.fcdcId=:fcdcId and a.assFecha >= :fechaInicio and a.assFecha <= :fechaFin and crr.crrId=:crrId"
								+ " order by a.assFecha asc");
				query.setParameter("crrId", crrId);
			} else {
				query = em.createQuery(
						"select a, h,mcp, mcm, m from Asistencia as a join a.horarioAcademico as h join h.mallaCurricularParalelo as mcp join mcp.mallaCurricularMateria as mcm join mcm.materia as m where a.fichaDocente.fcdcId=:fcdcId and a.assFecha >= :fechaInicio and a.assFecha <= :fechaFin order by a.assFecha asc");
			}
			query.setParameter("fcdcId", fdId);
			query.setParameter("fechaInicio", inicio);
			query.setParameter("fechaFin", fin);
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				listAs.add((Asistencia) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
		}
		return listAs;
	}

	@Override
	public List<ContenidoCurricular> listarContenidos(Integer fdId) {
		List<ContenidoCurricular> lstCn = new ArrayList<>();
		try {
			lstCn = em.createNamedQuery("Contenido.findAllByFdId", ContenidoCurricular.class).setParameter("fdId", fdId)
					.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return lstCn;
		}
		return lstCn;
	}

	@Override
	public List<String> listarActividades(Integer fdId) {
		List<String> lstAc = em.createNamedQuery("Actividad.findByFdId", String.class).setParameter("fcdcId", fdId)
				.getResultList();
		return lstAc;
	}

	/**
	 * Permite buscar el horario asignado a la asistencia a justificar
	 * 
	 * @param assId
	 * @return
	 */
	@Override
	public HorarioAcademico findHorarioByAsistencia(Asistencia asis) {
		HorarioAcademico horario = new HorarioAcademico();
		try {
			Object[] arrayObj;
			List<HorarioAcademico> lstH = new ArrayList<>();
			Query query = em.createQuery(
					"select h, h.horaClaseAula.horaClase from HorarioAcademico as h where h.mallaCurricularParalelo.mlcrprId=:mlcrpr and h.hracDia=:dia");
			query.setParameter("mlcrpr", asis.getHorarioAcademico().getMallaCurricularParalelo().getMlcrprId())
					.setParameter("dia", asis.getAssFecha().getDay() - 1);
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstH.add((HorarioAcademico) arrayObj[0]);
			}
			horario = lstH.get(0);
			// horario.setHracHoraInicio(lstH.get(0).getHracHoraInicio());
			horario.setHracHoraFin(lstH.get(lstH.size() - 1).getHracHoraFin());
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
			return horario;
		}
		return horario;
	}

	/**
	 * Actualiza la asistencia justificada
	 * 
	 * @param asistencia
	 */
	@Override
	public void actualizarAsistencia(Asistencia asistencia) {
		if (asistencia.getAssId() != null) {
			em.merge(asistencia);
		}
	}

	@Override
	public List<Materia> listarMaterias(Integer crrId) {
		List<Materia> lstM = new ArrayList<>();
		try {
			Query query = em.createQuery("select mtr from Materia as mtr where mtr.carrera.crrId=:crrId");
			query.setParameter("crrId", crrId);
			lstM = query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar materias por carrera");
			return lstM;
		}
		return lstM;
	}

	@Override
	public List<Seguimiento> listarSeguimientos(Integer fcdcId, Integer mtrId) {
		List<Seguimiento> lstS = new ArrayList<>();
		try {
			Object[] objArray;
			Query query = em.createQuery(
					"select sgm, mt , ass, fd from Seguimiento as sgm join sgm.mallaCurricularMateria.materia as mt join sgm.asistencia as ass join ass.fichaDocente as fd where fd.fcdcId=:fcdcId and mt.mtrId=:mtrId ");
			query.setParameter("fcdcId", fcdcId).setParameter("mtrId", mtrId);
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

	@Override
	public void guardarActualizarEstados(FichaDocente selectDcnt, TipoHuella selectTp, boolean flagMovil,
			boolean flagSinHuella) {
		HuellaDactilar hd = new HuellaDactilar();
		hd = findHuella(selectDcnt.getFcdcId(), selectTp.getTphlId());
		if (hd.getHldcId() == null) {
			hd.setFichaDocente(selectDcnt);
			hd.setTipoHuella(selectTp);
			if (selectTp.getTphlId() == 5 && flagSinHuella) {
				hd.setHldcCodigoAuxiliar(1);
			} else if (selectTp.getTphlId() == 5 && !flagSinHuella) {
				hd.setHldcCodigoAuxiliar(0);
			}
			em.persist(hd);
		} else {
			if (selectTp.getTphlId() == 5 && flagSinHuella) {
				hd.setHldcCodigoAuxiliar(1);
			} else if (selectTp.getTphlId() == 5 && !flagSinHuella) {
				hd.setHldcCodigoAuxiliar(0);
			} else if (selectTp.getTphlId() == 4 && !flagMovil) {
				hd.setTipoHuella(em.find(TipoHuella.class, 0));
			}
			em.merge(hd);
		}
	}

	@Override
	public List<Boolean> listarestados(Integer fcdcId) {
		List<Boolean> lstE = new ArrayList<>(2);
		lstE.add(0, false);
		lstE.add(1, false);
		List<HuellaDactilar> lstHD = new ArrayList<>();
		try {
			Object[] arrayObj;
			Query query = em
					.createQuery(
							"select hd, th from HuellaDactilar as hd join hd.tipoHuella as th where hd.fichaDocente.fcdcId=:fcdcId and hd.tipoHuella.tphlId not in (1,2,3)")
					.setParameter("fcdcId", fcdcId);
			for (Object obj : query.getResultList()) {
				arrayObj = (Object[]) obj;
				lstHD.add((HuellaDactilar) arrayObj[0]);
			}
			for (HuellaDactilar hd : lstHD) {
				if (hd.getHldcCodigoAuxiliar() == 1) {
					lstE.add(0, true);
				} else if (hd.getTipoHuella().getTphlId() == 4) {
					lstE.add(1, true);
				} else if (hd.getHldcCodigoAuxiliar() != 1) {
					lstE.add(0, false);
				} else if (hd.getTipoHuella().getTphlId() == 0) {
					lstE.add(1, false);
				}
			}

		} catch (Exception e) {
			System.out.println("Erros obtener estados docente");
			return lstE;
		}
		return lstE;
	}
}
