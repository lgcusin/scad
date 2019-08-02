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

 ARCHIVO:     RecordEstudianteServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla RecordEstudiante.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 09-OCT-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteException;
import ec.edu.uce.academico.ejb.excepciones.RecordEstudianteNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.RecordEstudiante;

/**
 * Interface RecordEstudianteServicio
 * Interfase que define las operaciones sobre la tabla RecordEstudiante.
 * @author dcollaguazo
 * @version 1.0
 */
public interface RecordEstudianteServicio {
	/**
	 * Busca una entidad RecordEstudiante por su id
	 * @param id - de la RecordEstudiante a buscar
	 * @return RecordEstudiante con el id solicitado
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una RecordEstudiante con el id solicitado
	 * @throws RecordEstudianteException - Excepcion general
	 */
	public RecordEstudiante buscarPorId(Integer id) throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

	/**
	 * Lista todas las entidades RecordEstudiante existentes en la BD
	 * @return lista de todas las entidades RecordEstudiante existentes en la BD
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra una RecordEstudiante 
	 * @throws RecordEstudianteException - Excepcion general
	 */
	public List<RecordEstudiante> listarTodos() throws RecordEstudianteNoEncontradoException , RecordEstudianteException;
	
	/**MQ
	 * Lista todas las entidades RecordEstudiante existentes en la BD, por fichaEstudiante
	 * @return lista de todas las entidades RecordEstudiante existentes en la BD, por fichaEstudiante
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra record estudiante
	 * @throws RecordEstudianteException - Excepcion general
	 */
	public List<RecordEstudiante> listarXfcesId(Integer fcesId) throws  RecordEstudianteException;

	public RecordEstudiante buscarPorIdSingle(Integer id)
			throws RecordEstudianteNoEncontradoException, RecordEstudianteException;

	
	/**@author Daniel
	 * Lista todas las entidades RecordEstudiante existentes en la BD del nivel más alto, por prsIdentificacion 
	 * @return Lista todas las entidades RecordEstudiante existentes en la BD del nivel más alto del estudiante
	 * @throws RecordEstudianteNoEncontradoException - Excepcion lanzada cuando no se encuentra record estudiante
	 * @throws RecordEstudianteException - Excepcion general
	 */
	public List<RecordEstudiante> buscarEstadoMateriasActualesPosgrado(String prsIdentificacion, Integer nivelId) throws RecordEstudianteNoEncontradoException;

	/**@author Daniel
	 * Registra al estudiante en el siguiente nivel del posgrado 
	 * @return void
	 * @throws Exception - Excepcion general
	 */
	public void registrarNuevoNivelPosgrado(Integer fcesId, Integer nivelId, Integer pracId);

	void actualizaMatriculadoPorFcesIdPorMlCrPrId(Integer fcesId, Integer mlcrprId) throws RecordEstudianteException;

	void actualizaEstadoRces(Integer rcesId, Integer estado) throws RecordEstudianteException;
	
	
}
