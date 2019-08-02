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

 ARCHIVO:     RolServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Rol.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 01-MARZ-2017            Dennis Collaguazo                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.jpa.entidades.publico.Titulo;

/**
 * Interface TituloServicio
 * Interfase que define las operaciones sobre la tabla Rol.
 * @author dalbuja
 * @version 1.0
 */
public interface TituloServicio {
	
	public Titulo buscarTituloXDescripcion(String ttlDescripcion);

	Titulo buscarTituloXId(Integer ttlId);

}
