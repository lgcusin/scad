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

 ARCHIVO:     TipoSistemaCalificacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla TipoSistemaCalificacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 30-05-2017            David Arellano                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionException;
import ec.edu.uce.academico.ejb.excepciones.TipoSistemaCalificacionNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoSistemaCalificacion;

/**
 * Interface TipoSistemaCalificacionServicio
 * Interfase que define las operaciones sobre la tabla TipoSistemaCalificacion.
 * @author darellano
 * @version 1.0
 */
public interface TipoSistemaCalificacionServicio {

	
	/**
	 * Busca la entidad por la descripcion
	 * @param descripcion - String de la descripcion que se requiere buscar
	 * @return Entidad TipoSistemaCalificacion por la descripcion buscada
	 * @throws TipoSistemaCalificacionNoEncontradoException - Excepcion lanzada cuando no se encuentra un TipoSistemaCalificacion  por descripcion 
	 * @throws TipoSistemaCalificacionException - Excepcion general
	 */
	public TipoSistemaCalificacion buscarXDescripcion(String descripcion) throws TipoSistemaCalificacionNoEncontradoException , TipoSistemaCalificacionException;
	
}
