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

 ARCHIVO:     UsuarioServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Usuario.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.UsuarioCreacionDto;
import ec.edu.uce.academico.ejb.excepciones.UsuarioException;
import ec.edu.uce.academico.ejb.excepciones.UsuarioNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;

/**
 * Interface UsuarioServicio
 * Interfase que define las operaciones sobre la tabla Usuario.
 * @author dcollaguazo
 * @version 1.0
 */
public interface UsuarioServicio {
	/**
	 * Busca una entidad Usuario por su id
	 * @param id - del Usuario a buscar
	 * @return Usuario con el id solicitado
	 * @throws UsuarioNoEncontradoException - Excepcion lanzada cuando no se encuentra un Usuario con el id solicitado
	 * @throws UsuarioException - Excepcion general
	 */
	public Usuario buscarPorId(Integer id) throws UsuarioNoEncontradoException, UsuarioException;

	/**
	 * Lista todas las entidades Usuario existentes en la BD
	 * @return lista de todas las entidades Usuario existentes en la BD
	 */
	public List<Usuario> listarTodos();

	/**
	 * Busca a un usuario por su nick
	 * @param nickName - nick con el que se va a buscar
	 * @return usuario con el nick solicitado
	 * @throws UsuarioNoEncontradoException - UsuarioNoEncontradoException Excepcion lanzada cuando no encuentra usuario por los parametros ingresados
	 * @throws UsuarioException - UsuarioException Excepción general
	 */
	public Usuario buscarPorNick(String nickName) throws UsuarioNoEncontradoException, UsuarioException ;

	
	
	/**
	 * Lista  entidad Usuario existentes en la BD de acuerdo a su identificación
	 * @return busca las entidade Usuario existentes en la BD
	 * @throws UsuarioException - UsuarioException Excepcion general
	 * @throws UsuarioNoEncontradoException - UsuarioNoEncontradoException Excepcion lanzada cuando no hay usuario
	 */
	
	public Usuario buscarUsuarioPorIdentificacion(String identificacion) throws UsuarioNoEncontradoException, UsuarioException;

	public boolean crearPersonaUsuario(UsuarioCreacionDto entidad);
}
