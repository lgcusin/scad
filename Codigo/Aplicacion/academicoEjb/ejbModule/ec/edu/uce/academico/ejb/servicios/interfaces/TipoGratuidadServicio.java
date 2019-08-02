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

 ARCHIVO:     TipoGratuidadServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Carrera.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadException;
import ec.edu.uce.academico.ejb.excepciones.TipoGratuidadNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.TipoGratuidad;

/**
 * Interface TipoGratuidadServicio
 * Interfase que define las operaciones sobre la tabla TipoGratuidad.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface TipoGratuidadServicio {

	/**
	 * Busca una entidad TipoGratuidad por su id
	 * @param id - deL TipoGratuidad a buscar
	 * @return TipoGratuidad con el id solicitado
	 * @throws TipoGratuidadNoEncontradoException - Excepcion lanzada cuando no se encuentra una TipoGratuidad con el id solicitado
	 * @throws TipoGratuidadException - Excepcion general
	 */
	public TipoGratuidad buscarPorId(Integer id) throws TipoGratuidadNoEncontradoException, TipoGratuidadException;

}
