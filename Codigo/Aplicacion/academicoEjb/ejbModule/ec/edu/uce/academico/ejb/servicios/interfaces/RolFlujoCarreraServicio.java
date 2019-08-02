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

 ARCHIVO:     RolFlujoCarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Rol_Flujo_Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 18-JULIO-2017            Gabriel Mafla                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.DocenteTHJdbcDto;
import ec.edu.uce.academico.ejb.dtos.PeriodoAcademicoDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.academico.ejb.dtos.UsuarioRolJdbcDto;
//import ec.edu.uce.academico.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.academico.ejb.excepciones.PersonaException;
import ec.edu.uce.academico.ejb.excepciones.PersonaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoCarreraValidacionException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Carrera;
import ec.edu.uce.academico.jpa.entidades.publico.Puesto;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoCarrera;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Interface RolFlujoCarreraServicio
 * Interfase que define las operaciones sobre la tabla Rol_Flujo_Carrera.
 * @author ghmafla
 * @version 1.0
 */
public interface RolFlujoCarreraServicio {

	
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una RolFlujoCarrera con el id solicitado
	 * @throws RolFlujoCarreraException - Excepcion general
	 */
	public RolFlujoCarrera buscarPorId(Integer id) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	/**
	 * Lista todas las entidades RolFlujoCarrera existentes en la BD
	 * @return lista de todas las entidades RolFlujoCarrera existentes en la BD
	 */
	public List<RolFlujoCarrera> listarTodos() throws RolFlujoCarreraNoEncontradoException;
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	public RolFlujoCarrera buscarPorCarrera(Integer carrera, Integer usuarioId, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id de carrera
	 * @param id - de la carrera 
	 *  @param id - del rol
	 * @return RolFlujoCarrera con el id solicitado
	 */
	public RolFlujoCarrera buscarPorCarreraXRol(Integer carrera, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	/**
	 * Busca una entidad RolFlujoCarrera por su id de usuario
	 * @param id - de la RolFlujoCarrera a buscar
	 * @return RolFlujoCarrera con el id solicitado
	 */
	
	public List<RolFlujoCarrera> buscarPorIdUsuario(Usuario usuario) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	/**
	 * Busca una entidad RolFlujoCarrera por idUsuario y idRol 
	 * @param usuarioId - de Usuario a buscar
	 * @param rolId - de Rol a buscar
	 * @return List<RolFlujoCarrera> solicitado
	 * @author Arturo Villafuerte - ajvillafuerte
	 */ 
	
	public List<RolFlujoCarrera> buscarXRolXUsuarioId(Integer usuarioId, Integer rolId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;
	
	public List<RolFlujoCarrera> buscarXUsuarioXRoles(Integer usuarioId, Integer[] rolesId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;

	public  boolean desactivarRolFlujoCarreraXUsuarioRol(Integer roflcrId) throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException;

	public boolean desactivarRolFlujoCarrerasXListaUsuarioRol(Integer usroId) 	throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException;

	public boolean activarRolFlujoCarreraXUsuarioRol(Integer roflcrId, Integer rolId) 	throws RolFlujoCarreraException, RolFlujoCarreraNoEncontradoException, RolFlujoCarreraValidacionException;


	public boolean anadirUsuarioRolFlujoCarrera(UsuarioCreacionDto entidad, Integer rolId, Integer carreraId, Integer dpnId, Integer tipoCarrera, Usuario usuario, DocenteTHJdbcDto docenteTH, PeriodoAcademicoDto periodoPosgrado, Puesto puesto, Integer relacionLaboral) throws PersonaValidacionException, PersonaException, UsuarioValidacionException;

	public boolean anadirRolFlujoCarrera(UsuarioRolJdbcDto entidad, Integer rolId, Integer carreraId) throws PersonaValidacionException, PersonaException;

	public List<Carrera> buscarRolFlujoCarreraXUsuario(int usrId);

	public List<RolFlujoCarrera> buscarXRolXUsuarioIdXDependencia(Integer usuarioId, Integer rolId, Integer dpnId) throws RolFlujoCarreraNoEncontradoException, RolFlujoCarreraException;

	List<Carrera> buscarCarrerasXUsuarioPosgrado(int usrId);
}
