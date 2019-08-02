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

 ARCHIVO:     CausalServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Causal.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 19-ENE-2018            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.CausalException;
import ec.edu.uce.academico.ejb.excepciones.CausalNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Causal;


public interface CausalServicio {
	/**
	 * Busca una entidad Causal por su id
	 * @param id - de la Causal a buscar
	 * @return Causal con el id solicitado
	 * @throws CausalNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws CausalException - Excepcion general
	 */

	public Causal buscarPorId(Integer id) throws CausalNoEncontradoException, CausalException;

	/**
	 * Lista todas las Causale existentes en la BD
	 * @return lista de todas las causales existentes en la BD
	 */

	public List<Causal> listarTodos() throws CausalNoEncontradoException;
	
	/**
	 * Lista todas las Causale existentes en la BD por tipo
	 * @return lista de todas las causales existentes en la BD pot tipo
	 */
	public List<Causal> listarxTipo(int idTipo) throws CausalNoEncontradoException;
	
	

}
