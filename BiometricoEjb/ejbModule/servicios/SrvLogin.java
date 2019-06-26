package servicios;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.DetallePuesto;
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
		Usuario usr = new Usuario();
		try {
			Object[] objArray;
			Query query;
			query = em.createQuery(
					"select u,fe,fd from Usuario as u join u.fichaEmpleado as fe join u.fichaDocente as fd where u.usrNick=:nick and u.usrPassword=:clave");
			query.setParameter("nick", nick).setParameter("clave", clave);
			Object obj = query.getSingleResult();
			objArray = (Object[]) obj;
			usr = (Usuario) objArray[0];

		} catch (Exception e) {
			System.out.println("Error al obtener usuario" + e);
			return usr;
		}
		return usr;
	}

	@Override
	public List<DetallePuesto> buscarDetallePuesto(Integer fcdcId) {
		List<DetallePuesto> lstdp = new ArrayList<>();
		try {
			Object[] objArray;
			Query query;
			query = em.createQuery(
					"select dp,cr,fc from DetallePuesto as dp join dp.carrera as cr join cr.facultad as fc where dp.fichaDocente.fcdcId=:fdId");
			query.setParameter("fdId", fcdcId);
			for (Object object : query.getResultList()) {
				objArray = (Object[]) object;
				lstdp.add((DetallePuesto) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Erroral obtener carrera/facultad del usuario");
			return lstdp;
		}
		return lstdp;
	}

}
