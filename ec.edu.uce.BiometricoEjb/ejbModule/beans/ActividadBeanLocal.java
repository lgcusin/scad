package beans;

import javax.ejb.Local;
import javax.persistence.EntityManager;

@Local
public interface ActividadBeanLocal {

	EntityManager getEntityManager();

}
