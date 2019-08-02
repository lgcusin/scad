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

 ARCHIVO:     AsignacionEvaluadorServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorException;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.AsignacionEvaluadorValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.AsignacionEvaluador;

/**
 * Interface AsignacionEvaluadorServicio
 * Interfase que define las operaciones sobre la tabla AsignacionEvaluador.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface AsignacionEvaluadorServicio {

	/**
	 * Busca una entidad AsignacionEvaluador por su id
	 * @param id - deL AsignacionEvaluador a buscar
	 * @return AsignacionEvaluador con el id solicitado
	 * @throws AsignacionEvaluadorNoEncontradoException - Excepcion lanzada cuando no se encuentra una AsignacionEvaluador con el id solicitado
	 * @throws AsignacionEvaluadorException - Excepcion general
	 */
	public AsignacionEvaluador buscarPorId(Integer id) throws AsignacionEvaluadorNoEncontradoException, AsignacionEvaluadorException;

	/**
	 * Lista todas las entidades AsignacionEvaluador existentes en la BD
	 * @return lista de todas las entidades AsignacionEvaluador existentes en la BD
	 * @throws AsignacionEvaluadorNoEncontradoException - Excepcion lanzada cuando no se encuentra una AsignacionEvaluador 
	 * @throws AsignacionEvaluadorException - Excepcion general
	 */
	public List<AsignacionEvaluador> listarTodos() throws AsignacionEvaluadorNoEncontradoException , AsignacionEvaluadorException;
		 
	/**
	 * Añade una AsignacionEvaluador en la BD
	 * @param entidad .- Entidad a ingresar
	 * @return Si se añadio o no la AsignacionEvaluador
	 * @throws AsignacionEvaluadorValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws AsignacionEvaluadorException - Excepción general
	 */
	public AsignacionEvaluador anadir(AsignacionEvaluador entidad) throws AsignacionEvaluadorValidacionException, AsignacionEvaluadorException;

}
