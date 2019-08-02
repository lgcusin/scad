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

 ARCHIVO:     GratuidadServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Gratuidad.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
17-01-2018            Arturo villafuerte                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.excepciones.GratuidadException;
import ec.edu.uce.academico.ejb.excepciones.GratuidadNoEncontradoException;
import ec.edu.uce.academico.jpa.entidades.publico.Gratuidad;

/**
 * Interface GratuidadServicio
 * Interfase que define las operaciones sobre la tabla Gratuidad.
 * @author ajvillafuerte
 * @version 1.0
 */
public interface GratuidadServicio {

	/**
	 * Buscar Gratuidad existentes en la BD
	 * @return Buscar la entidade Gratuidad existente en la BD
	 * @throws GratuidadNoEncontradoException - Excepcion lanzada cuando no se encuentra una Gratuidad
	 * @throws GratuidadException - Excepcion general
	 */
	public Gratuidad buscarGratuidadXFichaMatricula(int fcmtId) throws GratuidadNoEncontradoException , GratuidadException;

}
