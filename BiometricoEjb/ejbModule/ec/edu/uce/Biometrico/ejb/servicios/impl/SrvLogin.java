package ec.edu.uce.Biometrico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.Biometrico.ejb.servicios.interfaces.SrvLoginLocal;
import ec.edu.uce.biometrico.jpa.DetallePuesto;
import ec.edu.uce.biometrico.jpa.Usuario;
import ec.edu.uce.biometrico.jpa.UsuarioRol;


/**
 * Session Bean implementation class LoginBean
 */
@Stateless
@Local
public class SrvLogin implements SrvLoginLocal {

	@PersistenceContext
	private EntityManager em;

	public SrvLogin() {
	}

	@Override
	public UsuarioRol verificar(String nick, String clave) {
		UsuarioRol usr = new UsuarioRol();
		try {
			Object[] objArray;
			Query query;
			query = em.createQuery(
					"select usr, usu, p, r from UsuarioRol as usr join usr.usroUsuario as usu join usu.usrPersona as p "
							+ "join usr.usroRol as r where usu.usrNick=:nick and usu.usrPassword=:clave");
			query.setParameter("nick", nick).setParameter("clave", clave);
			Object obj = query.getSingleResult();
			objArray = (Object[]) obj;
			usr = (UsuarioRol) objArray[0];
		} catch (Exception e) {
			System.out.println("Error al obtener usuario" + e.getStackTrace());
			return usr;
		}
		return usr;
	}

	@Override
	public List<DetallePuesto> listarDetallePuestoDocente(Integer prsId) {
		List<DetallePuesto> lstdp = new ArrayList<>();
		try {
			Object[] objArray;
			Query query = em
					.createQuery("select dp,fd,p,cr,de,pu from DetallePuesto as dp join dp.dtpsFichaDocente as fd "
							+ "join fd.fcdcPersona as p join dp.dtpsCarrera as cr join cr.crrDependencia as de "
							+ "join dp.dtpsPuesto as pu where p.prsId=:fdId");
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
					"select dp,fe,p,cr,de,pu from DetallePuesto as dp join dp.dtpsFichaEmpleado as fe join fe.fcemPersona as p "
							+ "join dp.dtpsCarrera as cr join cr.crrDependencia as de join dp.dtpsPuesto as pu where p.prsId=:fdId");
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

	@Override
	public Usuario buscarPorNick(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UsuarioRol> buscarRolesActivoXUsuario(Integer usrId) {
		// TODO Auto-generated method stub
		return null;
	}

}
