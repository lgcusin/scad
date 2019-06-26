package servicios;

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

import model.Asistencia;
import model.Contenido;
import model.DetallePuesto;
import model.FichaDocente;
import model.Horario;
import model.HuellaDactilar;
import model.TipoHuella;

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
	public List<FichaDocente> listar(String param, Integer fcId) {
		param = "%" + param + "%";
		List<FichaDocente> lstD = new ArrayList<>();
		try {
			Query query;
			Object[] objArray;
			query = em.createQuery(
					"select fcd, dt, fc from DetallePuesto as dt join dt.carrera.facultad as fc join dt.fichaDocente as fcd "
							+ "where (fcd.fcdcApellidos LIKE :param or fcd.fcdcPrimerNombre LIKE :param or fcd.fcdcSegundoNombre LIKE :param)"
							+ " and fc.fclId=:fcId");
			query.setParameter("param", param).setParameter("fcId", fcId);
			for (Object obj : query.getResultList()) {
				objArray = (Object[]) obj;
				lstD.add((FichaDocente) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Error al consultar docentes por facultad");
			return lstD;
		}
		return lstD;

	}

	@Override
	public List<TipoHuella> listarDedos() {
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Asistencia> listarAsistencia(Integer fdId, Date inicio, Date fin, Integer crrId) {
		List<Asistencia> listAs = null;
		try {
			Query query;
			if (crrId != null) {
				query = em.createQuery(
						"select a from Asistencia as a join a.horario as h join h.materia as m join m.carrera as crr"
								+ " where a.fichaDocente.fcdcId=:fcdcId and a.assFecha >= :fechaInicio and a.assFecha <= :fechaFin and crr.crrId=:crrId"
								+ " order by a.assFecha asc");
				query.setParameter("crrId", crrId);
			} else {
				query = em.createQuery(
						"select a from Asistencia as a where a.fichaDocente.fcdcId=:fcdcId and a.assFecha >= :fechaInicio and a.assFecha <= :fechaFin order by a.assFecha asc");
			}
			query.setParameter("fcdcId", fdId);
			query.setParameter("fechaInicio", inicio);
			query.setParameter("fechaFin", fin);
			listAs = (List<Asistencia>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
		}
		return listAs;
	}

	@Override
	public List<Contenido> listarContenidos(Integer fdId) {
		List<Contenido> lstCn;
		try {
			lstCn = em.createNamedQuery("Contenido.findAllByFdId", Contenido.class).setParameter("fdId", fdId)
					.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return lstCn = new ArrayList<>();
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
	public Horario findHorarioByAsistencia(Integer assId) {
		Horario horario = null;
		try {
			Query query = em.createQuery("select h from Asistencia as a join a.horario as h where a.assId=:assId");
			query.setParameter("assId", assId);
			horario = (Horario) query.getSingleResult();
		} catch (Exception e) {
			System.out.println("Error al consultar Feriados: " + e);
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
}
