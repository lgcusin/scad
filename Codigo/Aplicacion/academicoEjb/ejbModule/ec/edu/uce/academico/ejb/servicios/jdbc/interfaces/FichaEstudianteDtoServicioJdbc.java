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

 ARCHIVO:     FichaEstudianteDtoServicioJdbc.java	  
 DESCRIPCION: Interface donde se registran los metodos para el servicio jdbc de la tabla FichaEstudiante.
 *************************************************************************
                           	MODIFICACIONES

 FECHA         		     AUTOR          					COMENTARIOS
 11-07-2018			Daniel Ortiz				       Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.jdbc.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.FichaEstudianteDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteDtoNoEncontradoException;

/**
 * Interface FichaEstudianteDtoServicioJdbc.
 * Interface donde se registran los metodos para el servicio jdbc de la tabla FichaEstudiante.
 * @author dortiz
 * @version 1.0
 */
public interface FichaEstudianteDtoServicioJdbc {
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por el Id
	 * @param El id de la ficha del estudiante.
	 * @return Lista de FichaEstudianteDto que corresponden a la persona con el Id
	 * @throws FichaEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaEstudianteDto> buscarXId(int fcesId)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException;
	/**
	 * Realiza la busqueda de un FichaMatriculaDto por el Id de la persona
	 * @param El id de la persona dueña de las fichas estudiante 
	 * @return Lista de FichaEstudianteDto que corresponden a la persona con el Id
	 * @throws FichaEstudianteDtoException - Lanzada cuando se ejecuta una excepcion no controlada
	 * @throws FichaEstudianteDtoNoEncontradoException - Lanzada cuando la busqueda no retorna resultados
	 */
	public List<FichaEstudianteDto> buscarXPersonaId(int fcesPersonaId)throws FichaEstudianteDtoException,FichaEstudianteDtoNoEncontradoException;

	
}
