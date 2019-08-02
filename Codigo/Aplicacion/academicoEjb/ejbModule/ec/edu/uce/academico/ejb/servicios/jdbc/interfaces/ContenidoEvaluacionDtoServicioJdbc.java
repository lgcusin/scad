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

 ARCHIVO:     ContenidoEvaluacionDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc del DTO ContenidoEvaluacionDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 30-AGOSTO-2017			Arturo Villafuerte				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.ContenidoEvaluacionDto;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.ContenidoEvaluacionDtoNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoException;
import ec.edu.uce.academico.ejb.excepciones.PersonaDatosDtoNoEncontradoException;

/**
 * Interface PersonaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc del DTO ContenidoEvaluacionDto.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface ContenidoEvaluacionDtoServicioJdbc {

	/**
	 * Enlista los contenidos de la evaluacion que se ha generado para el docente 
	 * @param idTipoEvaluacion - Id del tipo de evaluacion a buscar
	 * @param idEvaluacion - Id de la evaluacion a buscar
	 * @param idCrhr - Id de la carga horaria a buscar
	 * @param idEvaluador - Id usro del evaluador a buscar
	 * @param idEvaluado - Id usro del evaluado a buscar 
	 * @param idCarrera - Id de la carrera a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws ContenidoEvaluacionDtoNoEncontradoException ContenidoEvaluacionDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws ContenidoEvaluacionDtoException ContenidoEvaluacionDtoException excepción general lanzada
	 */
	List<ContenidoEvaluacionDto> listarXTipoEvaluacionXCrhrXEvaluadorXEvaluadoXCarreraXEvaluacion(int idTipoEvaluacion, int idEvaluacion, int idCrhr, int idEvaluador, int idEvaluado, int idCarrera) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException;
	
	/**
	 * Enlista los contenidos de la evaluacion que se ha generado para el docente 
	 * @param idEvaluado - Id usro del evaluado a buscar 
	 * @param idPrac - Id de periodo academico a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	List<ContenidoEvaluacionDto> listarEvaluacionIdXEvaluadoXPeriodo(int idEvaluado, int idPrac) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException;
	
	/**
	 * Enlista los contenidos de la evaluacion que se ha generado para el docente 
	 * @param idAsignacionEvaluador - Id del asignacionEvaluador a buscar
	 * @return Lista de docentes con informacion de tiempo de dedicacion y relacion laboral 
	 * @throws PersonaDatosDtoNoEncontradoException PersonaDatosDtoNoEncontradoException excepción lanzada cuando no existen estudiantes o registros en la consulta
	 * @throws PersonaDatosDtoException PersonaDatosDtoException excepción general lanzada
	 */
	List<ContenidoEvaluacionDto> listarXAsignacionEvaluador(int idAsignacionEvaluador) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException;
	
	/**
	 * Método que permite buscar si el docente a sido evaluado o no.
	 * @param prsIdentificacion - cedula o pasaporte del docente.
	 * @param tipoEvaluacion - tipos de evaluacion
	 * @return 1 si ha sido evaluado.
	 * @throws ContenidoEvaluacionDtoNoEncontradoException
	 * @throws ContenidoEvaluacionDtoException
	 */
	Integer buscarEvaluacionDocente(String prsIdentificacion, int tipoEvaluacion ) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException;

	
	/**
	 * Metodo que devuelve la evaluacion del docente para la apelacion
	 * @param idPeriodo
	 * @param idTipoEvaluacion
	 * @param evaluado
	 * @param evaluador
	 * @return
	 * @throws ContenidoEvaluacionDtoNoEncontradoException
	 * @throws ContenidoEvaluacionDtoException
	 */
	
public List<ContenidoEvaluacionDto> listarXPeriodoXTipoEvaluacionXEvaluadorXEvaluado(int idPeriodo, int idTipoEvaluacion, int usroIdEvaluador,int usroIdEvaluado,int usrId) throws ContenidoEvaluacionDtoNoEncontradoException, ContenidoEvaluacionDtoException;

}
