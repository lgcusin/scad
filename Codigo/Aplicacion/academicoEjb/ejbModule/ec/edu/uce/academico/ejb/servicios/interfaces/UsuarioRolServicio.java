/**************************************************************************
 *                (c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y está protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.

 ************************************************************************* 

 ARCHIVO:     UsuarioRolServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla UsuarioRol.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.UsuarioRolException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioRolNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;
import ec.edu.uce.academico.jpa.entidades.publico.UsuarioRol;

/**
 * Interface UsuarioRolServicio
 * Interfase que define las operaciones sobre la tabla UsuarioRol.
 * @author dcollaguazo
 * @version 1.0
 */
public interface UsuarioRolServicio {
	/**
	 * Busca una entidad UsuarioRol por su id
	 * @param id - del UsuarioRol a buscar
	 * @return UsuarioRol con el id solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorId(Integer id) throws UsuarioRolNoEncontradoException, UsuarioRolException;

	/**
	 * Lista todas las entidades UsuarioRol existentes en la BD
	 * @return lista de todas las entidades UsuarioRol existentes en la BD
	 */
	public List<UsuarioRol> listarTodos();

	/**
	 * Lista todos los roles de los usuarios existentes en la BD
	 * @param usrId -  usrId id del usuario a consultar
	 * @return - retorna la lista de todos los roles de los usuarios existentes en la BD
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Excepcion lanzada cuando no se encontraros usuario rol con los parametros ingresados
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 */
	public List<UsuarioRol> buscarXUsuario(int usrId) throws UsuarioRolNoEncontradoException, UsuarioRolException;

	/**
	 * Busca el usuario rol por id de usuario
	 * @param idUsuario -  idUsuario id del usuario a consultar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepcion general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolException Excepción lanzada cuando no hay resultado en la consulta
	 */
	public UsuarioRol buscarEvaluadorXUsuario(int idUsuario) throws UsuarioRolNoEncontradoException, UsuarioRolException ;

	/**
	 * Busca el usuario rol por id de usuario
	 * @param idUsuario -  idUsuario id del usuario a consultar
	 * @param idRol -  idRol id del rol que quiere buscar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Exepción lanzada cuando no encuentra usuario rol con los parametros indicados
	 */
	public UsuarioRol buscarXUsuarioXrol(int idUsuario, int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException;

	/**
	 * Busca el usuario_rol por rol que tenga ese usuario
	 * @param idRol -  idRol id del rol que quiere buscar
	 * @return UsuarioRol - la entidad usuario rol que se busca
	 * @throws UsuarioRolException - lanzada cuando exite un error desonocido o el resultado no es unico
	 * @throws UsuarioRolNoEncontradoException - lanzada cuando no se encuentra la entidad buscada
	 */
	public UsuarioRol buscarXrol(int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException;

	/**
	 * Lista todos los roles del usuario enviado en la BD
	 * @param usrId -  usrId id del usuario a consultar
	 * @return - retorna la lista de todos los roles de los usuarios existentes en la BD
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Excepcion lanzada cuando no se encontraros usuario rol con los parametros ingresados
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 */
	public List<Rol> buscarRolesXUsuario(int usrId) throws UsuarioRolNoEncontradoException, UsuarioRolException;
	
	/**
	 * Busca el usuario rol por id de persona
	 * @param idPersona -  idPersona id del usuario a consultar
	 * @param idRol -  idRol id del rol que quiere buscar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepción general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolNoEncontradoException Exepción lanzada cuando no encuentra usuario rol con los parametros indicados
	 * @author Arturo Villafuerte - ajvillafuerte
	 */
	public UsuarioRol buscarXPersonaXrol(int idPersona, int idRol) throws UsuarioRolException , UsuarioRolNoEncontradoException;

	/**
	 * Busca una entidad UsuarioRol por su identificacion  de SOPORTE
	 * @param identificacion - del UsuarioRol a buscar
	 * @return UsuarioRol con la identificacion solicitado
	 * @throws UsuarioRolNoEncontradoException - Excepcion lanzada cuando no se encuentra un UsuarioRol con el id solicitado
	 * @throws UsuarioRolException - Excepcion general
	 */
	public UsuarioRol buscarPorIdentificacionSoporte(String identificacion)
			throws UsuarioRolNoEncontradoException, UsuarioRolException;

	public void activarUsuarioRolXid(Integer usroId, Integer usrId) throws UsuarioRolException;
	
	public void desactivarUsuarioRol(Integer usroId)throws  UsuarioRolException ;

	public void desactivarUsuarioRolXid(Integer usroId, Integer usrId) throws UsuarioRolException;

	public void activarUsuarioRol(Integer usroId)throws  UsuarioRolException ;
	
	public UsuarioRol buscarPorIdentificacion(String identificacion)
			throws UsuarioRolNoEncontradoException, UsuarioRolException;

	public List<UsuarioRol> buscarRolesActivoXUsuario(int usrId) throws UsuarioRolNoEncontradoException, UsuarioRolException;
	
	/**
	 * Busca el usuario rol por id de usuario
	 * @param idUsuario -  idUsuario id del usuario a consultar
	 * @return - la entidad usuario rol
	 * @throws UsuarioRolException - UsuarioRolException Excepcion general
	 * @throws UsuarioRolNoEncontradoException - UsuarioRolException Excepción lanzada cuando no hay resultado en la consulta
	 */
	public UsuarioRol buscarXUsuarioId(int idUsuario) throws UsuarioRolNoEncontradoException, UsuarioRolException ;

	void regresarNivelacionUsuarioRol(Integer usroId) throws UsuarioRolException;

	UsuarioRol buscarPorIdentificacionPorRol(String identificacion, int rolId)
			throws UsuarioRolNoEncontradoException, UsuarioRolException;

	UsuarioRol buscarXUsuarioXrolActivo(int idUsuario, int idRol)
			throws UsuarioRolException, UsuarioRolNoEncontradoException;

}
