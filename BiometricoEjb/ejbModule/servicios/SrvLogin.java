package servicios;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import model.FichaDocente;
import model.FichaEmpleado;
import model.Usuario;

/**
 * Session Bean implementation class LoginBean
 */
@Stateless
@LocalBean
public class SrvLogin implements SrvLoginLocal {

	@PersistenceContext
	EntityManager em;
	public SrvLogin() {
	}

	@Override
	public Usuario verificar(String nick, String clave) {
		Usuario usr;
		try {
			usr = em.createNamedQuery("Usuario.finduser", Usuario.class).setParameter("nick", nick)
					.setParameter("clave", clave).getSingleResult();

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		usr.setFichaDocente(getDocente(usr.getUrsId()));
		usr.setFichaEmpleado(getEmpleado(usr.getUrsId()));
		return usr;
	}

	@Override
	public FichaDocente getDocente(Integer usrId) {
		FichaDocente fd;
		try {
			fd = em.createNamedQuery("Docente.findByUsrId", FichaDocente.class).setParameter("usrId", usrId)
					.getSingleResult();
		} catch (Exception e) {
			System.out.println(e);
			return fd = null;
		}
		return fd;
	}

	@Override
	public FichaEmpleado getEmpleado(Integer usrId) {
		FichaEmpleado fe;
		try {
			fe = em.createNamedQuery("Empleado.findByUsrId", FichaEmpleado.class).setParameter("usrId", usrId)
					.getSingleResult();
		} catch (Exception e) {
			System.out.println(e);
			return fe = null;
		}
		return fe;
	}

}
