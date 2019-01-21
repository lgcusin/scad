package beans;

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

	public LoginBean() {
	}

	@Override
	public Usuario verificar(Usuario u) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
		EntityManager em = emf.createEntityManager();
		Usuario usr = em.createNamedQuery("Usuario.finduser", Usuario.class).setParameter("nick", u.getUsrNick())
				.setParameter("clave", u.getUsrPassword()).getSingleResult();
		if (usr.getUrsId() == null) {
			return null;
		} else
			return usr;
	}

}
