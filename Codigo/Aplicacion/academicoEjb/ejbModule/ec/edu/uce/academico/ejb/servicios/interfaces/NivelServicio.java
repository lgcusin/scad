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

 ARCHIVO:     NivelServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Nivel.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 13-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.NivelException;
import ec.edu.uce.academico.ejb.excepciones.NivelNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Nivel;

/**
 * Interface NivelServicio
 * Interfase que define las operaciones sobre la tabla Nivel.
 * @author dcollaguazo
 * @version 1.0
 */
public interface NivelServicio {
	/**
	 * Busca una entidad Nivel por su id
	 * @param id - del Nivel a buscar
	 * @return Nivel con el id solicitado
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel con el id solicitado
	 * @throws NivelException - Excepcion general
	 */
	public Nivel buscarPorId(Integer id) throws NivelNoEncontradoException, NivelException ;

	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 */
	public List<Nivel> listarTodos() throws NivelNoEncontradoException, NivelException ;
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	public List<Nivel> listarTodosPosgrado() throws NivelNoEncontradoException, NivelException;
	
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	public List<Nivel> listarNivelacion() throws NivelNoEncontradoException, NivelException;
	
	/**
	 * Nivel por numeral
	 * @author Arturo Villafuerte - ajvillafuerte
	 * @param numeral .- numeral del nivel
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	public Nivel listarNivelXNumeral(int numeral) throws NivelNoEncontradoException, NivelException;
	
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	public List<Nivel> listarSuficienciaCulturaFisica() throws NivelNoEncontradoException, NivelException ;
	
	/**
	 * Lista todas las entidades Nivel existentes en la BD
	 * @return lista de todas las entidades Nivel existentes en la BD
	 * @throws NivelNoEncontradoException - Excepcion lanzada cuando no se encuentra un Nivel
	 * @throws NivelException - Excepcion general
	 */
	public List<Nivel> listarSuficienciaIdiomas() throws NivelNoEncontradoException, NivelException ;
	
}
