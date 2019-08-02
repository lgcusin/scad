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

 ARCHIVO:     CarreraServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
20-10-2017            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionException;
import ec.edu.uce.academico.ejb.excepciones.TiempoDedicacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TiempoDedicacion;

/**
 * Interface TiempoDedicacionServicio
 * Interfase que define las operaciones sobre la tabla Carrera.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface TiempoDedicacionServicio {

	/**
	 * Busca una entidad TiempoDedicacion por su id
	 * @param id - deL TiempoDedicacion a buscar
	 * @return TiempoDedicacion con el id solicitado
	 * @throws TiempoDedicacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una TiempoDedicacion con el id solicitado
	 * @throws TiempoDedicacionException - Excepcion general
	 */
	public TiempoDedicacion buscarPorId(Integer id) throws TiempoDedicacionNoEncontradoException, TiempoDedicacionException;

	/**
	 * Lista todas las entidades TiempoDedicacion existentes en la BD
	 * @return lista de todas las entidades Carrera existentes en la BD
	 * @throws TiempoDedicacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una Carrera 
	 * @throws TiempoDedicacionException - Excepcion general
	 */
	public List<TiempoDedicacion> listarTodos() throws TiempoDedicacionNoEncontradoException , TiempoDedicacionException;
	
}
