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

 ARCHIVO:     RolServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Rol.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.RolException;
import ec.edu.uce.academico.ejb.excepciones.RolNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Rol;

/**
 * Interface RolServicio
 * Interfase que define las operaciones sobre la tabla Rol.
 * @author dcollaguazo
 * @version 1.0
 */
public interface RolServicio {
	/**
	 * Busca una entidad Rol por su id
	 * @param id - del Rol a buscar
	 * @return Rol con el id solicitado
	 * @throws RolNoEncontradoException - Excepcion lanzada cuando no se encuentra un Rol con el id solicitado
	 * @throws RolException - Excepcion general
	 */
	public Rol buscarPorId(Integer id) throws RolNoEncontradoException, RolException;

	/**
	 * Lista todas las entidades Rol existentes en la BD
	 * @return lista de todas las entidades Rol existentes en la BD
	 */
	public List<Rol> listarTodos();
	
	/**
	 * Busca el rol por descripcion
	 * @return rol por descripcion
	 */
	public Rol buscarRolXDescripcion(String rolDesc);

	public List<Rol> listarTodosparaAdministracion();

	public List<Rol> listarTodosparaAgregarCarreras();

	public List<Rol> listarRolesXUsrId(Integer usrId);
	
	public List<Rol> listarRolesXUsrIdXRoflcr(Integer usrId ) ;
	
}
