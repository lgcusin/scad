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

 ARCHIVO:     PlanificacionCronogramaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla PlanificacionCronograma.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 29-03-2017            David Arellano                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.dtos.MallaPeriodoDto;
import ec.edu.uce.academico.ejb.dtos.PlanificacionCronogramaDto;
import ec.edu.uce.academico.ejb.excepciones.CronogramaException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoException;
import ec.edu.uce.academico.ejb.excepciones.PeriodoAcademicoValidacionException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PlanificacionCronogramaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.PlanificacionCronograma;

/**
 * Interface PlanificacionCronogramaServicio
 * Interfase que define las operaciones sobre la tabla PlanificacionCronograma.
 * @author darellano
 * @version 1.0
 */
public interface PlanificacionCronogramaServicio {
	/**
	 * Busca una entidad PlanificacionCronograma por su id
	 * @param id - de la PlanificacionCronograma a buscar
	 * @return PlanificacionCronograma con el id solicitado
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una PlanificacionCronograma con el id solicitado
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	public PlanificacionCronograma buscarPorId(Integer id) throws PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException;

	/**
	 * Lista todas las entidades PlanificacionCronograma existentes en la BD
	 * @return lista de todas las entidades PlanificacionCronograma existentes en la BD
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una PlanificacionCronograma 
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	public List<PlanificacionCronograma> listarTodos() throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException;
	
	/**
	 * Lista todas las entidades PlanificacionCronograma por el cronogama proceso flujo
	 * @param cronogramaProcesoFlujoId - id del cronograma proceso flujo a buscar
	 * @return la entidad Planificacion cronograma según los parametros enviados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra una PlanificacionCronograma
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	public PlanificacionCronograma buscarXCronogramaProcesoFlujo(int cronogramaProcesoFlujoId) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException;
	
	/**
	 * Lista todas las entidades PlanificacionCronograma por estado del cronograma, por estado del período académico y el proceso flujo
	 * @param estadoCronograma - estado del cronograma que desea listar
	 * @param estadoPeriodoAcademico - estado del período académico que se requiere listar
	 * @param listProcesoFlujo - lista de Integer con los proceso flujo requeridos para listar
	 * @param tipoCronograma - Integer del tipo de cronograma a buscar
	 * @return Lista de entidades planificacion cronograma con los parámetros ingresados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra las entidades por los parametros enviados
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	public List<PlanificacionCronograma> buscarXestadoCrnXestadoPracXprocesoFlujo(int estadoCronograma, int estadoPeriodoAcademico, List<Integer> listProcesoFlujo, Integer tipoCronograma) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException;
	
	/**
	 * Lista todas las entidades PlanificacionCronograma por estado del cronograma, por estado del período académico y el proceso flujo
	 * @param estadoCronograma - estado del cronograma que desea listar
	 * @param estadoPeriodoAcademico - estado del período académico que se requiere listar
	 * @param listProcesoFlujo - lista de Integer con los proceso flujo requeridos para listar
	 * @param tipoCronograma - Integer del tipo de cronograma a buscar
	 * @return Lista de entidades planificacion cronograma con los parámetros ingresados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra las entidades por los parametros enviados
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	//TODO: CAMBIAR EL COMENTARIO
	public List<PlanificacionCronograma> buscarXperiodoIdXtipoCronogramaXlistProcesoFlujo(int periodoId, List<Integer> listProcesoFlujo, Integer tipoCronograma) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException;
	

	/**
	 * Lista todas las entidades PlanificacionCronograma por posgrado por estado del cronograma, por estado del período académico y el proceso flujo
	 * @param crrId - id del programa de posgrado
	 * @param crnEstado - estado del cronograma que desea listar
	 * @param crnTipo - Integer del tipo de cronograma a buscar
	 * @param pracEstado - estado del período académico que se requiere listar
	 * @param pracTipo - estado del período académico de posgrado por programa que se requiere listar
	 * @param listProcesoFlujo - lista de Integer con los proceso flujo requeridos para listar
	 * @return Lista de entidades planificacion cronograma con los parámetros ingresados
	 * @throws PlanificacionCronogramaNoEncontradoException - Excepcion lanzada cuando no se encuentra las entidades por los parametros enviados
	 * @throws PlanificacionCronogramaException - Excepcion general
	 */
	public List<PlanificacionCronograma> buscarPlcrPosgradoXestadoCrnXestadoPracXprocesoFlujo(int crrId,int crnEstado,Integer crnTipo, int pracEstado,int pracTipo, List<Integer> listProcesoFlujo ) throws PlanificacionCronogramaNoEncontradoException , PlanificacionCronogramaException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return la entidad editada
	 * @throws PlanificacionCronogramaException 
	 * @throws CronogramaValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws CronogramaException - Excepción general
	 */
	public Boolean editarCronograma(CronogramaDto entidad) throws PeriodoAcademicoValidacionException, PeriodoAcademicoException, PlanificacionCronogramaException;
	
	/**
	 * Método que permite crear el flujo de una nueva planificacion , registra un periodo academico , cronograma, y planificacion.
	 * @author fgguzman
	 * @param periodo - periodo y cronograma
	 * @param planificaciones - cronogramas por proceso
	 * @param malla - malla periodo
	 * @return true - si se crea correctamente
	 * @throws PlanificacionCronogramaException
	 * @throws PlanificacionCronogramaValidacionException
	 */
	Boolean crearNuevaPlanificacion(PlanificacionCronogramaDto periodo, List<PlanificacionCronogramaDto> planificaciones,  List<MallaPeriodoDto> malla) throws PlanificacionCronogramaException, PlanificacionCronogramaValidacionException;
	
	/**
	 * Método que permite actualizar el cronograma de un proceso de la planificacion.
	 * @author fgguzman
	 * @param proceso - entidad con atributós para actualizar
	 * @return true si la actualizacion fue correcta
	 * @throws PlanificacionCronogramaException
	 * @throws PlanificacionCronogramaValidacionException
	 */
	Boolean editarPlanificacion(PlanificacionCronogramaDto proceso) throws PlanificacionCronogramaException, PlanificacionCronogramaValidacionException;

	List<PlanificacionCronograma> buscarPlcrPosgradoXestadoCrnXestadoPracXprocesoFlujoXPracId(int crrId, int crnEstado,
			Integer crnTipo, int pracEstado, int pracTipo, List<Integer> listProcesoFlujo, Integer pracId)
			throws PlanificacionCronogramaNoEncontradoException, PlanificacionCronogramaException;
}
