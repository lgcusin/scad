/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción distribución no autorizada de este programa, 
 * o cualquier porción de  él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán  procesadas con el grado  máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     RolFlujoGrupoServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Rol_Flujo_Grupo. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 07-FEBRERO-2018           Arturo Villafuerte                    Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.RolFlujoGrupoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoGrupoNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolFlujoGrupoServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolFlujoCarreraConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoGrupo;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Clase (Bean)RolFlujoGrupoServicioImpl.
 * Bean declarado como Stateless.
 * @author ajvillafuerte
 * @version 1.0
 */

@Stateless
public class RolFlujoGrupoServicioImpl implements RolFlujoGrupoServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad RolFlujoGrupo por su id
	 * @param id - de la RolFlujoGrupo a buscar
	 * @return RolFlujoGrupo con el id solicitado
	 * @throws RolFlujoGrupoNoEncontradoException - Excepcion lanzada cuando no se encuentra una RolFlujoGrupo con el id solicitado
	 * @throws RolFlujoGrupoException - Excepcion general
	 */
	@Override
	public RolFlujoGrupo buscarPorId(Integer id) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException {
		RolFlujoGrupo retorno = null;
		if (id != null) {
			try {
				retorno = em.find(RolFlujoGrupo.class, id);
			} catch (NoResultException e) {
				throw new RolFlujoGrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoGrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RolFlujoGrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.id.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Lista todas las entidades RolFlujoGrupo existentes en la BD
	 * @return lista de todas las entidades RolFlujoGrupo existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoGrupo> listarTodos() throws RolFlujoGrupoNoEncontradoException{
		List<RolFlujoGrupo> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select roflgr from RolFlujoGrupo roflgr ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		
		if(retorno.size()<=0){
			throw new RolFlujoGrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.todos.no.result.exception")));
		}
		
		return retorno;
		
	}
	

	/**
	 * Busca una entidad RolFlujoGrupo por su id de carrera
	 * @param id - de la RolFlujoGrupo a buscar
	 * @return RolFlujoGrupo con el id solicitado
	 */
	@Override
	public RolFlujoGrupo buscarPorGrupo(Integer carrera, Integer usuarioId, Integer rolId) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException{
		RolFlujoGrupo retorno = null;
		if(carrera != null){
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflgr from RolFlujoGrupo roflgr ");
				sbsql.append(" where roflgr.roflgrGrupo.crrId = :carrera");
				sbsql.append(" and roflgr.roflgrUsuarioRol.usroRol.rolId = :rolId ");
				sbsql.append(" and roflgr.roflgrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("carrera", carrera );
				q.setParameter("rolId", rolId );
				q.setParameter("usuarioId", usuarioId );
				retorno = (RolFlujoGrupo)q.getSingleResult();
			} catch (NoResultException e) {
				throw new RolFlujoGrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.grupo.no.result.exception",carrera)));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoGrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.grupo.non.unique.result.exception",carrera)));
			} catch (Exception e) {
				throw new RolFlujoGrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.grupo.exception")));
			}
		}
		return retorno;
	}
	
	
	/**
	 * Busca una entidad RolFlujoGrupo por su id de usuario
	 * @param id - de la RolFlujoGrupo a buscar
	 * @return RolFlujoGrupo con el id solicitado
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoGrupo> buscarPorIdUsuario(Usuario usuario) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException{
		List<RolFlujoGrupo> retorno = null;
		if(usuario != null){
			try{
				retorno=new ArrayList<RolFlujoGrupo>();
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflgr from RolFlujoGrupo roflgr ");
				sbsql.append(" where roflgr.roflgrUsuarioRol.usroUsuario.usrId = :usuarioId");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("usuarioId", usuario.getUsrId() );
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new RolFlujoGrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.idUsuario.no.result.exception",usuario.getUsrId())));
			}catch (NonUniqueResultException e) {
				throw new RolFlujoGrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.idUsuario.non.unique.result.exception",usuario.getUsrId())));
			} catch (Exception e) {
				throw new RolFlujoGrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.por.idUsuario.exception")));
			}
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad RolFlujoGrupo por idUsuario y idRol 
	 * @param usuarioId - de Usuario a buscar
	 * @param rolId - de Rol a buscar
	 * @return RolFlujoGrupo solicitado 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RolFlujoGrupo> buscarXRolXUsuarioId(Integer usuarioId, Integer rolId) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException{
		List<RolFlujoGrupo> retorno = null;
		if(usuarioId != null && rolId != null){
			try{
				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select roflgr from RolFlujoGrupo roflgr ");
				sbsql.append(" where roflgr.roflgrUsuarioRol.usroRol.rolId = :rolId ");
				sbsql.append(" and roflgr.roflgrUsuarioRol.usroUsuario.usrId = :usuarioId ");
				sbsql.append(" and roflgr.roflgrEstado = :roflgrEstado ");
				Query q = em.createQuery(sbsql.toString()); 
				q.setParameter("rolId", rolId );
				q.setParameter("usuarioId", usuarioId );
				q.setParameter("roflgrEstado", RolFlujoCarreraConstantes.ESTADO_ACTIVO_VALUE );
				retorno = q.getResultList();
			} catch (NoResultException e) {
				throw new RolFlujoGrupoNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.rol.usuarioid.no.result.exception",usuarioId)));
			} catch (Exception e) {
				throw new RolFlujoGrupoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("RolFlujoGrupo.buscar.rol.usuarioid.exception")));
			}
		}
		return retorno;
	}
	

}
