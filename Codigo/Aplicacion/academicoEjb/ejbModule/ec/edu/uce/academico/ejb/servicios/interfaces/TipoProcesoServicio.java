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

 ARCHIVO:     TipoProcesoServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla  TipoProceso.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 14-05-2018            Marcelo Quishpe                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.TipoProcesoException;
import ec.edu.uce.academico.ejb.excepciones.TipoProcesoNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoProceso;

/**
 * Interface TipoProcesoServicio
 * Interfase que define las operaciones sobre la tabla  TipoProceso.
 * @author lmquishpei
 * @version 1.0
 */
public interface TipoProcesoServicio {
	
	/**
	 * Busca una entidad Tipo Proceso por su descripcion
	 * @param descripcion - del Tipo Proceso a buscar
	 * @return Tipo Proceso por su descripcion
	 * @throws TipoProcesoNoEncontradoException - Excepcion lanzada cuando no se encuentra un Tipo de proceso 
	 * @throws TipoProcesoException - Excepcion general
	 */
	public TipoProceso buscarXDescripcion(String descripcion) throws TipoProcesoNoEncontradoException, TipoProcesoException;

	
	
}
