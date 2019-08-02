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

 ARCHIVO:    ReferenciaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Referencia.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
11-12-2018                Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.ReferenciaException;
import ec.edu.uce.academico.ejb.excepciones.ReferenciaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Referencia;

/**
 * Interface ReferenciaServicio
 * Interfase que define las operaciones sobre la tabla Referencia.
 * @author dcollaguazo
 * @version 1.0
 */
public interface ReferenciaServicio {
	/**
	 * Busca una entidad Referencia por su id
	 * @param id - del Referencia a buscar
	 * @return Referencia con el id solicitado
	 * @throws ReferenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra un Referencia con el id solicitado
	 * @throws ReferenciaException - Excepcion general
	 */
	public Referencia buscarPorId(Integer id) throws ReferenciaNoEncontradoException, ReferenciaException ;

	/**
	 * Lista todas las entidades Referencia existentes en la BD
	 * @return lista de todas las entidades Referencia existentes en la BD
	 * @throws ReferenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Referencia 
	 * @throws ReferenciaException - Excepcion general
	 */
	public List<Referencia> listarTodos() throws ReferenciaNoEncontradoException , ReferenciaException ;
	
	/**
	 * Lista todas las entidades Referencia existentes en la BD por empleado
	 * @return lista de todas las entidades Referencia existentes en la BD por empleado
	 * @throws ReferenciaNoEncontradoException - Excepcion lanzada cuando no se encuentra una Referencia 
	 * @throws ReferenciaException - Excepcion general
	 */
	public List<Referencia> buscarXPersona(Integer prsId) throws ReferenciaNoEncontradoException , ReferenciaException ;

}
