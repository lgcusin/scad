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

 ARCHIVO:     SistemaCalificacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla SistemaCalificacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 30-MARZ-2017            Gabriel Mafla                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.SistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.SistemaCalificacion;

/**
 * Interface SistemaCalificacionServicio
 * Interfase que define las operaciones sobre la tabla SistemaCalificacion.
 * @author ghmafla
 * @version 1.0
 */
public interface SistemaCalificacionServicio {
	/**
	 * Busca una entidad SistemaCalificacion por su id
	 * @param id - de la SistemaCalificacion a buscar
	 * @return SistemaCalificacion con el id solicitado
	 * @throws SistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una SistemaCalificacion con el id solicitado
	 * @throws SistemaCalificacionException - Excepcion general
	 */
	public SistemaCalificacion buscarPorId(Integer id) throws SistemaCalificacionNoEncontradoException, SistemaCalificacionException;
	
	/**
	 * Lista todas las entidades SistemaCalificacion existentes en la BD
	 * @return lista de todas las entidades SistemaCalificacion existentes en la BD
	 * @throws SistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una SistemaCalificacion 
	 * @throws SistemaCalificacionException - Excepcion general
	 */
	public List<SistemaCalificacion> listarTodos() throws SistemaCalificacionNoEncontradoException , SistemaCalificacionException;
	
	/**
	 * Lista todas SistemaCalificacion por pracId, tissclId y estado existentes en la BD
	 * @return Lista todas carreras por facultad existentes en la BD
	 * @throws SistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra una SistemaCalificacion por pracId, tissclId y estado  
	 * @throws SistemaCalificacionException - Excepcion general
	 */
	public SistemaCalificacion listarSistemaCalificacionXPracXtissclXEstado(int pracId, int tissclId, int estado ) throws SistemaCalificacionNoEncontradoException , SistemaCalificacionException;

}
