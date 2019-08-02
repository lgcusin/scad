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

 ARCHIVO:     TipoSedeServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TipoSede.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 13-03-2019            	Daniel Albuja                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.TipoSedeException;
import ec.edu.uce.academico.ejb.excepciones.TipoSedeNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSede;

/**
 * Interface TipoSedeServicio
 * Interfase que define las operaciones sobre la tabla TipoSede.
 * @author dalbuja
 * @version 1.0
 */
public interface TipoSedeServicio {
	/**
	 * Busca una entidad TipoSede por su id
	 * @param id - del TipoSede a buscar
	 * @return TipoSede con el id solicitado
	 * @throws TipoSedeNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoSede con el id solicitado
	 * @throws TipoSedeException - Excepcion general
	 */
	public TipoSede buscarPorId(Integer id) throws TipoSedeNoEncontradoException, TipoSedeException ;

	/**
	 * Lista todas las entidades TipoSede existentes en la BD
	 * @return lista de todas las entidades TipoSede existentes en la BD
	 */
	public List<TipoSede> listarTodos() throws TipoSedeNoEncontradoException, TipoSedeException ;
	
}
