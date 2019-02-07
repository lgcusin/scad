package servicios;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Usuario;

/**
 * Session Bean implementation class LoginBean
 */
@Stateless
@LocalBean
public class LoginBean implements LoginBeanLocal {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
	private EntityManager em = emf.createEntityManager();

	public LoginBean() {
	}

	@Override
	public Usuario verificar(String nick, String clave) {

		Usuario usr = em.createNamedQuery("Usuario.finduser", Usuario.class).setParameter("nick", nick)
				.setParameter("clave", clave).getSingleResult();
		if (usr.getUrsId() == null) {
			return null;
		} else
			return usr;
	}

}
