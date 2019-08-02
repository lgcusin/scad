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

 ARCHIVO:     RolFlujoGrupoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Rol_Flujo_Grupo.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 07-FEBRERO-2018           Arturo Villafuerte                    Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.RolFlujoGrupoException;
import ec.edu.uce.academico.ejb.excepciones.RolFlujoGrupoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.RolFlujoGrupo;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Interface RolFlujoGrupoServicio
 * Interfase que define las operaciones sobre la tabla Rol_Flujo_Grupo.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface RolFlujoGrupoServicio {

	
	
	/**
	 * Busca una entidad RolFlujoGrupo por su id
	 * @param id - de la RolFlujoGrupo a buscar
	 * @return RolFlujoGrupo con el id solicitado
	 * @throws RolFlujoGrupoNoEncontradoException - Excepcion lanzada cuando no se encuentra una RolFlujoGrupo con el id solicitado
	 * @throws RolFlujoGrupoException - Excepcion general
	 */
	public RolFlujoGrupo buscarPorId(Integer id) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException;
	
	/**
	 * Lista todas las entidades RolFlujoGrupo existentes en la BD
	 * @return lista de todas las entidades RolFlujoGrupo existentes en la BD
	 */
	public List<RolFlujoGrupo> listarTodos() throws RolFlujoGrupoNoEncontradoException;
	
	/**
	 * Busca una entidad RolFlujoGrupo por su id de carrera
	 * @param id - de la RolFlujoGrupo a buscar
	 * @return RolFlujoGrupo con el id solicitado
	 */
	public RolFlujoGrupo buscarPorGrupo(Integer carrera, Integer usuarioId, Integer rolId) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException;
	
	/**
	 * Busca una entidad RolFlujoGrupo por su id de usuario
	 * @param id - de la RolFlujoGrupo a buscar
	 * @return RolFlujoGrupo con el id solicitado
	 */
	public List<RolFlujoGrupo> buscarPorIdUsuario(Usuario usuario) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException;
	
	/**
	 * Busca una entidad RolFlujoGrupo por idUsuario y idRol 
	 * @param usuarioId - de Usuario a buscar
	 * @param rolId - de Rol a buscar
	 * @return List<RolFlujoGrupo> solicitado
	 * @author Arturo Villafuerte - ajvillafuerte
	 */ 
	public List<RolFlujoGrupo> buscarXRolXUsuarioId(Integer usuarioId, Integer rolId) throws RolFlujoGrupoNoEncontradoException, RolFlujoGrupoException;
}
