package servicios;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.sql.rowset.serial.SerialException;

import model.Actividad;
import model.Asistencia;
import model.Contenido;
import model.FichaDocente;
import model.HuellaDactilar;
import model.TipoHuella;
import model.Usuario;

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
	public List<FichaDocente> listar(String param) {
		param = "%" + param + "%";
		return em.createNamedQuery("Docente.listar", FichaDocente.class).setParameter("apellido", param)
				.setParameter("primernombre", param).setParameter("segundonombre", param).getResultList();

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

	@Override
	public void guardarImagen(BufferedImage bimg1, BufferedImage bimg2, FichaDocente fcdc, TipoHuella tphl)
			throws IOException, SerialException, SQLException {
		HuellaDactilar hldc = new HuellaDactilar();
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

	@Override
	public List<Asistencia> listarAsistencia(Integer id) {
		List<Asistencia> listAs = em.createNamedQuery("Asistencia.findAll", Asistencia.class).setParameter("fcdcId", id)
				.getResultList();
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
	public List<String> listarActividades(Integer id) {
		List<String> lstAc = em.createNamedQuery("Actividad.findByFdId", String.class).setParameter("fcdcId", id)
				.getResultList();
		return lstAc;
	}

	
}
