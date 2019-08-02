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

 ARCHIVO:     ModalidadServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Modalidad.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 13-03-2019            	Daniel Albuja                 Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.ModalidadException;
import ec.edu.uce.academico.ejb.excepciones.ModalidadNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Modalidad;

/**
 * Interface NivelServicio
 * Interfase que define las operaciones sobre la tabla Modalidad.
 * @author dalbuja
 * @version 1.0
 */
public interface ModalidadServicio {
	/**
	 * Busca una entidad Modalidad por su id
	 * @param id - del Modalidad a buscar
	 * @return Modalidad con el id solicitado
	 * @throws ModalidadNoEncontradoException - Excepcion lanzada cuando no se encuentra un Modalidad con el id solicitado
	 * @throws ModalidadException - Excepcion general
	 */
	public Modalidad buscarPorId(Integer id) throws ModalidadNoEncontradoException, ModalidadException ;

	/**
	 * Lista todas las entidades Modalidad existentes en la BD
	 * @return lista de todas las entidades Modalidad existentes en la BD
	 */
	public List<Modalidad> listarTodos() throws ModalidadNoEncontradoException, ModalidadException ;
	
}
