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

 ARCHIVO:     CalificacionServicio.java      
 DESCRIPCIÓN: Interfase que define las operaciones sobre la tabla Calificacion.  
 *************************************************************************
                               MODIFICACIONES

 FECHA                      AUTOR                              COMENTARIOS
 20/06/2018         	   Daniel Albuja                  Emisión Inicial
 ***************************************************************************/
package ec.edu.uce.academico.ejb.servicios.interfaces;

import ec.edu.uce.academico.ejb.dtos.ProgramaPosgradoDto;

/**
 * Interface ProgramaPosgradoServicio
 * Interfase que define las operaciones sobre el programa de posgrado
 * @author dalbuja
 * @version 1.0
 */
public interface ProgramaPosgradoServicio {
	


	public void guardarConfiguracionPrograma(ProgramaPosgradoDto programa, boolean programaExiste);
	
}
