package beans;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import model.FichaDocente;
import model.HuellaDactilar;
import model.TipoHuella;
import model.Usuario;

/**
 * Session Bean implementation class DocenteBean
 */
@Stateless
@LocalBean
public class DocenteBean implements DocenteBeanLocal {

	public DocenteBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<FichaDocente> listar(String param) {
		param = "%" + param + "%";
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
		return em.createNamedQuery("Docente.listar", FichaDocente.class).setParameter("apellido", param)
				.setParameter("primernombre", param).setParameter("segundonombre", param).getResultList();

	}

	@Override
	public List<TipoHuella> listarDedos() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
		return em.createNamedQuery("TipoHuella.listar", TipoHuella.class).getResultList();
	}

	@Override
	public List<BufferedImage> listarHuellas(Integer fcdc_id) throws SQLException, IOException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
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
		byte[] imagebytes = blb.getBytes(0, (int) blb.length());

		// convert byte-Array into Buffered Image (Subclass of Image)
		BufferedImage theImage = ImageIO.read(new ByteArrayInputStream(imagebytes));
		return theImage;
	}

	@Override
	public FichaDocente getDocente(Integer id) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
		return em.find(FichaDocente.class, id);
	}

}
