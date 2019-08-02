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

 ARCHIVO:     RolServicioImpl.java      
 DESCRIPCION: Bean sin estado encargado de gestionar las operaciones sobre la tabla Rol. 
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017           Dennis Collaguazo                   Emisión Inicial
 ***************************************************************************/

package ec.edu.uce.academico.ejb.servicios.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.ejb.servicios.interfaces.RolServicio;
import ec.edu.uce.academico.ejb.utilidades.constantes.GeneralesConstantes;
import ec.edu.uce.academico.ejb.utilidades.constantes.RolConstantes;
import ec.edu.uce.academico.ejb.utilidades.servicios.MensajeGeneradorUtilidades;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;

/**
 * Clase (Bean)RolServicioImpl.
 * Bean declarado como Stateless.
 * @author dcollaguazo
 * @version 1.0
 */

@Stateless
public class RolServicioImpl implements RolServicio{

	@PersistenceContext(unitName=GeneralesConstantes.APP_UNIDAD_PERSISTENCIA)
	private EntityManager em;

	/**
	 * Busca una entidad Rol por su id
	 * @param id - del Rol a buscar
	 * @return Rol con el id solicitado
	 * @throws RolNoEncontradoException - Excepcion lanzada cuando no se encuentra un Rol con el id solicitado
	 * @throws RolException - Excepcion general
	 */
	@Override
	public Rol buscarPorId(Integer id) throws RolNoEncontradoException, RolException {
		Rol retorno = null;
		if (id != null) {
			try {
				retorno = em.find(Rol.class, id);
			} catch (NoResultException e) {
				throw new RolNoEncontradoException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rol.buscar.por.id.no.result.exception",id)));
			}catch (NonUniqueResultException e) {
				throw new RolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rol.buscar.por.id.non.unique.result.exception",id)));
			} catch (Exception e) {
				throw new RolException(MensajeGeneradorUtilidades.getMsj(new MensajeGeneradorUtilidades("Rol.buscar.por.id.exception")));
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
	public List<Rol> listarTodos() {
		List<Rol> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select rol from Rol rol ");
		sbsql.append(" Order by rolDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * Busca el rol por descripcion
	 * @return rol por descripcion
	 */
	@Override
	public Rol buscarRolXDescripcion(String rolDesc) {
		Rol retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select r from Rol r where ");
		sbsql.append(" UPPER(r.rolDescripcion) =:rolDesc ");
		Query q = em.createQuery(sbsql.toString());
		q.setParameter("rolDesc", rolDesc.toUpperCase());
		retorno = (Rol)q.getSingleResult();
		return retorno;
		
	}

	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> listarTodosparaAdministracion() {
		List<Rol> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select rol from Rol rol ");
		sbsql.append(" Where rol.rolId not in (");
		sbsql.append(RolConstantes.ROL_SOPORTE_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_DIRRRHH_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_ACTUALIZACIONDOCENTE_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_COORDASIGNACIONCARGAHORARIA_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_COORDGESTION_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_COORDINADORAREA_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_DIRASEGURAMIENTOCALIDAD_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_COORDINVESTIGACION_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_DIRINVESTIGACION_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_PRESCOMITEETICA_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_EVALUADORDIRECTIVOS_VALUE);sbsql.append(",");
		sbsql.append(RolConstantes.ROL_EVALUADORPARESACADEMICOS_VALUE);
		sbsql.append(" ) ");
		sbsql.append(" order by rol.rolDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;

	}
	
	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> listarTodosparaAgregarCarreras() {
		List<Rol> retorno = null;
		StringBuffer sbsql = new StringBuffer();
		sbsql.append(" Select rol from Rol rol ");
		sbsql.append(" Where rol.rolId in (");
		sbsql.append(RolConstantes.ROL_DIRCARRERA_VALUE);sbsql.append(" , ");
		sbsql.append(RolConstantes.ROL_DOCENTE_VALUE);sbsql.append(" , ");
		sbsql.append(RolConstantes.ROL_SECRECARRERA_VALUE);sbsql.append(" ) ");
		sbsql.append(" order by rol.rolDescripcion ");
		Query q = em.createQuery(sbsql.toString());
		retorno = q.getResultList();
		return retorno;

	}
	
	
	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> listarRolesXUsrId(Integer usrId ) {
		List<Rol> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rol from Rol rol, UsuarioRol usro, Usuario usr ");
			sbsql.append(" Where rol.rolId = usro.usroRol.rolId ");
			sbsql.append(" And usr.usrId = usro.usroUsuario.usrId ");
			sbsql.append(" And usr.usrId = ");sbsql.append(usrId);
			sbsql.append(" order by rol.rolDescripcion ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			return retorno;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rol> listarRolesXUsrIdXRoflcr(Integer usrId ) {
		List<Rol> retorno = null;
		try {
			StringBuffer sbsql = new StringBuffer();
			sbsql.append(" Select rol from Rol rol, UsuarioRol usro, Usuario usr, RolFlujoCarrera roflcr ");
			sbsql.append(" Where rol.rolId = usro.usroRol.rolId ");
			sbsql.append(" And usr.usrId = usro.usroUsuario.usrId ");
			sbsql.append(" And usro.usroId = roflcr.roflcrUsuarioRol.usroId ");
			sbsql.append(" And usr.usrId = ");sbsql.append(usrId);
			sbsql.append(" order by rol.rolDescripcion ");
			Query q = em.createQuery(sbsql.toString());
			retorno = q.getResultList();
			return retorno;
		} catch (NoResultException e) {
			return null;
		}
	}
}
