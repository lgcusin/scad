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

 ARCHIVO:     FichaEstudianteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla FichaEstudiante.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 04-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.dtos.EstudianteJdbcDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.FichaEstudianteNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.FichaEstudiante;
import ec.edu.uce.academico.jpa.entidades.publico.FichaInscripcion;

/**
 * Interface FichaEstudianteServicio
 * Interfase que define las operaciones sobre la tabla FichaEstudiante.
 * @author dcollaguazo
 * @version 1.0
 */
public interface FichaEstudianteServicio {
	/**
	 * Busca una entidad FichaEstudiante por su id
	 * @param id - de la FichaEstudiante a buscar
	 * @return FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante con el id solicitado
	 * @throws FichaEstudianteException - Excepcion general
	 */
	public FichaEstudiante buscarPorId(Integer id) throws FichaEstudianteNoEncontradoException, FichaEstudianteException;

	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */
	public List<FichaEstudiante> listarTodos() throws FichaEstudianteNoEncontradoException , FichaEstudianteException;
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */
	public FichaEstudiante buscarXpersonaId(int personaId) throws FichaEstudianteNoEncontradoException , FichaEstudianteException;

	
	
	public void cambiarNivelacionFichaEstudiante(int fcesId,FichaInscripcion fcin) throws FichaEstudianteNoEncontradoException , FichaEstudianteException;
	
	
	public FichaEstudiante buscarPorFcinId(Integer fcinId) throws FichaEstudianteNoEncontradoException, FichaEstudianteException;

	public void editarObservacionTerceraMatricula(Integer fcesId)
			throws FichaEstudianteNoEncontradoException, FichaEstudianteException;
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD por fcinId
	  * @param id - de la FichaInscripcion a buscar
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */

	public List<FichaEstudiante> listarPorFcinId(Integer fcinId) throws FichaEstudianteException; 
	
	/**
	 * Lista todas las entidades FichaEstudiante existentes en la BD
	 * @return lista de todas las entidades FichaEstudiante existentes en la BD
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */
	
	public FichaEstudiante buscarPorFcinIdNueva(Integer fcinId) throws  FichaEstudianteException;
	
	/**MQ
	 * Editar FichaEstudiante existentes en la BD para cambiar campos de segunda carrera
	  * @param estudianteDto - estudiante a editar 
	 * @throws FichaEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaEstudiante 
	 * @throws FichaEstudianteException - Excepcion general
	 */

	public Boolean editarFcesPorSegundaCarrera(EstudianteJdbcDto estudianteDto) ;



	void editarFcesPorRetorno(List<FichaInscripcionDto> item);

	void habilitarObservacionTerceraMatricula(Integer fcesId)
			throws FichaEstudianteNoEncontradoException, FichaEstudianteException;
	
}
