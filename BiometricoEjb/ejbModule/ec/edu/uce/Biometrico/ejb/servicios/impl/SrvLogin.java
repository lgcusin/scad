package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvLoginLocal;
import ec.uce.edu.biometrico.jpa.DetallePuesto;
import ec.uce.edu.biometrico.jpa.Usuario;
import ec.uce.edu.biometrico.jpa.UsuarioRol;

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
	public UsuarioRol verificar(String nick, String clave) {
		UsuarioRol usr = new UsuarioRol();
		try {
			Object[] objArray;
			Query query;
			query = em.createQuery(
					"select usr, usu, p, r from UsuarioRol as usr join usr.usuario as usu join usu.persona as p join usr.rol as r where usu.usrNick=:nick and usu.usrPassword=:clave");
			query.setParameter("nick", nick).setParameter("clave", clave);
			Object obj = query.getSingleResult();
			objArray = (Object[]) obj;
			usr = (UsuarioRol) objArray[0];
		} catch (Exception e) {
			System.out.println("Error al obtener usuario" + e);
			return usr;
		}
		return usr;
	}

	@Override
	public List<DetallePuesto> listarDetallePuestoDocente(Integer prsId) {
		List<DetallePuesto> lstdp = new ArrayList<>();
		try {
			Object[] objArray;
			Query query = em.createQuery(
					"select dp,fd,p,cr,de,pu from DetallePuesto as dp join dp.fichaDocente as fd join fd.persona as p join dp.carrera as cr join cr.dependencia as de join dp.puesto as pu where p.prsId=:fdId");
			query.setParameter("fdId", prsId);
			for (Object object : query.getResultList()) {
				objArray = (Object[]) object;
				lstdp.add((DetallePuesto) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Erroral obtener detallePuestos de docente");
			return lstdp;
		}
		return lstdp;
	}

	@Override
	public List<DetallePuesto> listarDetallePuestoEmpleado(Integer prsId) {
		List<DetallePuesto> lstdp = new ArrayList<>();
		try {
			Object[] objArray;
			Query query = em.createQuery(
					"select dp,fe,p,cr,de,pu from DetallePuesto as dp join dp.fichaEmpleado as fe join fe.persona as p join dp.carrera as cr join cr.dependencia as de join dp.puesto as pu where p.prsId=:fdId");
			query.setParameter("fdId", prsId);
			for (Object object : query.getResultList()) {
				objArray = (Object[]) object;
				lstdp.add((DetallePuesto) objArray[0]);
			}
		} catch (Exception e) {
			System.out.println("Erroral obtener detallePuestos de empleado");
			return lstdp;
		}
		return lstdp;
	}

}
