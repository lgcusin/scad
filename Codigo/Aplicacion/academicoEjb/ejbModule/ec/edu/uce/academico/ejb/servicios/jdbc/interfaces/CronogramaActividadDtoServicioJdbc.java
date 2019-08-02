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

 ARCHIVO:     CronogramaActividadDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc del DTO CronogramaActividadesDTO.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 20-06-2017			Dennis Collaguazo				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import ec.edu.uce.academico.ejb.dtos.CronogramaActividadJdbcDto;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.CronogramaActividadDtoJdbcNoEncontradoException;

/**
 * Interface CronogramaActividadDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc del DTO CronogramaActividadDTO.
 * @author dcollaguazo
 * @version 1.0
 */
public interface CronogramaActividadDtoServicioJdbc {
	
	/**
	 * Metodo que realiza la busqueda de las actividades del proceso flujo
	 * @param idCrr - idCrr id de la carrera que pertenece el consultado
	 * @param estadoPrac - estadoPrac estado del periodo académico del que pertenece el consultado
	 * @param tipoCrn - tipoCrn tipo cronograma si es para 1:nivelación ó 0:carrera o academico 
	 * @param idPrfl - idPrfl id del proceso flujo al que se quiere consultar
	 * @param estadoPrfl - estadoPrfl estado del proceso flujo del que se quiere consultar
	 * @return Retorna la entidad CronogramaActividadesJdbcDto que ha sido consultado con los parametros requirientes
	 * @throws CronogramaActividadDtoJdbcException  - CronogramaActividadDtoJdbcException excepción general
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException  - CronogramaActividadDtoJdbcNoEncontradoException excepción cuando no se encuentra cronogramaactividad por los parametros ingresados
	 */
	public CronogramaActividadJdbcDto listarXCarrXEstadoPeriAcadXTipoCronoXPlanCronoXEstadoPlanCrono(int idCrr, int estadoPrac, int tipoCrn, int idPrfl, int estadoPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException ;

	/**
	 * Metodo busca rango de fechas por tipo cronograma y proceso flujo para el ingreso a paso de notas
	 * @param tipoCrn - tipo cronograma
	 * @param idPrfl - tipo ingreso notas 6.- 1parcial 7.- 2parcial 8.- recuperacion
	 * @return CronogramaActividadJdbcDto con las fechas de paso de notas
	 * @throws CronogramaActividadDtoJdbcException  - CronogramaActividadDtoJdbcException excepción general
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException  - CronogramaActividadDtoJdbcNoEncontradoException excepción cuando no se encuentra cronogramaactividad por los parametros ingresados
	 */
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoEnCierreXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujo(int tipoCrn, int idPrfl)throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;
	
	public CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechas(String date) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	public CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechasNivelacion(String date) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;
	
	/**
	 * Busca Planificacion cronograma por fecha
	 * @author v1-ajvillafuerte
	 * @author v2-fgguzman
	 * @param date = fecha actual
	 * @param crnTipo = {0-PREGRADO , 1-NIVELACION , 2-CONTRATACION ,3-POSGRADO , 4-HOMOLOGACION , 5-SUF_IDIOMAS}
	 */ 
	public CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechasFull(String date, int crnTipo) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;
	
	/**
	 * Buscar CronogramaActividadJdbcDto por los campos mencionados 
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param tipoCrn .- TipoCronograma a buscar
	 * @param idPrfl .- ProcesoFLujo a buscar
	 * @return CronogramaActividadJdbcDto encontrado
	 * @throws CronogramaActividadDtoJdbcException
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException
	 */
	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoFull(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoSchedule(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	public CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoActivo(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;
	
	/**
	 * Método que permite buscar una actividad del Proceso flujo segun tipo de Cronograma.
	 * @author fgguzman
	 * @param prflId - ID del ProcesoFlujo qe requiera buscar.
	 * @param crnTipo - tipo de cronog
	 * @return Cronograma de la Actividad solicitada.
	 * @throws CronogramaActividadDtoJdbcException
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException
	 */
	CronogramaActividadJdbcDto buscarCronograma(int prflId, int crnTipo) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;
	
	/**
	 * Método que permite buscar una actividad del Proceso flujo segun Planificacion del Cronograma.
	 * @author fgguzman
	 * @param plcrId - ID de la Planificacion Cronograma que en el cual el estudiante de matriculo.
	 * @return Cronograma de la Actividad solicitada.
	 * @throws CronogramaActividadDtoJdbcException
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException
	 */
	CronogramaActividadJdbcDto buscarCronogramaPorPlanificacionCronograma(int plcrId) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	/**
	 * Método que permite buscar un cronograma segun parametros.
	 * @author fgguzman
	 * @param procesoId - id del proceso flujo
	 * @param cronogramaTipo - tipo del cronograma
	 * @param periodoId - id del periodo
	 * @return cronograma solicitado
	 * @throws CronogramaActividadDtoJdbcException
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException
	 */
	CronogramaActividadJdbcDto buscarCronogramaPorPeriodo(int procesoId, int cronogramaTipo, int periodoId) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException ;
	
	/**
	 * Método que permite buscar la planificacion de un proceso segun proceso y periodo.
	 * @author fgguzman
	 * @param procesoId - id del ProcesoFlujo.
	 * @param periodoId - id del Periodo Academico.
	 * @return dto
	 * @throws CronogramaActividadDtoJdbcException
	 * @throws CronogramaActividadDtoJdbcNoEncontradoException
	 */
	CronogramaActividadJdbcDto buscarCronogramaPorProcesoPeriodo(int procesoId, int periodoId) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException ;

	
	CronogramaActividadJdbcDto buscarRangoFechasNotasXestadoPeriodoXTipoCronogramaXestadoCronogramaXprocesoFlujoXestadoProcesoFlujoEnCierre(int tipoCrn, int idPrfl)throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	CronogramaActividadJdbcDto buscarRangoFechasNotasSuficienciaCulturaFisica(int tipoCrn, int idPrfl) throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	CronogramaActividadJdbcDto buscarRangoFechasxPracIdXtipoFlujo(int pracId, int idPrfl)
			throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	CronogramaActividadJdbcDto buscarRangoFechasPeriodoActivoPorProceso(int tipoCrn, int idPrfl)
			throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;

	CronogramaActividadJdbcDto buscarPlanificacionCronogramaPorFechasNivelacionCursoVerano(String date)
			throws CronogramaActividadDtoJdbcException, CronogramaActividadDtoJdbcNoEncontradoException;


}
