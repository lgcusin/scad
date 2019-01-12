package beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Actividad;

/**
 * Session Bean implementation class ActividadBean
 */
@Stateless
@LocalBean
public class ActividadBean implements ActividadBeanRemote, ActividadBeanLocal {

	@PersistenceContext(unitName = "UnidadPersistencia")
	private EntityManager em;

	
	public ActividadBean() {
		super(Actividad.class);
	}


	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

}
