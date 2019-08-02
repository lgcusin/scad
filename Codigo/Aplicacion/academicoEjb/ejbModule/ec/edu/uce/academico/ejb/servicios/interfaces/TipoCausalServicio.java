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

 ARCHIVO:     TipoCausalServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TipoCausal.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 19-ENE-2018            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.CarreraException;
import ec.edu.uce.academico.ejb.excepciones.CarreraNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.TipoCausalException;
import ec.edu.uce.academico.ejb.excepciones.TipoCausalNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoCausal;

/**
 * Busca una entidad Tipo Causal por su id
 * @param id - del Tipo causal a buscar
 * @return Tipo Causal con el id solicitado
 * @throws TipoCausalNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
 * @throws TipoCausalException - Excepcion general
 */
public interface TipoCausalServicio {
	/**
	 * Busca una entidad Carrera por su id
	 * @param id - de la Carrera a buscar
	 * @return Carrera con el id solicitado
	 * @throws CarreraNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera con el id solicitado
	 * @throws CarreraException - Excepcion general
	 */
	public TipoCausal buscarPorId(Integer id) throws TipoCausalNoEncontradoException, TipoCausalException;

	/**
	 * Lista todas los TipoCausal existentes en la BD
	 * @return lista de todas los TipoCausal existentes en la BD
	 */

	public List<TipoCausal> listarTodos() throws TipoCausalNoEncontradoException;
	
	

}
