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

 ARCHIVO:     TipoEvaluacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TipoEvaluacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoEvaluacion;

/**
 * Interface TipoEvaluacionServicio
 * Interfase que define las operaciones sobre la tabla TipoEvaluacion.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface TipoEvaluacionServicio {

	/**
	 * Busca una entidad TipoEvaluacion por su id
	 * @param id - deL TipoEvaluacion a buscar
	 * @return TipoEvaluacion con el id solicitado
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion con el id solicitado
	 * @throws TipoEvaluacionException - Excepcion general
	 */
	public TipoEvaluacion buscarPorId(Integer id) throws TipoEvaluacionNoEncontradoException, TipoEvaluacionException;

	/**
	 * Lista todas las entidades TipoEvaluacion existentes en la BD
	 * @return lista de todas las entidades TipoEvaluacion existentes en la BD
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion 
	 * @throws TipoEvaluacionException - Excepcion general
	 */
	public List<TipoEvaluacion> listarTodos() throws TipoEvaluacionNoEncontradoException , TipoEvaluacionException;
	
	/**
	 * Lista todas las entidades TipoEvaluacion existentes en la BD con estado activo
	 * @return lista de todas las entidades TipoEvaluacion existentes en la BD
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion
	 * @throws TipoEvaluacionException - Excepcion general
	 */ 
	public List<TipoEvaluacion> listarActivos() throws TipoEvaluacionNoEncontradoException , TipoEvaluacionException;
	

	/**
	 * Lista todas las entidades TipoEvaluacion existentes en la BD con estado activo
	 * @return lista de todas las entidades TipoEvaluacion existentes en la BD
	 * @throws TipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoEvaluacion
	 * @throws TipoEvaluacionException - Excepcion general
	 */
	public List<TipoEvaluacion> listarXApelacion() throws TipoEvaluacionNoEncontradoException , TipoEvaluacionException;
	
}
