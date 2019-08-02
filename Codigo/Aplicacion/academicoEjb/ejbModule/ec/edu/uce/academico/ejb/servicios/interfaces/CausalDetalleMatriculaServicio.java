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

 ARCHIVO:     CausalDetalleMatriculaServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla CausalDetalleMatricula.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 24-ENE-2019            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import java.util.List;

import ec.edu.uce.academico.ejb.excepciones.CausalDetalleMatriculaException;
import ec.edu.uce.academico.ejb.excepciones.CausalDetalleMatriculaNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.CausalDetalleMatricula;


public interface CausalDetalleMatriculaServicio {
	/**
	 * Busca una entidad CausalDetalleMatricula por su id
	 * @param id - de la CausalDetalleMatricula a buscar
	 * @return CausalDetalleMatricula con el id solicitado
	 * @throws CausalDetalleMatriculaNoEncontradoException - Excepcion lanzada cuando no se encuentra una CausalDetalleMatricula con el id solicitado
	 * @throws CausalDetalleMatriculaException - Excepcion general
	 */

	public CausalDetalleMatricula buscarPorId(Integer id) throws CausalDetalleMatriculaNoEncontradoException,CausalDetalleMatriculaException;

	
	/**
	 * Lista todas las Causale existentes en la BD por tipo
	 * @return lista de todas las causales existentes en la BD pot tipo
	 */

	public List<CausalDetalleMatricula> listarxdtmtId(int dtmtId);
	
	

}
