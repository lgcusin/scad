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

 ARCHIVO:     TipoContenidoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.TipoContenidoException;
import ec.edu.uce.academico.ejb.excepciones.TipoContenidoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoContenidoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoContenido;

/**
 * Interface TipoContenidoServicio
 * Interfase que define las operaciones sobre la tabla TipoContenido.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface TipoContenidoServicio {

	/**
	 * Busca una entidad TipoContenido por su id
	 * @param id - deL TipoContenido a buscar
	 * @return TipoContenido con el id solicitado
	 * @throws TipoContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoContenido con el id solicitado
	 * @throws TipoContenidoException - Excepcion general
	 */
	public TipoContenido buscarPorId(Integer id) throws TipoContenidoNoEncontradoException, TipoContenidoException;

	/**
	 * Lista todas las entidades TipoContenido existentes en la BD
	 * @return lista de todas las entidades TipoContenido existentes en la BD
	 * @throws TipoContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoContenido 
	 * @throws TipoContenidoException - Excepcion general
	 */
	public List<TipoContenido> listarTodos() throws TipoContenidoNoEncontradoException , TipoContenidoException;
	
	/**
	 * Añade una TipoContenido en la BD
	 * @param entidad .- Entidad a ingresar
	 * @return Si se añadio o no la TipoContenido
	 * @throws TipoContenidoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws TipoContenidoException - Excepción general
	 */
	public TipoContenido anadir(TipoContenido entidad) throws TipoContenidoValidacionException, TipoContenidoException;
	
	
	/**
	 * Lista de entidades TipoContenido existentes en la BD por tipo de evalucion
	 * @param idTipoContenido .- id del tipo de TipoContenido
	 * @return Lista de entidades TipoContenido existentes en la BD
	 * @throws TipoContenidoNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoContenido
	 * @throws TipoContenidoException - Excepcion general
	 */
	public List<TipoContenido> listarTodosXTipo(int idTipoContenido) throws TipoContenidoNoEncontradoException , TipoContenidoException;
}
