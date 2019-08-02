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

 ARCHIVO:     VigenciaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Vigencia.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 13-03-2019            	Daniel Albuja                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.VigenciaException;
import ec.edu.uce.academico.ejb.excepciones.VigenciaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Vigencia;

/**
 * Interface VigenciaServicio
 * Interfase que define las operaciones sobre la tabla Vigencia.
 * @author dalbuja
 * @version 1.0
 */
public interface VigenciaServicio {
	/**
	 * Busca una entidad Vigencia por su id
	 * @param id - del Vigencia a buscar
	 * @return Vigencia con el id solicitado
	 * @throws VigenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Vigencia con el id solicitado
	 * @throws VigenciaException - Excepcion general
	 */
	public Vigencia buscarPorId(Integer id) throws VigenciaNoEncontradoException, VigenciaException ;

	/**
	 * Lista todas las entidades Vigencia existentes en la BD
	 * @return lista de todas las entidades Vigencia existentes en la BD
	 */
	public List<Vigencia> listarTodos() throws VigenciaNoEncontradoException, VigenciaException ;
	
}
