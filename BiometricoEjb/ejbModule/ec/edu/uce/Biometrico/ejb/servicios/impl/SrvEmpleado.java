package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvEmpleadoLocal;
import ec.uce.edu.biometrico.jpa.Carrera;
import ec.uce.edu.biometrico.jpa.FichaDocente;
import ec.uce.edu.biometrico.jpa.UnidadCurricular;

/**
 * Session Bean implementation class SrvEmpleado
 */
@Stateless
@LocalBean
public class SrvEmpleado implements SrvEmpleadoLocal {

	@PersistenceContext
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public SrvEmpleado() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Carrera> listarCarreras(Integer fcId) {
		List<Carrera> lstC = new ArrayList<>();
		try {
			Query query= em.createQuery("select c from Carrera as c where c.dependencia.dpnId=:fcId").setParameter("fcId", fcId);
			lstC= query.getResultList();
		} catch (Exception e) {
			System.out.println(e);
			return lstC;
		}

		return lstC;
	}

	@Override
	public List<FichaDocente> listarDocentes(Integer crrId) {
		List<FichaDocente> lstD = new ArrayList<>();
		try {
			Object[] arraObj;
			Query query = em.createQuery("select fd,dp,p from DetallePuesto as dp join dp.fichaDocente as fd join fd.persona as p where dp.carrera.crrId=:crId order by p desc").setParameter("crId", crrId);
			for(Object obj : query.getResultList()){
				arraObj= (Object[]) obj;
				lstD.add((FichaDocente) arraObj[0]);
			}
		} catch (Exception e) {
			System.out.println(e);
			return lstD;
		}
		return lstD;
	}

	@Override
	public void create(UnidadCurricular uc) {
		// TODO Auto-generated method stub

	}

}
