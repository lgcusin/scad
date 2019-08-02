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
 07-AGOS-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.UnidadFormacionException;
import ec.edu.uce.academico.ejb.excepciones.UnidadFormacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.UnidadFormacion;

/**
 * Interface CarreraServicio
 * Interfase que define las operaciones sobre la tabla Carrera.
 * @author dcollaguazo
 * @version 1.0
 */
public interface UnidadFormacionServicio {
	/**
	 * Busca una entidad UnidadFormacion por su id
	 * @param id - de la UnidadFormacion a buscar
	 * @return UnidadFormacion con el id solicitado
	 * @throws UnidadFormacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una UnidadFormacion con el id solicitado
	 * @throws UnidadFormacionException - Excepcion general
	 */
	public UnidadFormacion buscarPorId(Integer id) throws UnidadFormacionNoEncontradoException, UnidadFormacionException ;
	
	/**
	 * Lista todas las entidades UnidadFormacion existentes en la BD
	 * @return lista de todas las entidades UnidadFormacion existentes en la BD
	 * @throws UnidadFormacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una UnidadFormacion 
	 * @throws UnidadFormacionException - Excepcion general
	 */
	public List<UnidadFormacion> listarTodos() throws UnidadFormacionNoEncontradoException , UnidadFormacionException ;
	

}
