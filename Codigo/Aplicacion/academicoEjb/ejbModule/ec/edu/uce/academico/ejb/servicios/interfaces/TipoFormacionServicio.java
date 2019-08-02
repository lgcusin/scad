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

 ARCHIVO:     TipoFormacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TipoFormacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 22-05-2017            David Arellano                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.TipoFormacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoFormacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacion;

/**
 * Interface TipoFormacionServicio
 * Interfase que define las operaciones sobre la tabla TipoFormacion.
 * @author darellano
 * @version 1.0
 */
public interface TipoFormacionServicio {
	/**
	 * Busca una entidad TipoFormacion por su id
	 * @param id - del TipoFormacion a buscar
	 * @return TipoFormacion con el id solicitado
	 * @throws TipoFormacionNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoFormacion con el id solicitado
	 * @throws TipoFormacionException - Excepcion general
	 */
	public TipoFormacion buscarPorId(Integer id) throws TipoFormacionNoEncontradoException, TipoFormacionException;

	/**
	 * Lista todas las entidades TipoFormacion existentes en la BD
	 * @return lista de todas las entidades TipoFormacion existentes en la BD
	 */
	public List<TipoFormacion> listarTodos() throws TipoFormacionNoEncontradoException;
	
}
