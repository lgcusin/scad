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

 ARCHIVO:     TipoFormacionMallaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TipoFormacionMalla.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 22-ABRIL-2017            Gabriel Mafla                   Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.TipoFormacionMallaException;
import ec.edu.uce.academico.ejb.excepciones.TipoFormacionMallaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoFormacionMalla;

/**
 * Interface TipoFormacionMallaServicio
 * Interfase que define las operaciones sobre la tabla TipoFormacionMalla.
 * @author ghmafla
 * @version 1.0
 */
public interface TipoFormacionMallaServicio {
	/**
	 * Busca una entidad TipoFormacionMalla por su id
	 * @param id - del TipoFormacionMalla a buscar
	 * @return TipoFormacionMalla con el id solicitado
	 * @throws TipoFormacionMallaNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoFormacionMalla con el id solicitado
	 * @throws TipoFormacionMallaException - Excepcion general
	 */
	public TipoFormacionMalla buscarPorId(Integer id) throws TipoFormacionMallaNoEncontradoException, TipoFormacionMallaException;

	/**
	 * Lista todas las entidades TipoFormacionMalla existentes en la BD
	 * @return lista de todas las entidades TipoFormacionMalla existentes en la BD
	 */
	public List<TipoFormacionMalla> listarTodos() throws TipoFormacionMallaNoEncontradoException;
	
	/**
	 * Lista todas las entidades Activas de TipoFormacionMalla existentes en la BD
	 * @return lista de todas las entidades Activas de TipoFormacionMalla existentes en la BD
	 */
	public List<TipoFormacionMalla> listarTodosActivas() throws TipoFormacionMallaNoEncontradoException;
	
}
