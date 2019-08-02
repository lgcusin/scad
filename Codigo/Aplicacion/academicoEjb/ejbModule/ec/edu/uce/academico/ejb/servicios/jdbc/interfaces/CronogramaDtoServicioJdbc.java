/**************************************************************************
 *				(c) Copyright UNIVERSIDAD CENTRAL DEL ECUADOR. 
 *                            www.uce.edu.ec

 * Este programa de computador es propiedad de la UNIVERSIDAD CENTRAL DEL ECUADOR
 * y esta protegido por las leyes y tratados internacionales de derechos de 
 * autor. El uso, reproducción o distribución no autorizada de este programa, 
 * o cualquier porción de él, puede dar lugar a sanciones criminales y 
 * civiles severas, y serán procesadas con el grado máximo contemplado 
 * por la ley.
 ************************************************************************* 

 ARCHIVO:     CronogramaDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla Cronograma.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 12-DICI-2017		  Vinicio Rosales 				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CronogramaDto;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaDtoJdbcNoEncontradoException;

/**
 * Interface CronogramaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla Cronograma.
 * @author jvrosales
 * @version 1.0
 */
public interface CronogramaDtoServicioJdbc {
	/**
	 * Realiza la busqueda de los procesos con sus respectivo periodo academico
	 * @return Lista los procesos del cronograma
	 * @throws CronogramaDtoJdbcException 
	 * @throws CronogramaDtoJdbcNoEncontradoException 
	 * @throws CronogramaDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws CronogramaDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	List<CronogramaDto> listarProcesosXPeriodoxTipo(int pracId, int crnTipo) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException;
	
	
	/**
	 * Realiza la búsqueda de la planificacion de cronograma para ingresar las fechas
	 * @param pracId - id del periodo acedemico
	 * @param cnrTipo - tipo de cronograma nivelacion, pregrado
	 * @param plcrId - id de la planificaicon a buscar 
	 * @return la planificacion del cronograma
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	CronogramaDto buscarPlanificacionCronogramaXPeriodoXTipoXPlanificacion(int pracId, int cnrTipo, int plcrId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la búsqueda de cronograma por los parametros de busqueda
	 * @author Arturo Villafuerte - ajvillafuerte 
	 * @param prflId - id del proceso flujo
	 * @param tiapId - tipo de tipoApertura
	 * @param nvapNumeral - numeral en nivel apertira por semestre 
	 * @return fechas de el cronograma
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	CronogramaDto buscarCronogramaXTipoAperturaXNivelAperturaXprocesoFlujo(int prflId, int tiapId, int nvapNumeral, int nvapCrrId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la busqueda del Proceso Flujo del periodo academico segun tipo de Cronograma.
	 * @author fgguzman
	 * @param crnTipo - 0,1,2,3,4,5,6,7 -> Pregrado, Nivelacion, Contratacion, Postgrado, Homologacion,Idiomas,Cultura Fisica, Informatica
	 * @param pracEstado - 0,1,2 -> Activo, Inactivo, En cierre
	 * @return procesos flujo correspondientes al tipo de cronograma segun estado del periodo academico.
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	List<CronogramaDto> buscarProcesoFlujo(int crnTipo, Integer[] pracEstado) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException;
	

	/**
	 * Realiza la busqueda del Proceso Flujo para habilitar matriculas de estudiantes regulares o irregulares.
	 * @author fgguzman
	 * @param crnTipo - 0,1,2,3,4,5,6,7 -> Pregrado, Nivelacion, Contratacion, Postgrado, Homologacion,Idiomas,Cultura Fisica, Informatica
	 * @param pracEstado - 0,1,2 -> Activo, Inactivo, En cierre
	 * @param prflId - id del proceso requerido
	 * @return procesos flujo correspondientes al tipo de cronograma segun estado del periodo academico.
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	CronogramaDto buscarTipoMatriculaGeneral(int crnTipo, int pracEstado, int prflId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException;
	
	/**
	 * Realiza la búsqueda de cronograma por los parametros de busqueda
	 * @author MQ:
	 * @param prflId - id del proceso flujo
	 * @param crnTipo - tipo de cronograma
	 * @param pracId - id del periodo académico 
	 * @return cronograma con fechas
	 * @throws CronogramaDtoJdbcException
	 * @throws CronogramaDtoJdbcNoEncontradoException
	 */
	
	public List<CronogramaDto> listarFechasCronogramaXTipoCronogramaXPeriodo(int crnTipo, int pracId) throws CronogramaDtoJdbcException, CronogramaDtoJdbcNoEncontradoException;
	
}
