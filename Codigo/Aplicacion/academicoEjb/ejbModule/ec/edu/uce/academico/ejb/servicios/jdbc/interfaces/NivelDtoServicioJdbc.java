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

 ARCHIVO:     NivelDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla nivel.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 17-MARZ-2017		Dennis Collaguazo				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.NivelDto;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcException;
import ec.edu.uce.academico.ejb.excepciones.NivelDtoJdbcNoEncontradoException;


/**
 * Interface CarreraDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla nivel.
 * @author dcollaguazo
 * @version 1.0
 */
public interface NivelDtoServicioJdbc {

	/**
	 * Realiza la busqueda de un nivel por id
	 * @param nivelId - id del nivel
	 * @return Nivel con el id solicitado 
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public NivelDto buscarXId(int nivelId) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;

	/**
	 * Realiza la busqueda de todas las carreras de la aplicacion
	 * @return Lista todas las carreras de la aplicacion
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarTodos() throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;
	
	/**
	 * Método que realiza la consulta de niveles por el id de la carrera
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @return - Retorna la lista de niveles que pertenecen al id de la carrera a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarNivelXCarrera(int idCarrera) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;
	
	/**
	 * Método que realiza la consulta de niveles por periodo por carrera y detalle puesto docente
	 * @param idPeriodo - idPeriodo periodo academico a consultar
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @param docente - docente detalle puesto que pertenece al docente a consultar
	 * @return - Retorna la lista de niveles que pertenecen al periodo, carrera y docente a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarNivelXPeriodoXCarrera(int idPeriodo, int idCarrera, int docente) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;
	
	
	/**
	 * Método que realiza la consulta de niveles por periodo activo por carrera y detalle puesto docente
	 * @param idCarrera - idCarrera que pertenece a la carrera a consultar
	 * @param docente - docente detalle puesto que pertenece al docente a consultar
	 * @return - Retorna la lista de niveles que pertenecen al periodo, carrera y docente a consultar
	 * @throws NivelDtoJdbcException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws NivelDtoJdbcNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<NivelDto> listarNivelXPeriodoActivoXCarrera( int idCarrera, int docente) throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;

	List<NivelDto> listarTodosPosgrado() throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;

	List<NivelDto> listarNivelXCarreraPosgrado(int idCarrera)
			throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;

	List<NivelDto> listarNivelXPeriodoXCarreraPosgrado(int idPeriodo, int idCarrera, int docente)
			throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;

	List<NivelDto> listarNivelXPeriodoActivoXCarreraPosgrado(int idCarrera, int docente)
			throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;

	List<NivelDto> listarNivelXPeriodoActivoXCarreraNivelacion(int idCarrera, int docente)
			throws NivelDtoJdbcException, NivelDtoJdbcNoEncontradoException;

}
