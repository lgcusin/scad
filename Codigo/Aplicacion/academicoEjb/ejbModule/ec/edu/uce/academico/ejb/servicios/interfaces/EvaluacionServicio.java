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

 ARCHIVO:     EvaluacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.EvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.EvaluacionValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionEvaluacionValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.Evaluacion;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionEvaluacion;

/**
 * Interface EvaluacionServicio
 * Interfase que define las operaciones sobre la tabla Evaluacion.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface EvaluacionServicio {

	/**
	 * Busca una entidad Evaluacion por su id
	 * @param id - deL Evaluacion a buscar
	 * @return Evaluacion con el id solicitado
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion con el id solicitado
	 * @throws EvaluacionException - Excepcion general
	 */
	Evaluacion buscarPorId(Integer id) throws EvaluacionNoEncontradoException, EvaluacionException;

	/**
	 * Lista todas las entidades Evaluacion existentes en la BD
	 * @return lista de todas las entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion 
	 * @throws EvaluacionException - Excepcion general
	 */
	List<Evaluacion> listarTodos() throws EvaluacionNoEncontradoException , EvaluacionException;
	
	/**
	 * Lista todas las entidades Evaluacion existentes en la BD
	 * @return lista de todas las entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	
	public List<Evaluacion> listarActivo() throws EvaluacionNoEncontradoException , EvaluacionException;
	
	/**
	 * Entidad Evaluacion existentes en la BD con estado activo y por tipo de evalucion
	 * @param idTipoEvaluacion .- id del tipo de evaluacion
	 * @return Entidade Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */ 
	Evaluacion buscarActivoXTipo(int idTipoEvaluacion) throws EvaluacionValidacionException, EvaluacionNoEncontradoException , EvaluacionException;
	
	/**
	 * Lista todas las entidades Evaluacion existentes en la BD por periodo
	 * @param idPeriodo .- Id del periodo a buscar
	 * @return lista de todas las entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	List<Evaluacion> listarXPeriodo(int idPeriodo) throws EvaluacionNoEncontradoException , EvaluacionException;
	 
	/**
	 * Lista de entidades Evaluacion existentes en la BD por tipo de evalucion activas
	 * @param idTipoEvaluacion .- id del tipo de evaluacion
	 * @return Lista de entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	List<Evaluacion> listarTodosXTipoActivo(int idTipoEvaluacion) throws EvaluacionNoEncontradoException , EvaluacionException;
	
	/**
	 * Añade una Evaluacion en la BD
	 * @param entidad .- Entidad a ingresar
	 * @return Si se añadio o no la Evaluacion
	 * @throws EvaluacionValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws EvaluacionException - Excepción general
	 */
	Evaluacion anadir(Evaluacion entidad) throws EvaluacionValidacionException, EvaluacionException;
	
	/**
	 * Edita una Evaluacion en la BD
	 * @param entidad .- Entidad a modificar
	 * @return Si se Edito o no la Evaluacion
	 * @throws EvaluacionValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws EvaluacionException - Excepción general
	 */
	Boolean editar(Evaluacion entidad) throws EvaluacionValidacionException, EvaluacionException;
	
	/**
	 * Lista de entidades Evaluacion existentes en la BD por tipo de evalucion
	 * @param idTipoEvaluacion .- id del tipo de evaluacion
	 * @return Lista de entidades Evaluacion existentes en la BD
	 * @throws EvaluacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Evaluacion
	 * @throws EvaluacionException - Excepcion general
	 */
	List<Evaluacion> listarTodosXTipo(int idTipoEvaluacion) throws EvaluacionNoEncontradoException , EvaluacionException;

	/**
	 * Método que permite encontrar una evaluacion por tipo de evaluacion.
	 * @param periodo
	 * @param tipoEvl
	 * @return
	 * @throws EvaluacionNoEncontradoException
	 * @throws EvaluacionException
	 */
	Evaluacion buscarEvaluacion(int periodo, int tipoEvl) throws EvaluacionNoEncontradoException, EvaluacionException;

	/**
	 * Método que permite buscar la planificacion de los procesos de Evaluacion al Desempeño Docente.
	 * @author fgguzman
	 * @param evaluacionId - id de la Evaluacion
	 * @param procesoId - id del ProcesoFlujo
	 * @param plevEstado - id del estado de la PlanificacionEvaluacion
	 * @return Planifcacion Evaluacion solicitada.
	 * @throws PlanificacionEvaluacionNoEncontradoException
	 * @throws PlanificacionEvaluacionValidacionException
	 * @throws PlanificacionEvaluacionException
	 */
	PlanificacionEvaluacion buscarPlanificacionEvaluacion(int evaluacionId, int procesoId, int plevEstado) throws PlanificacionEvaluacionNoEncontradoException, PlanificacionEvaluacionValidacionException, PlanificacionEvaluacionException;

}
