package beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.FichaDocente;

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
	public void guardar(FichaDocente fcdc) {
		
	}
    
    

}