package servicios;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Prueba;

/**
 * Session Bean implementation class pruebabean
 */
@Stateless
@LocalBean
public class pruebabean implements pruebabeanInter {

	
	@PersistenceContext
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public pruebabean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public Prueba listar(Integer id) {
		// TODO Auto-generated method stub
		Prueba p= em.find(Prueba.class, id);
		return p;
	}

}
