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

 ARCHIVO:     DetalleCargaHorariaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla DetalleCargaHoraria.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 08-09-2017            Arturo Villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaException;
import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaNoEncontradoException;
import ec.edu.uce.academico.ejb.excepciones.DetalleCargaHorariaValidacionException;
import ec.edu.uce.academico.jpa.entidades.publico.DetalleCargaHoraria;

/**
 * Interface DetalleCargaHorariaServicio
 * Interfase que define las operaciones sobre la tabla DetalleCargaHoraria.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface DetalleCargaHorariaServicio {
	/**
	 * Busca una entidad DetalleCargaHoraria por su id
	 * @param id - de la DetalleCargaHoraria a buscar
	 * @return DetalleCargaHoraria con el id solicitado
	 * @throws DetalleCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleCargaHoraria con el id solicitado
	 * @throws DetalleCargaHorariaException - Excepcion general
	 */
	public DetalleCargaHoraria buscarPorId(Integer id) throws DetalleCargaHorariaNoEncontradoException, DetalleCargaHorariaException;

	/**
	 * Lista todas las entidades DetalleCargaHoraria existentes en la BD
	 * @return lista de todas las entidades DetalleCargaHoraria existentes en la BD
	 * @throws DetalleCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleCargaHoraria 
	 * @throws DetalleCargaHorariaException - Excepcion general
	 */
	public List<DetalleCargaHoraria> listarTodos() throws DetalleCargaHorariaNoEncontradoException , DetalleCargaHorariaException;
	
	/**
	 * Añade una detalle carga horaria en la BD
	 * @return Si se añadio o no la detalle carga horaria
	 * @throws DetalleCargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws DetalleCargaHorariaException - Excepción general
	 */
	public DetalleCargaHoraria anadir(DetalleCargaHoraria entidad) throws DetalleCargaHorariaValidacionException, DetalleCargaHorariaException;
	
	/**
	 * Lista todas las entidades DetalleCargaHoraria existentes en la BD por carga horaria
	 * @param crhrId Id de carga horaria a buscar 
	 * @return lista de todas las entidades DetalleCargaHoraria existentes en la BD
	 * @throws DetalleCargaHorariaNoEncontradoException - Excepcion lanzada cuando no se encuentra una DetalleCargaHoraria 
	 * @throws DetalleCargaHorariaException - Excepcion general
	 */
	public List<DetalleCargaHoraria> listaPorCargaHoraria(int crhrId) throws DetalleCargaHorariaNoEncontradoException , DetalleCargaHorariaException;

	/**
	 * Edita un detalle carga horaria en la BD
	 * @return Si se Edito o no el detalle carga horaria
	 * @throws DetalleCargaHorariaValidacionException - Excepción lanzada en el caso de que no finalizó todas las validaciones
	 * @throws DetalleCargaHorariaException - Excepción general
	 */
	public Boolean editar(DetalleCargaHoraria entidad) throws DetalleCargaHorariaValidacionException, DetalleCargaHorariaException;
	
	/**
	 * Método que permite eliminar un detalle de la carga horaria.
	 * @param entidad
	 * @return
	 * @throws DetalleCargaHorariaValidacionException
	 * @throws DetalleCargaHorariaException
	 */
	public Boolean eliminar(DetalleCargaHoraria entidad) throws DetalleCargaHorariaValidacionException, DetalleCargaHorariaException;

}
