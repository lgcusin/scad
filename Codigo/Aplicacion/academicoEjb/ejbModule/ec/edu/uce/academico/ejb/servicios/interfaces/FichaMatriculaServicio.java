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

 ARCHIVO:     FichaMatriculaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla FichaFichaMatricula.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 09-03-2017          David Arellano                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.FichaMatriculaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.FichaMatricula;

/**
 * Interface FichaFichaMatriculaServicio
 * Interfase que define las operaciones sobre la tabla FichaFichaMatricula.
 * @author darellano
 * @version 1.0
 */
public interface FichaMatriculaServicio {
	/**
	 * Busca una entidad FichaMatricula por su id
	 * @param id - de la FichaMatricula a buscar
	 * @return FichaMatricula con el id solicitado
	 * @throws FichaMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaMatricula con el id solicitado
	 * @throws FichaMatriculaException - Excepcion general
	 */
	public FichaMatricula buscarPorId(Integer id) throws FichaMatriculaNoEncontradoException, FichaMatriculaException;

	/**
	 * Lista todas las entidades FichaMatricula existentes en la BD
	 * @return lista de todas las entidades FichaMatricula existentes en la BD
	 * @throws FichaMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una FichaMatricula 
	 * @throws FichaMatriculaException - Excepcion general
	 */
	public List<FichaMatricula> listarTodos() throws FichaMatriculaNoEncontradoException , FichaMatriculaException;
	
	/**
	 * Busca Lista de  FichaMatricula por  id Ficha estudiante
	 * @param idFces - id del Estudiante a a buscar
	 * @return Lista de  FichaMatricula del idEstudiante. 
	 * 
	 */
	public List<FichaMatricula> ListarFichaMatriculaXfcesId(Integer fcesId) throws  FichaMatriculaException, FichaMatriculaNoEncontradoException;

	FichaMatricula buscarFichaMatriculaXPeriodoActivoXFcesId(Integer fcesId)
			throws FichaMatriculaException, FichaMatriculaNoEncontradoException;

}
