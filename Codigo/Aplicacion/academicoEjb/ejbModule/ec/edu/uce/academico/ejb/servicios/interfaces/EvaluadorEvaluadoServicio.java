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

 ARCHIVO:     EvaluadorEvaluadoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluadorEvaluadoValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.EvaluadorEvaluado;

/**
 * Interface EvaluadorEvaluadoServicio
 * Interfase que define las operaciones sobre la tabla EvaluadorEvaluado.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface EvaluadorEvaluadoServicio {

	/**
	 * Busca una entidad EvaluadorEvaluado por su id
	 * @param id - deL EvaluadorEvaluado a buscar
	 * @return EvaluadorEvaluado con el id solicitado
	 * @throws EvaluadorEvaluadoNoEncontradoException - Excepcion lanzada cuando no se encuentra una EvaluadorEvaluado con el id solicitado
	 * @throws EvaluadorEvaluadoException - Excepcion general
	 */
	public EvaluadorEvaluado buscarPorId(Integer id) throws EvaluadorEvaluadoNoEncontradoException, EvaluadorEvaluadoException;

	/**
	 * Lista todas las entidades EvaluadorEvaluado existentes en la BD
	 * @return lista de todas las entidades EvaluadorEvaluado existentes en la BD
	 * @throws EvaluadorEvaluadoNoEncontradoException - Excepcion lanzada cuando no se encuentra una EvaluadorEvaluado 
	 * @throws EvaluadorEvaluadoException - Excepcion general
	 */
	public List<EvaluadorEvaluado> listarTodos() throws EvaluadorEvaluadoNoEncontradoException , EvaluadorEvaluadoException;
		 
	/**
	 * Añade una EvaluadorEvaluado en la BD
	 * @param entidad .- Entidad a ingresar
	 * @return Si se añadio o no la EvaluadorEvaluado
	 * @throws EvaluadorEvaluadoValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws EvaluadorEvaluadoException - Excepción general
	 */
	public EvaluadorEvaluado anadir(EvaluadorEvaluado entidad) throws EvaluadorEvaluadoValidacionException, EvaluadorEvaluadoException;

}
