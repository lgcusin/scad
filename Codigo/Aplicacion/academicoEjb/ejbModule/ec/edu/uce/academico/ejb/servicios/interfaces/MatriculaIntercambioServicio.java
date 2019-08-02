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

 ARCHIVO:     CarreraIntercambioServicio.java      
 DESCRIPCIÓN: Interfaz que define las operaciones sobre la tabla CarreraIntercambio.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 05-OCT-2018            Daniel Ortiz   	                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;


import java.util.List;
import java.util.Map;

import ec.edu.uce.academico.ejb.dtos.CarreraDto;
import ec.edu.uce.academico.ejb.dtos.FichaInscripcionDto;
import ec.edu.uce.academico.ejb.dtos.MateriaDto;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.CarreraIntercambioValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.CarreraIntercambio;
import ec.edu.uce.academico.jpa.entidades.publico.Usuario;


/**
 * Interfaz CarreraIntercambioServicio
 * Interfaz que define las operaciones sobre la tabla CarreraIntercambio.
 * @author dortiz
 * @version 1.0
 */
public interface MatriculaIntercambioServicio {
	/**
	 * Busca una entidad CarreraIntercambio por su id
	 * @param id - de la CarreraIntercambio  a buscar
	 * @return CarreraIntercambio con el id solicitado
	 * @throws CarreraIntercambioNoEncontradoException - Excepcion lanzada cuando no se encuentra una CarreraIntercambio con el id solicitado
	 * @throws CarreraIntercambioException - Excepcion general
	 */
	public CarreraIntercambio buscarPorId(Integer id) throws CarreraIntercambioNoEncontradoException, CarreraIntercambioException;
	
	/**
	 * Lista todas las entidades CarreraIntercambio existentes en la BD
	 * @return lista de todas las entidades CarreraIntercambio existentes en la BD
	 * @throws CarreraIntercambioNoEncontradoException - Excepcion lanzada cuando no se encuentra una CarreraIntercambio
	 * @throws CarreraIntercambioaException - Excepcion general
	 */
	public List<CarreraIntercambio> listarTodos() throws CarreraIntercambioNoEncontradoException , CarreraIntercambioException;
	
	/**
	 * Edita todos los atributos de la entidad indicada
	 * @param entidad - entidad a editar
	 * @return true si se guardo la entidad editada, false si no se guardo
	 * @throws CarreraIntercambioValidacionException - Excepción lanzada en el caso de que no finalizo todas las validaciones
	 * @throws CarreraIntercambioException - Excepción general
	*/
	public Boolean editar(CarreraIntercambio entidad) throws CarreraIntercambioValidacionException , CarreraIntercambioException;
	
	/**
	 * Método que permite registrar una materia para el proceso de matriculas de intercambio.
	 * @param entidad - MateriaDto
	 * @return true - si se agrego correctamente.
	 * @throws CarreraIntercambioValidacionException
	 * @throws CarreraIntercambioException
	 */
	Boolean guardar (Map<CarreraDto, List<MateriaDto>> materiasPorCrr, List<FichaInscripcionDto> fichasInscripcion,Usuario usuario) throws CarreraIntercambioValidacionException, CarreraIntercambioException;
	
	/**
	 * Método que permite eliminar una materia para el proceso de matriculas de intercambio.
	 * @param entidad - MateriaDto
	 * @return true - si se agrego correctamente.
	 * @throws CarreraIntercambioValidacionException
	 * @throws CarreraIntercambioException
	 */
	Boolean eliminar(MateriaDto entidad) throws CarreraIntercambioNoEncontradoException, CarreraIntercambioException;
	
}
