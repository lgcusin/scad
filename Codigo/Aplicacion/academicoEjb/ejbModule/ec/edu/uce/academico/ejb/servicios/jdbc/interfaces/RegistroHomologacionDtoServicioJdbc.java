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

 ARCHIVO:     RegistroHomologacionDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla RegistroHomologacionDto.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 16-04-2018  		Marcelo Quishpe				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.RegistroHomologacionDto;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoException;
import ec.edu.uce.academico.ejb.excepciones.RegistroHomologacionDtoNoEncontradoException;

/**
 * Interface AulaDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla AulaDto.
 * @author lmquishpei
 * @version 1.0
 */
public interface RegistroHomologacionDtoServicioJdbc {

	/**MQ
	 * Realiza la busqueda de los estudiantes que que se les ha realizado homologación
	 * @param crrId - id de la carrera por la cual se va buscar 
	 * @param identificacionId - identificacion del estudiante a buscar
	 * @return Lista de los estudiantes  homologados en la carrera
	 * @throws RegistroHomologacionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RegistroHomologacionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RegistroHomologacionDto> listarXEstudiantesXcarreraXIdentificacion(int crrId, String identificacion) throws RegistroHomologacionDtoException, RegistroHomologacionDtoNoEncontradoException;
	
	/**MQ
	 * Realiza la busqueda del estudiantes por el id identificación para listar las carreras en las que esta inscrito
	 * @param identificacionId - identificacion del estudiante a buscar
	 * @return lista de estudiante con carreras inscrito
	 * @throws RegistroHomologacionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RegistroHomologacionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RegistroHomologacionDto> listarXEstudianteXIdentificacion(String identificacion) throws RegistroHomologacionDtoException, RegistroHomologacionDtoNoEncontradoException;
	
	
	/**MQ
	 * Realiza la busqueda de los estudiantes que se le ha realizado homologación con lista de carreras, si selecciona todas
	 * @param crrId - id de la carrera por la cual se va buscar 
	 * @param identificacionId - identificacion del estudiante a buscar
	 * @return Lista de los estudiantes  homologados en la carrera
	 * @throws RegistroHomologacionDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws RegistroHomologacionDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<RegistroHomologacionDto> listarXEstudiantesXListaCarreraXIdentificacion(List<CarreraDto> listCarreraDto, int crrId, String identificacion) throws RegistroHomologacionDtoException, RegistroHomologacionDtoNoEncontradoException;

}
