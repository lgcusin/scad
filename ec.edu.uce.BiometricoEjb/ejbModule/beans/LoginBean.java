package beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import model.Usuario;

/**
 * Session Bean implementation class LoginBean
 */
@Stateless
@LocalBean
public class LoginBean implements LoginBeanLocal {

	// @PersistenceContext(unitName="UnidadPersistencia")
	// public EntityManager em;

	public LoginBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean verificar(String nick, String clave) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
		String CI= em.createNamedQuery("Usuario.finduser", String.class).setParameter("nick", nick)
				.setParameter("clave", clave).getSingleResult();
		if (CI == null) {
			return false;
		} else
			return true;
	}

}
