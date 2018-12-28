package servicios;

import java.util.List;

import javax.annotation.PostConstruct;
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
public class DocenteBean implements DocenteBeanIn {
	
	
	@PersistenceContext
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public DocenteBean() {
        // TODO Auto-generated constructor stub
    	
    }

	@Override
	public List<FichaDocente> listar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void crear(FichaDocente fcdc) {
		// TODO Auto-generated method stub
		
	}

}
