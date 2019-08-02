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

 ARCHIVO:     FuncionTipoEvaluacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla FuncionTipoEvaluacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
18-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.FuncionTipoEvaluacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.FuncionTipoEvaluacion;

/**
 * Interface FuncionTipoEvaluacionServicio
 * Interfase que define las operaciones sobre la tabla FuncionTipoEvaluacion.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface FuncionTipoEvaluacionServicio {

	/**
	 * Busca una entidad FuncionTipoEvaluacion por su id
	 * @param id - deL FuncionTipoEvaluacion a buscar
	 * @return FuncionTipoEvaluacion con el id solicitado
	 * @throws FuncionTipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una FuncionTipoEvaluacion con el id solicitado
	 * @throws FuncionTipoEvaluacionException - Excepcion general
	 */
	public FuncionTipoEvaluacion buscarPorId(Integer id) throws FuncionTipoEvaluacionNoEncontradoException, FuncionTipoEvaluacionException;

	/**
	 * Lista todas las entidades FuncionTipoEvaluacion existentes en la BD
	 * @return lista de todas las entidades FuncionTipoEvaluacion existentes en la BD
	 * @throws FuncionTipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una FuncionTipoEvaluacion 
	 * @throws FuncionTipoEvaluacionException - Excepcion general
	 */
	public List<FuncionTipoEvaluacion> listarTodos() throws FuncionTipoEvaluacionNoEncontradoException , FuncionTipoEvaluacionException;
	
	/**
	 * Entidad FuncionTipoEvaluacion existentes en la BD con estado activo y por tipo de FuncionTipoEvaluacion
	 * @param idTipoFuncionTipoEvaluacion .- id del tipo de FuncionTipoEvaluacion
	 * @return Entidade FuncionTipoEvaluacion existentes en la BD
	 * @throws FuncionTipoEvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una FuncionTipoEvaluacion
	 * @throws FuncionTipoEvaluacionException - Excepcion general
	 */ 
	public List<FuncionTipoEvaluacion> listarActivoXTipoEvaluacion(int idTipoFuncionTipoEvaluacion) throws FuncionTipoEvaluacionNoEncontradoException , FuncionTipoEvaluacionException;
	
}
