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

 ARCHIVO:     UsuarioRolServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla UsuarioRol. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.UsuarioRolServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.UsuarioRolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;

/**
 * Clase (Bean)UsuarioRolServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class UsuarioRolServicioImpl implements UsuarioRolServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;
	@Resource(mappedName=GeneralesConstantes.APP_DATA_SURCE)
	DataSource ds;
	/**
	 * Busca una entidad UsuarioRol por su id
	 * @param id - del UsuarioRol a buscar
	 * @return UsuarioRol con el id solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	@Override
	public UsuarioRol buscarPorId(Integer id) throws UsuarioRolNoEncontradoException, UsuarioRolException {
		UsuarioRol retorno = null;
		if (id != null) {
			try {
				retorno = em.find(UsuarioRol.class, id);
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.id.exception")));
			}
		}
		return retorno;
	}

	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioRol> listarTodos() {
		List<UsuarioRol> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select usrl from UsuarioRol usrl ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;

	}

	/**
	 * Lista todos los roles de los usuarios existentes en la BD
	 * @param usrId -  usrId id del usuario a consultar
	 * @return - retorna la lista de todos los roles de los usuarios existentes en la BD
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Excepcion lanzada cuando no se encontraros usuario rol con los parametros ingresados
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioRol> buscarXUsuario(int usrId) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		List<UsuarioRol> retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
			sbSql.append(" usrl.usroUsuario.usrId =:usrId ");

			Query q = em.createQuery(sbSql.toString());
			q.setParameter("usrId",usrId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.no.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.exception")));
		}
		return retorno;
	}

	/**
	 * Lista todos los roles de los usuarios existentes en la BD
	 * @param usrId -  usrId id del usuario a consultar
	 * @return - retorna la lista de todos los roles de los usuarios existentes en la BD
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Excepcion lanzada cuando no se encontraros usuario rol con los parametros ingresados
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UsuarioRol> buscarRolesActivoXUsuario(int usrId) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		List<UsuarioRol> retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
			sbSql.append(" usrl.usroUsuario.usrId =:usrId ");
			sbSql.append(" and usrl.usroEstado=");sbSql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("usrId",usrId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.no.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca el usuario rol por id de usuario
	 * @param idUsuario -  idUsuario id del usuario a consultar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepcion general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolException Excepción lanzada cuando no hay resultado en la consulta
	 */
	public UsuarioRol buscarEvaluadorXUsuario(int idUsuario) throws UsuarioRolNoEncontradoException, UsuarioRolException {
		UsuarioRol retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
			sbSql.append(" usrl.usroEstado =:estado ");
			sbSql.append(" and usrl.usroUsuario.usrId =:idUsuario ");

			Query q = em.createQuery(sbSql.toString());
			q.setParameter("estado",UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idUsuario",idUsuario);
			retorno = (UsuarioRol) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idUsuario.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idUsuario.non.unique.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idUsuario.exception")));
		}
		return retorno;
	}

	/**
	 * Busca el usuario rol por id de usuario
	 * @param idUsuario -  idUsuario id del usuario a consultar
	 * @param idRol -  idRol id del rol que quiere buscar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Exepción lanzada cuando no encuentra usuario rol con los parametros indicados
	 */
	public UsuarioRol buscarXUsuarioXrol(int idUsuario, int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException{
		UsuarioRol retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
//			sbSql.append(" usrl.usroEstado =:estado ");
			sbSql.append(" usrl.usroUsuario.usrId =:idUsuario ");
			sbSql.append(" and usrl.usroRol.rolId =:idRol ");
			Query q = em.createQuery(sbSql.toString());
//			q.setParameter("estado",UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idUsuario",idUsuario);
			q.setParameter("idRol",idRol);
			retorno = (UsuarioRol) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.non.unique.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.exception")));
		}
		return retorno;
	}

	/**
	 * Busca el usuario rol por id de usuario
	 * @param idUsuario -  idUsuario id del usuario a consultar
	 * @param idRol -  idRol id del rol que quiere buscar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Exepción lanzada cuando no encuentra usuario rol con los parametros indicados
	 */
	@Override
	public UsuarioRol buscarXUsuarioXrolActivo(int idUsuario, int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException{
		UsuarioRol retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
			sbSql.append(" usrl.usroEstado =:estado ");
			sbSql.append(" and usrl.usroUsuario.usrId =:idUsuario ");
			sbSql.append(" and usrl.usroRol.rolId =:idRol ");
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("estado",UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idUsuario",idUsuario);
			q.setParameter("idRol",idRol);
			retorno = (UsuarioRol) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.non.unique.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.rol.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca el usuario_rol por rol que tenga ese usuario
	 * @param idRol -  idRol id del rol que quiere buscar
	 * @return UsuarioRol - la entidad usuario rol que se busca
	 * @throws UsuarioRolException - lanzada cuando exite un error desonocido o el resultado no es unico
	 * @throws UsuarioRolNoEncontradoException - lanzada cuando no se encuentra la entidad buscada
	 */
	public UsuarioRol buscarXrol(int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException{
		UsuarioRol retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
			sbSql.append(" usrl.usroEstado =:estado ");
			sbSql.append(" and usrl.usroRol.rolId =:idRol ");

			Query q = em.createQuery(sbSql.toString());
			q.setParameter("estado",UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idRol",idRol);
			retorno = (UsuarioRol) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idRol.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idRol.non.unique.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idRol.exception")));
		}
		return retorno;
	}

	/**
	 * Lista todos los roles del usuario enviado en la BD
	 * @param usrId -  usrId id del usuario a consultar
	 * @return - retorna la lista de todos los roles de los usuarios existentes en la BD
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Excepcion lanzada cuando no se encontraros usuario rol con los parametros ingresados
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> buscarRolesXUsuario(int usrId) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		List<Rol> retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select distinct (rol) from Rol rol, UsuarioRol usro   ");
			sbSql.append(" join fetch rol.rolUsuarioRols usro ");
			sbSql.append(" where usro.usroUsuario.usrId =:usrId ");
			Query q = em.createQuery(sbSql.toString());
			q.setParameter("usrId",usrId);
			retorno = q.getResultList();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.no.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.usuario.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca el usuario rol por id de persona
	 * @param idPersona -  idPersona id del usuario a consultar
	 * @param idRol -  idRol id del rol que quiere buscar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Exepción lanzada cuando no encuentra usuario rol con los parametros indicados
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public UsuarioRol buscarXPersonaXrol(int idPersona, int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException{
		UsuarioRol retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
//			sbSql.append(" usrl.usroEstado =:estado ");
//			sbSql.append(" and usrl.usroUsuario.usrPersona.prsId =:idPersona ");
			sbSql.append(" usrl.usroUsuario.usrPersona.prsId =:idPersona ");
			sbSql.append(" and usrl.usroRol.rolId =:idRol ");
			Query q = em.createQuery(sbSql.toString());
//			q.setParameter("estado",UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idPersona",idPersona);
			q.setParameter("idRol",idRol);
			retorno = (UsuarioRol) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.persona.rol.no.result.exception"))+idPersona);
		}catch (NonUniqueResultException e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.persona.rol.non.unique.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.persona.rol.exception")));
		}
		return retorno;
	}
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion  de SOPORTE
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	@Override
	public UsuarioRol buscarPorIdentificacionSoporte(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				sbsql.append(" and usro.usroRol.rolId = :rolSoporte ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				q.setParameter("rolSoporte", RolConstantes.ROL_SOPORTE_VALUE);
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
	}
	
	@Override
	public void desactivarUsuarioRolXid(Integer usroId, Integer usrId)
			throws  UsuarioRolException {
		if( usrId!=null){
			PreparedStatement pstmt = null;
			PreparedStatement pstmt1 = null;
			Connection con = null;
			ResultSet rs = null;
			try {
				StringBuilder sbsql = new StringBuilder();
				sbsql.append(" Update Usuario set usrEstado = ");sbsql.append(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
				sbsql.append(" where usrId = :usrId ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("usrId", usrId );
				q.executeUpdate();
				
				con = ds.getConnection();
				sbsql = new StringBuilder();
				sbsql.append(" Update rol_flujo_carrera set roflcr_estado = ");sbsql.append(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
				sbsql.append(" where roflcr_id in ( ");
				sbsql.append(" select roflcr_id from usuario usr, usuario_rol usro, rol_flujo_carrera roflcr ");
				sbsql.append(" where usr.usr_id=usro.usr_id ");
				sbsql.append(" and usro.usro_id=roflcr.usro_id ");
				sbsql.append(" and usr.usr_id = ");sbsql.append(usrId);sbsql.append(" )");
				pstmt = con.prepareStatement(sbsql.toString());
				pstmt.executeUpdate();
				
				sbsql = new StringBuilder();
				sbsql.append(" Update usuario_rol set usro_estado = ");sbsql.append(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
				sbsql.append(" where usro_id in ( ");
				sbsql.append(" select usro_id from usuario usr, usuario_rol usro ");
				sbsql.append(" where usr.usr_id=usro.usr_id ");
				sbsql.append(" and usr.usr_id = ");sbsql.append(usrId);sbsql.append(" )");
				
				pstmt1 = con.prepareStatement(sbsql.toString());
				pstmt1.executeUpdate();
				
				
			} catch (Exception e) {
				throw new UsuarioRolException("Error al desactivar el usuario.");
			}finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (pstmt1 != null) {
						pstmt1.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					}	
			}
			
		}
	}
	
	public void desactivarUsuarioRol(Integer usroId)throws  UsuarioRolException {
		if( usroId !=null){
			PreparedStatement pstmt = null;
			PreparedStatement pstmt1 = null;
			Connection con = null;
			ResultSet rs = null;
			try {
				StringBuilder sbsql = new StringBuilder();
				
				con = ds.getConnection();
				sbsql = new StringBuilder();
								
				sbsql.append(" Update usuario_rol set usro_estado = ");sbsql.append(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
				sbsql.append(" where usro_id = ");sbsql.append(usroId);
				
				pstmt = con.prepareStatement(sbsql.toString());
				pstmt.executeUpdate();
				
				sbsql = new StringBuilder();
				sbsql.append(" Update rol_flujo_carrera set roflcr_estado = ");sbsql.append(UsuarioRolConstantes.ESTADO_INACTIVO_VALUE);
				sbsql.append(" where roflcr_id in ( ");
				sbsql.append(" select roflcr_id from usuario_rol usro, rol_flujo_carrera roflcr ");
				sbsql.append(" where  ");
				sbsql.append(" usro.usro_id=roflcr.usro_id ");
				sbsql.append(" and usro.usro_id = ");sbsql.append(usroId);sbsql.append(" )");
				pstmt1 = con.prepareStatement(sbsql.toString());
				pstmt1.executeUpdate();
				
				
			} catch (Exception e) {
				throw new UsuarioRolException("Error al desactivar el usuario rol.");
			}finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
					if (pstmt1 != null) {
						pstmt1.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					}	
			}
			
		}
	}
	
	@Override
	public void activarUsuarioRolXid(Integer usroId, Integer usrId)
			throws  UsuarioRolException {
		if( usrId!=null){
			try {
				StringBuilder sbsql = new StringBuilder();
				sbsql.append(" Update Usuario set usrEstado = ");sbsql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				sbsql.append(" where usrId = :usrId ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("usrId", usrId );
				q.executeUpdate();
				
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
			
		}
	}
	
	public void activarUsuarioRol(Integer usroId)throws  UsuarioRolException {
		if( usroId!=null){
			try {
				StringBuilder sbsql = new StringBuilder();
				sbsql.append(" Update UsuarioRol set usroEstado = ");sbsql.append(UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
				sbsql.append(" where usroId = :usroId ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("usroId", usroId );
				q.executeUpdate();
				
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
			
		}
	}
	
	/**
	 * Busca una entidad UsuarioRol por su identificacion de validador
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	@Override
	public UsuarioRol buscarPorIdentificacion(String identificacion) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
		
		
	}
	
	@Override
	public UsuarioRol buscarPorIdentificacionPorRol(String identificacion, int rolId) throws UsuarioRolNoEncontradoException, UsuarioRolException{
		UsuarioRol retorno = null;
		if(identificacion != null){
			try{

				StringBuffer sbsql = new StringBuffer();
				sbsql.append(" Select usro from UsuarioRol usro ");
				sbsql.append(" where usro.usroUsuario.usrIdentificacion = :identificacion ");
				sbsql.append(" and usro.usroRol.rolId = :rolId ");
				Query q = em.createQuery(sbsql.toString());
				q.setParameter("identificacion", identificacion );
				q.setParameter("rolId", rolId );
				retorno = (UsuarioRol)q.getSingleResult();
				
			} catch (NoResultException e) {
				throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.no.result.exception",identificacion)));
			}catch (NonUniqueResultException e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.non.unique.result.exception",identificacion)));
			} catch (Exception e) {
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
		}
		return retorno;
		
		
	}
	
	
	/**
	 * Busca el usuario rol por id de usuario
	 * @param idUsuario -  idUsuario id del usuario a consultar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepcion general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolException Excepción lanzada cuando no hay resultado en la consulta
	 */
	public UsuarioRol buscarXUsuarioId(int idUsuario) throws UsuarioRolNoEncontradoException, UsuarioRolException {
		UsuarioRol retorno = null;
		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append(" Select usrl from UsuarioRol usrl where ");
			sbSql.append(" usrl.usroEstado =:estado ");
			sbSql.append(" and usrl.usroUsuario.usrId =:idUsuario ");

			Query q = em.createQuery(sbSql.toString());
			q.setParameter("estado",UsuarioRolConstantes.ESTADO_ACTIVO_VALUE);
			q.setParameter("idUsuario",idUsuario);
			retorno = (UsuarioRol) q.getSingleResult();
		} catch (NoResultException e) {
			throw new UsuarioRolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idUsuario.no.result.exception")));
		}catch (NonUniqueResultException e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idUsuario.non.unique.result.exception")));
		} catch (Exception e) {
			throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.idUsuario.exception")));
		}
		return retorno;
	}
	
	@Override
	public void regresarNivelacionUsuarioRol(Integer usroId)throws  UsuarioRolException {
		if( usroId!=null){
			try {
				UsuarioRol usroAux = em.find(UsuarioRol.class, usroId);
				Rol rolAux = em.find(Rol.class, RolConstantes.ROL_ESTUD_VALUE);
				usroAux.setUsroRol(rolAux);
				em.merge(usroAux);
				em.flush();
			} catch (Exception e) {
				e.printStackTrace();
				throw new UsuarioRolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("UsuarioRol.buscar.por.identificacion.exception")));
			}
			
		}
	}
	
}
